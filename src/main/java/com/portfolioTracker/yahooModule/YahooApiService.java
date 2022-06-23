package com.portfolioTracker.yahooModule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.portfolioTracker.core.contract.ApiCurrencyService;
import com.portfolioTracker.core.contract.ApiTickerService;
import com.portfolioTracker.core.contract.SplitEventDto;
import com.portfolioTracker.yahooModule.config.YahooModuleConfig;
import com.portfolioTracker.yahooModule.dto.YahooResponseDto;
import com.portfolioTracker.yahooModule.validation.exception.YahooAPIException;
import com.portfolioTracker.yahooModule.validation.exception.YahooResponseDtoNullException;
import com.portfolioTracker.yahooModule.dto.YahooCurrencyRateDto;
import com.portfolioTracker.yahooModule.validation.YahooResponseValidationService;
import com.portfolioTracker.yahooModule.validation.annotation.JsonString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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

    private final Map<String, YahooResponseDto> dtoMap = new HashMap<>();

    private final RestTemplate restTemplate;
    private final YahooResponseValidationService responseValidationService;
    private final YahooDtoDeserializer yahooDtoDeserializer;
    private final YahooModuleConfig yahooConfig;

    public YahooApiService(RestTemplate restTemplate, YahooResponseValidationService responseValidationService, YahooDtoDeserializer yahooDtoDeserializer, YahooModuleConfig yahooConfig) {
        this.restTemplate = restTemplate;
        this.responseValidationService = responseValidationService;
        this.yahooDtoDeserializer = yahooDtoDeserializer;
        this.yahooConfig = yahooConfig;
    }

    @Override
    public Boolean isCurrencySupported(String currency) {
        YahooResponseDto responseDto;
        try {
            responseDto = getObjectFromTicker(getTickerFromCurrencies(currency, currency));
        } catch (YahooResponseDtoNullException ex) {
            return false;
        }
        return responseDto.getTicker().equalsIgnoreCase(getTickerFromCurrencies(currency, currency));
    }

    @Override
        public YahooCurrencyRateDto getRateForCurrencyPairOnDate(String currencyFrom, String currencyTo, LocalDate date) {
            YahooResponseDto responseDto;
            try {
                responseDto = getObjectFromTicker(getTickerFromCurrencies(currencyFrom, currencyTo));
            } catch (YahooResponseDtoNullException ex) {
                throw new YahooResponseDtoNullException("currency pair " + currencyFrom + currencyTo +
                        " is not supported by Yahoo Api");
            }

            YahooCurrencyRateDto currencyRateDto = new YahooCurrencyRateDto();
            currencyRateDto.setDate(date);
            currencyRateDto.setEventCurrency(currencyTo);
            currencyRateDto.setPortfolioCurrency(currencyFrom);

            BigDecimal baseRate = getCurrencyBaseRateFromResponseDto(responseDto, date);

            currencyRateDto.setRateClientBuys(baseRate.multiply(new BigDecimal("1.02"))); //TODO
            currencyRateDto.setRateClientSells(baseRate.multiply(new BigDecimal("0.98"))); //TODO

            return currencyRateDto;
        }

    @Override
    public Boolean isTickerSupported(String ticker) {
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
    public BigDecimal getTickerCurrentPrice(String ticker) {
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
    public String getTickerCurrency(String ticker) {
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
    public List<SplitEventDto> getSplitEventList(String ticker) {
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

    private @NotNull YahooResponseDto getObjectFromTicker(@NotBlank String ticker)
            throws YahooResponseDtoNullException{
        if (dtoMap.containsKey(ticker) &&
                dtoMap.get(ticker).getUpdateDateTime().isAfter(LocalDateTime.now().minusMinutes(5))) { //TODO
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

    private String getJsonFromResponseString(@NotBlank String response) {
        return response.substring(
                response.indexOf(yahooConfig.getJsonExtractionStart()) +
                        yahooConfig.getJsonExtractionStart().length(),
                response.indexOf(yahooConfig.getJsonExtractionEnd()));
    }

    private String getYahooResponseString(@NotBlank String ticker) {
// TODO TODO TODO TODO refactoring of hardcoded variables
        String url = "https://finance.yahoo.com/quote/" + ticker.replace("^", "%5E").toUpperCase() +
                "/history" +
                "?period1=" + getSecondsFromDate(LocalDate.of(2019, 1, 1)) +
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

    private BigDecimal getCurrencyBaseRateFromResponseDto(@NotNull YahooResponseDto responseDto, @NotNull @PastOrPresent LocalDate date) {
        try {
            if (date.equals(LocalDate.now())) {
                return responseDto.getCurrentMarketPrice();
            } else {
                return responseDto.getYahooPriceDtoList().stream()
                        .filter(yahooPriceDto -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPriceDto.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(date)) //TODO
                        .findFirst()
                        .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + responseDto.getTicker() + " for date " + date))
                        .getClose();
            }
        } catch (YahooAPIException e) {
            log.info("YahooApi did not provide exchange rate for {} for a date {}. Date week day is {}", responseDto.getTicker(), date, date.getDayOfWeek());
            return responseDto.getYahooPriceDtoList().stream()
                    .filter(yahooPriceDto -> LocalDate.of(1970, 1, 1).plus((Long.parseLong(yahooPriceDto.getDate()) / 24 / 60 / 60), ChronoUnit.DAYS).equals(date.minusDays(2))) //TODO
                    .findFirst()
                    .orElseThrow(() -> new YahooAPIException("Yahoo API did not provide exchange rate for " + responseDto.getTicker() + " for date " + date.minusDays(2)))
                    .getClose();
        }
    }

    private String getSecondsFromDate(@NotNull @PastOrPresent LocalDate localDate) {
        LocalDate yahooBaseDate = LocalDate.of(1970, 1, 1); //TODO
        long days = ChronoUnit.DAYS.between(yahooBaseDate, localDate);
        long seconds = Duration.of(days, ChronoUnit.DAYS).toSeconds();
        return Long.toString(seconds);
    }

    private String getTickerFromCurrencies(String fromCurrency, String toCurrency) {
        return fromCurrency + toCurrency + "=X";
    }
}


