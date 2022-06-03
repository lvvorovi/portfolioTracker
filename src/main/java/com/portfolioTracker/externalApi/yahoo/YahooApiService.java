package com.portfolioTracker.externalApi.yahoo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.externalApi.yahoo.dto.Price;
import com.portfolioTracker.externalApi.yahoo.dto.YahooEvent;
import com.portfolioTracker.externalApi.yahoo.dto.YahooResponseDto;
import com.portfolioTracker.externalApi.yahoo.exception.YahooAPIException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class YahooApiService implements ApiTickerService, ApiCurrencyService {

    private static final String TICKER_ROOT_APP_MAIN_JSON_START = "root.App.main = ";
    private static final String TICKER_ROOT_APP_MAIN_JSON_END = "}(this));";
    private final Map<String, YahooResponseDto> dtoMap = new HashMap<>();

    @Override
    public Boolean isCurrencySupported(String currency) {
        YahooResponseDto responseDto = getObjectFromTicker(currency + currency + "=X");
        return responseDto.getTicker().equalsIgnoreCase(currency + currency + "=X");
    }

    @Override
    public BigDecimal getRateForCurrencyPairOnDate(String portfolioCurrency, String eventCurrency, LocalDate onDate) {
        YahooResponseDto responseDto = getObjectFromTicker(portfolioCurrency + eventCurrency + "=X");
        Price resultPrice;
        try {
            resultPrice = responseDto.getPriceList().stream()
                    .filter(price -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(price.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(onDate))
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + portfolioCurrency + " " + eventCurrency + " for date " + onDate));
        } catch (YahooAPIException e) {
            resultPrice = responseDto.getPriceList().stream()
                    .filter(price -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(price.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(onDate.plusDays(2)))
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + portfolioCurrency + " " + eventCurrency + " for date " + onDate));
        }

        return resultPrice.getClose();
    }

    @Override
    public Boolean isTickerSupported(String ticker) {
        YahooResponseDto responseDto = getObjectFromTicker(ticker);
        return responseDto.getTicker().equalsIgnoreCase(ticker);
    }

    @Override
    public BigDecimal getTickerCurrentPrice(String ticker) {
        return getObjectFromTicker(ticker).getCurrentMarketPrice();
    }

    @Override
    public String getTickerCurrency(String ticker) {
        return getObjectFromTicker(ticker).getCurrency();
    }

    @Override
    public List<YahooEvent> getSplitEventList(@NotNull String ticker) {

        return getObjectFromTicker(ticker).getEventDataList()
                .stream()
                .filter(event -> event.getType().equalsIgnoreCase("SPLIT"))
                .collect(Collectors.toList());
    }

    private YahooResponseDto getObjectFromTicker(String ticker) {
        if (dtoMap.containsKey(ticker)) {
            return dtoMap.get(ticker);
        }
        String response = getYahooCallResponse(ticker);
        String jsonString = getJsonFromResponseString(response);
        YahooResponseDto responseDto = deserializeYahooJson(jsonString);
        dtoMap.put(responseDto.getTicker(), responseDto);
        return responseDto;
    }

    private YahooResponseDto deserializeYahooJson(String jsonString) {
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

    private String getJsonFromResponseString(@NotNull String response) {
        try {
            response = response.substring(
                    response.indexOf(TICKER_ROOT_APP_MAIN_JSON_START) + TICKER_ROOT_APP_MAIN_JSON_START.length(),
                    response.indexOf(TICKER_ROOT_APP_MAIN_JSON_END));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getYahooCallResponse(@NotNull String ticker) {
        System.out.println("request for " + ticker);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://finance.yahoo.com/quote/" + ticker.replace("^", "%5E") + "/history" +
                        "?period1=" +
                        Duration.of(ChronoUnit.DAYS.between(LocalDate.of(1970, 1, 1), LocalDate.of(2018, 1, 1)), ChronoUnit.DAYS).toSeconds()
                        +
                        "&period2=" +
                        Duration.of(ChronoUnit.DAYS.between(LocalDate.of(1970, 1, 1), LocalDate.now()), ChronoUnit.DAYS).toSeconds()
                        + "&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new YahooAPIException(e.toString());
        }
        return response.body();
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
