package com.portfolioTracker.domain.currency;

import com.portfolioTracker.core.validation.annotation.Currency;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
public abstract class CurrencyRateDto {

    @Currency
    String portfolioCurrency;
    @Currency
    String eventCurrency;
    @NotNull
    @PastOrPresent
    LocalDate date;
    @NotNull
    @NumberFormat
    @Positive
    BigDecimal rateClientSells;
    @NotNull
    @NumberFormat
    @Positive
    BigDecimal rateClientBuys;

}
