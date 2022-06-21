package com.portfolioTracker.yahooModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.yahooModule.dto.YahooResponseDto;
import com.portfolioTracker.yahooModule.dto.YahooSplitEventDto;
import com.portfolioTracker.yahooModule.validation.annotation.Date;
import com.portfolioTracker.yahooModule.validation.annotation.Ticker;
import com.portfolioTracker.yahooModule.validation.exception.YahooAPIException;
import com.portfolioTracker.yahooModule.validation.exception.YahooResponseDtoNullException;
import com.portfolioTracker.yahooModule.dto.YahooCurrencyRateDto;
import com.portfolioTracker.yahooModule.validation.YahooResponseValidationService;
import com.portfolioTracker.yahooModule.validation.annotation.Currency;
import com.portfolioTracker.yahooModule.validation.annotation.JsonString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class YahooApiService implements ApiTickerService, ApiCurrencyService {

    private static final String TICKER_ROOT_APP_MAIN_JSON_START = "root.App.main = ";
    private static final String TICKER_ROOT_APP_MAIN_JSON_END = "}(this));";
    private final Map<String, YahooResponseDto> dtoMap = new HashMap<>();

    private final RestTemplate restTemplate;
    private final YahooResponseValidationService responseValidationService;
    private final YahooDtoDeserializer yahooDtoDeserializer;

    public YahooApiService(RestTemplate restTemplate, YahooResponseValidationService responseValidationService, YahooDtoDeserializer yahooDtoDeserializer) {
        this.restTemplate = restTemplate;
        this.responseValidationService = responseValidationService;
        this.yahooDtoDeserializer = yahooDtoDeserializer;
    }

    @Override
    public Boolean isCurrencySupported(@Currency String currency) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(currency + currency + "=X");
        } catch (YahooResponseDtoNullException ex) {
            return false;
        }
        return responseDto.getTicker().equalsIgnoreCase(currency + currency + "=X");
    }

    @Override
    public YahooCurrencyRateDto getRateForCurrencyPairOnDate(@Currency String portfolioCurrency, @Currency String eventCurrency, @Date LocalDate date) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(portfolioCurrency + eventCurrency + "=X");
        } catch (YahooResponseDtoNullException ex) {
            throw new YahooResponseDtoNullException("currency pair " + portfolioCurrency + eventCurrency +
                    " is not supported by Yahoo Api");
        }

        YahooCurrencyRateDto currencyRateDto = new YahooCurrencyRateDto();
        currencyRateDto.setDate(date);
        currencyRateDto.setEventCurrency(eventCurrency);
        currencyRateDto.setPortfolioCurrency(portfolioCurrency);

        BigDecimal baseRate = getCurrencyBaseRateFromResponseDto(responseDto, date);

        currencyRateDto.setRateClientBuys(baseRate.multiply(new BigDecimal("1.02")));
        currencyRateDto.setRateClientSells(baseRate.multiply(new BigDecimal("0.98")));

        return currencyRateDto;
    }

    @Override
    public Boolean isTickerSupported(@Ticker String ticker) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(ticker);
        } catch (YahooResponseDtoNullException ex) {
            throw new YahooResponseDtoNullException("ticker " + ticker +
                    " is not supported by Yahoo Api");
        }
        return responseDto.getTicker().equalsIgnoreCase(ticker);
    }

    @Override
    public BigDecimal getTickerCurrentPrice(@Ticker String ticker) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(ticker);
        } catch (YahooResponseDtoNullException ex) {
            throw new YahooResponseDtoNullException("ticker " + ticker +
                    " is not supported by Yahoo Api");
        }
        return responseDto.getCurrentMarketPrice();
    }

    @Override
    public String getTickerCurrency(@Ticker String ticker) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(ticker);
        } catch (YahooResponseDtoNullException ex) {
            throw new YahooResponseDtoNullException("ticker " + ticker +
                    " is not supported by Yahoo Api");
        }
        return responseDto.getCurrency();
    }

    @Override
    public List<YahooSplitEventDto> getSplitEventList(@Ticker String ticker) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(ticker);
        } catch (YahooResponseDtoNullException ex) {
            throw new YahooResponseDtoNullException("ticker " + ticker +
                    " is not supported by Yahoo Api");
        }
        return responseDto.getYahooSplitEventDtoList()
                .stream()
                .filter(event -> event.getType().equalsIgnoreCase("SPLIT"))
                .collect(Collectors.toList());
    }

    private @NotNull YahooResponseDto getObjectFromTicker(@Ticker String ticker) throws YahooResponseDtoNullException{
        if (dtoMap.containsKey(ticker) && dtoMap.get(ticker).getUpdateDateTime().isAfter(LocalDateTime.now().minusMinutes(5))) {
            return dtoMap.get(ticker);
        }
        String response = getYahooResponseString(ticker);
        String jsonString = getJsonFromResponseString(response);
        YahooResponseDto responseDto = deserializeYahooJson(jsonString);
        responseDto.setUpdateDateTime(LocalDateTime.now());
        dtoMap.put(responseDto.getTicker(), responseDto);
        return responseDto;
    }

    private @NotNull YahooResponseDto deserializeYahooJson (@JsonString String jsonString) throws YahooResponseDtoNullException{
        ObjectMapper yahooDtoMapper = new ObjectMapper();
        yahooDtoMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule("YahooDtoDeserializer");
        module.addDeserializer(YahooResponseDto.class, yahooDtoDeserializer);
        yahooDtoMapper.registerModule(module);
        YahooResponseDto responseDto;
        try {
            responseDto = yahooDtoMapper.readValue(jsonString, YahooResponseDto.class);
        } catch (JsonProcessingException | NullPointerException e) {
            throw new YahooResponseDtoNullException("could not deserialize request " + jsonString);
        }

        if (responseDto == null) throw new YahooResponseDtoNullException("deserializeYahooJson() method " +
                "returned null");
        return responseDto;
    }

    private String getJsonFromResponseString(@NotEmpty String response) {
        return response.substring(
                response.indexOf(TICKER_ROOT_APP_MAIN_JSON_START) + TICKER_ROOT_APP_MAIN_JSON_START.length(),
                response.indexOf(TICKER_ROOT_APP_MAIN_JSON_END));
    }

    private String getYahooResponseString(@Ticker String ticker) {

        String url = "https://finance.yahoo.com/quote/" + ticker.replace("^", "%5E").toUpperCase() +
                "/history" +
                "?period1=" + getSecondsFromDate(LocalDate.of(2018, 1, 1)) +
                "&period2=" + getSecondsFromDate(LocalDate.now()) +
                "&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true";

        try {
            log.info("YahooService calling for {}", ticker);
            String responseString = restTemplate.getForObject(url, String.class);
            responseValidationService.validate(responseString);
            return responseString;
        } catch (RestClientException e) {
            throw new YahooAPIException("request to yahooApi caught an exception" +
                    e + " with message " + e.getMessage());
        }
    }

    private BigDecimal getCurrencyBaseRateFromResponseDto(@NotNull YahooResponseDto responseDto, @Date LocalDate date) {
        try {
            if (date.equals(LocalDate.now())) {
                return responseDto.getCurrentMarketPrice();
            } else {
                return responseDto.getYahooPriceDtoList().stream()
                        .filter(yahooPriceDto -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPriceDto.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(date))
                        .findFirst()
                        .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + responseDto.getTicker() + " for date " + date))
                        .getClose();
            }
        } catch (YahooAPIException e) {
            log.info("YahooApi did not provide exchange rate for {} for a date {}. Date week day is {}", responseDto.getTicker(), date, date.getDayOfWeek());
            return responseDto.getYahooPriceDtoList().stream()
                    .filter(yahooPriceDto -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPriceDto.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(date.minusDays(2)))
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + responseDto.getTicker() + " for date " + date.minusDays(2)))
                    .getClose();
        }
    }

    private String getSecondsFromDate(@Date LocalDate localDate) {
        LocalDate yahooBaseDate = LocalDate.of(1970, 1, 1);
        long days = ChronoUnit.DAYS.between(yahooBaseDate, localDate);
        long seconds = Duration.of(days, ChronoUnit.DAYS).toSeconds();
        return Long.toString(seconds);
    }

}


