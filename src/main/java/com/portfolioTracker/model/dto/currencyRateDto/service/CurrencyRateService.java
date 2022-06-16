//package com.portfolioTracker.model.dto.currencyRateDto.service;
//
//import com.portfolioTracker.contract.ApiCurrencyService;
//import com.portfolioTracker.contract.CurrencyRateResponse;
//import com.portfolioTracker.validation.annotation.Currency;
//import com.portfolioTracker.validation.annotation.Date;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//import java.time.LocalDate;
//
//@Validated
//@Service
//public class CurrencyRateService {
//
//    private final ApiCurrencyService apiCurrencyService;
//
//    public CurrencyRateService(ApiCurrencyService apiCurrencyService) {
//        this.apiCurrencyService = apiCurrencyService;
//    }
//
//    public CurrencyRateResponse getRateForPairOnDate(@Currency String portfolioCurrency, @Currency String eventCurrency, @Date LocalDate onDate) {
//        return apiCurrencyService.getRateForCurrencyPairOnDate(portfolioCurrency, eventCurrency, onDate);
//    }
//
//    public void loadCurrencyPairsToContext(@Currency String currencyFrom, @Currency String currencyTo) {
//        apiCurrencyService.getRateForCurrencyPairOnDate(currencyFrom, currencyTo, LocalDate.now());
//    }
//
//    public boolean isCurrencySupported(@Currency String currency) {
//        return apiCurrencyService.isCurrencySupported(currency);
//    }
//}
