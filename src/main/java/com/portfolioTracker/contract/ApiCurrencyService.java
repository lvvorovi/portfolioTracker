package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface ApiCurrencyService {

    Boolean isCurrencySupported(@NotNull String currency);

    BigDecimal getRateForCurrencyPairOnDate(@NotNull String currencyFrom, @NotNull String currencyTo, @NotNull LocalDate onDate);

}
