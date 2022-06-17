package com.portfolioTracker.contract;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class CurrencyRateDto {

    @Currency
    String portfolioCurrency;
    @Currency
    String eventCurrency;
    @Date
    LocalDate date;
    @AmountOfMoney
    BigDecimal rateClientSells;
    @AmountOfMoney
    BigDecimal rateClientBuys;

}
