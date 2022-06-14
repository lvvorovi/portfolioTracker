package com.portfolioTracker.model.dto.currencyRateDto.service;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.model.dto.currencyRateDto.dto.CurrencyRateResponseDto;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.Date;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Service
public class CurrencyRateService {

    private final ApiCurrencyService apiCurrencyService;

    public CurrencyRateService(ApiCurrencyService apiCurrencyService) {
        this.apiCurrencyService = apiCurrencyService;
    }

    public CurrencyRateResponseDto getRateForPairOnDate(@Currency String portfolioCurrency, @Currency String eventCurrency, @Date LocalDate onDate) {
        CurrencyRateResponseDto responseDto = new CurrencyRateResponseDto();
        responseDto.setDate(onDate);
        responseDto.setPortfolioCurrency(portfolioCurrency);
        responseDto.setEventCurrency(eventCurrency);
        BigDecimal apiCurrencyResponse = apiCurrencyService.getRateForCurrencyPairOnDate(portfolioCurrency, eventCurrency, onDate);
        responseDto.setRateClientSells(apiCurrencyResponse.multiply(new BigDecimal("1.02")));
        responseDto.setRateClientBuys(apiCurrencyResponse.multiply(new BigDecimal("0.98")));
        return responseDto;
    }

    public void loadCurrencyPairsToContext(@Currency String currencyFrom, @Currency String currencyTo) {
        apiCurrencyService.getRateForCurrencyPairOnDate(currencyFrom, currencyTo, LocalDate.now());
    }

}
