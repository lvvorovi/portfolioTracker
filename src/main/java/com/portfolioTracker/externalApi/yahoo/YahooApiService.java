package com.portfolioTracker.externalApi.yahoo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.externalApi.yahoo.dto.YahooEvent;
import com.portfolioTracker.externalApi.yahoo.dto.YahooPrice;
import com.portfolioTracker.externalApi.yahoo.dto.YahooResponseDto;
import com.portfolioTracker.externalApi.yahoo.validation.exception.YahooAPIException;
import com.portfolioTracker.externalApi.yahoo.validation.exception.YahooResponseDtoNullException;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.JsonString;
import com.portfolioTracker.validation.annotation.Ticker;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@Service
public class YahooApiService implements ApiTickerService, ApiCurrencyService {

    private static final String TICKER_ROOT_APP_MAIN_JSON_START = "root.App.main = ";
    private static final String TICKER_ROOT_APP_MAIN_JSON_END = "}(this));";
    private final Map<String, YahooResponseDto> dtoMap = new HashMap<>();

    private final RestTemplate restTemplate;

    public YahooApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean isCurrencySupported(@Currency String currency) {
        YahooResponseDto responseDto = getObjectFromTicker(currency + currency + "=X");
        if (responseDto == null) throw new YahooResponseDtoNullException("isCurrencySupported() method " +
                "received null response from getObjectFromTicker() method");
        return responseDto.getTicker().equalsIgnoreCase(currency + currency + "=X");
    }

    @Override
    public BigDecimal getRateForCurrencyPairOnDate(@Currency String portfolioCurrency, @Currency String eventCurrency, @Date LocalDate onDate) {
        YahooResponseDto responseDto = getObjectFromTicker(portfolioCurrency + eventCurrency + "=X");
        if (responseDto == null) throw new YahooResponseDtoNullException("getRateForCurrencyPairOnDate() method " +
                "received null response from getObjectFromTicker() method");
        YahooPrice resultYahooPrice;
        try {
            if (onDate.equals(LocalDate.now())) return responseDto.getCurrentMarketPrice();
            resultYahooPrice = responseDto.getPriceList().stream()
                    .filter(yahooPrice -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPrice.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(onDate))
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + portfolioCurrency + " " + eventCurrency + " for date " + onDate));
        } catch (YahooAPIException e) {
            resultYahooPrice = responseDto.getPriceList().stream()
                    .filter(yahooPrice -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPrice.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(onDate.minusDays(2)))
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + portfolioCurrency + " " + eventCurrency + " for date " + onDate));
        }

        return resultYahooPrice.getClose();
    }

    @Override
    public Boolean isTickerSupported(@Ticker String ticker) {
        YahooResponseDto responseDto = getObjectFromTicker(ticker);
        if (responseDto == null) throw new YahooResponseDtoNullException("isTickerSupported() method " +
                "received null response from getObjectFromTicker() method");
        return responseDto.getTicker().equalsIgnoreCase(ticker);
    }

    @Override
    public BigDecimal getTickerCurrentPrice(@Ticker String ticker) {
        YahooResponseDto responseDto = getObjectFromTicker(ticker);
        if (responseDto == null) throw new YahooResponseDtoNullException("getTickerCurrentPrice() method " +
                "received null response from getObjectFromTicker() method");
        return responseDto.getCurrentMarketPrice();
    }

    @Override
    public String getTickerCurrency(@Ticker String ticker) {
        YahooResponseDto responseDto = getObjectFromTicker(ticker);
        if (responseDto == null) throw new YahooResponseDtoNullException("getTickerCurrency() method " +
                "received null response from getObjectFromTicker() method");
        return responseDto.getCurrency();
    }

    @Override
    public List<YahooEvent> getSplitEventList(@Ticker String ticker) {
        YahooResponseDto responseDto = getObjectFromTicker(ticker);
        if (responseDto == null) throw new YahooResponseDtoNullException("getSplitEventList() method " +
                "received null response from getObjectFromTicker() method");
        return responseDto.getEventDataList()
                .stream()
                .filter(event -> event.getType().equalsIgnoreCase("SPLIT"))
                .collect(Collectors.toList());
    }

    private YahooResponseDto getObjectFromTicker(@Ticker String ticker) {
        if (dtoMap.containsKey(ticker)) {
            return dtoMap.get(ticker);
        }
        String response = getYahooCallResponse(ticker);
        String jsonString = getJsonFromResponseString(response);
        YahooResponseDto responseDto = deserializeYahooJson(jsonString);
        if (responseDto == null) throw new YahooResponseDtoNullException("getObjectFromTicker() method " +
                "received null response from deserializeYahooJson() method");
        dtoMap.put(responseDto.getTicker(), responseDto);
        return responseDto;
    }

    private YahooResponseDto deserializeYahooJson(@JsonString String jsonString) {
        ObjectMapper yahooDtoMapper = new ObjectMapper();
        yahooDtoMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule("YahooDtoDeserializer");
        module.addDeserializer(YahooResponseDto.class, new YahooDtoDeserializer());
        yahooDtoMapper.registerModule(module);

        try {
            return yahooDtoMapper.readValue(jsonString, YahooResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private String getJsonFromResponseString(@NotEmpty String response) {
        try {
            response = response.substring(
                    response.indexOf(TICKER_ROOT_APP_MAIN_JSON_START) + TICKER_ROOT_APP_MAIN_JSON_START.length(),
                    response.indexOf(TICKER_ROOT_APP_MAIN_JSON_END));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getYahooCallResponse(@Ticker String ticker) {

        String url = "https://finance.yahoo.com/quote/" + ticker.replace("^", "%5E").toUpperCase() +
                "/history" +
                "?period1=" + getSecondsFromDate(LocalDate.of(2018,1,1)) +
                "&period2=" + getSecondsFromDate(LocalDate.now()) +
                "&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true";

        try {
            System.out.println("request for " + ticker);
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new YahooAPIException("request to yahooApi return an exception" +
                    e + " with message " + e.getMessage());
        }
    }

    private String getSecondsFromDate(LocalDate localDate) {
        LocalDate yahooBaseDate = LocalDate.of(1970,1,1);
        long days = ChronoUnit.DAYS.between(yahooBaseDate, localDate);
        long seconds = Duration.of(days, ChronoUnit.DAYS).toSeconds();
        return Long.toString(seconds);

    }


/*

    private List<YahooEvent> getDividendEventList() {
        return currentDto.getContext().getDispatcher().getStores().getHistoricalPriceStore().getEventsDataList()
                .stream()
                .filter(event -> event.getType().equals("DIVIDEND"))
                .collect(Collectors.toList());
    }

    public List<YahooEvent> getSplitEventList(@NotNull String ticker) {
        setCurrentVariables(ticker);
        return currentDto.getContext().getDispatcher().getStores().getHistoricalPriceStore().getEventsDataList()
                .stream()
                .filter(event -> event.getType().equalsIgnoreCase("SPLIT"))
                .collect(Collectors.toList());
    }

    private BigDecimal rateForCurrencyPair(@NotNull String currencyFrom, @NotNull String currencyTo) {
        setCurrentVariables(currencyTo + currencyFrom + "=X");
        return currentDto.getContext().getDispatcher().getStores().getQuoteSummaryStore().getPrice()
                .getRegularMarketPrice().getFmt();
    }

*/

}
