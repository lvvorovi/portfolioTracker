package com.portfolioTracker.core.contract;

import com.portfolioTracker.core.validation.annotation.Currency;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
public interface ApiCurrencyService {

    Boolean isCurrencySupported(@Currency String currency);

    CurrencyRateDto getRateForCurrencyPairOnDate(@Currency String currencyFrom, @Currency String currencyTo, @PastOrPresent LocalDate onDate);

}
