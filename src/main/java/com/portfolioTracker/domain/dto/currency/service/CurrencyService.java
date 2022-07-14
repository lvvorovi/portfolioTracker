package com.portfolioTracker.domain.dto.currency.service;

import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.domain.dto.currency.CurrencyRateDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
public interface CurrencyService {

    Boolean isCurrencySupported(@Currency String currency);

    CurrencyRateDto getRateForCurrencyPairOnDate(@Currency String currencyFrom, @Currency String currencyTo, @PastOrPresent LocalDate onDate);

}
