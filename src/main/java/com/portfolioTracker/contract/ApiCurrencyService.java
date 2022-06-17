package com.portfolioTracker.contract;

import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.Date;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public interface ApiCurrencyService {

    Boolean isCurrencySupported(@Currency String currency); //TODO no validation for CURRENCY in input data

    CurrencyRateDto getRateForCurrencyPairOnDate(@Currency String currencyFrom, @Currency String currencyTo, @Date LocalDate onDate);

}
