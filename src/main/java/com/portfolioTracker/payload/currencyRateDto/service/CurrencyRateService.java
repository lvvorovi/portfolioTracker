package com.portfolioTracker.payload.currencyRateDto.service;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.payload.currencyRateDto.dto.CurrencyRateResponseDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CurrencyRateService {

    private final ApiCurrencyService apiCurrencyService;

    public CurrencyRateService(ApiCurrencyService apiCurrencyService) {
        this.apiCurrencyService = apiCurrencyService;
    }

    public CurrencyRateResponseDto getRateForPairOnDate(@NotNull String portfolioCurrency, @NotNull String eventCurrency, @NotNull LocalDate onDate) {
        CurrencyRateResponseDto responseDto = new CurrencyRateResponseDto();
        responseDto.setDate(onDate);
        responseDto.setPortfolioCurrency(portfolioCurrency);
        responseDto.setEventCurrency(eventCurrency);
        BigDecimal apiCurrencyResponse = apiCurrencyService.getRateForCurrencyPairOnDate(portfolioCurrency, eventCurrency, onDate);
        responseDto.setRateClientSells(apiCurrencyResponse.multiply(new BigDecimal("1.02")));
        responseDto.setRateClientBuys(apiCurrencyResponse.multiply(new BigDecimal("0.98")));
        return responseDto;
    }

    public void loadCurrencyPairsToContext(String currencyFrom, String currenctyTo) {
        apiCurrencyService.getRateForCurrencyPairOnDate(currencyFrom, currenctyTo, LocalDate.now());
    }

}
