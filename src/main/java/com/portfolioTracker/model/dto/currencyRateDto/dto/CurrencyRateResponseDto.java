package com.portfolioTracker.model.dto.currencyRateDto.dto;

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
public class CurrencyRateResponseDto {

    @Currency
    private String portfolioCurrency;
    @Currency
    private String eventCurrency;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal rateClientSells;
    @AmountOfMoney
    private BigDecimal rateClientBuys;

}
