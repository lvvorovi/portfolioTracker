package com.portfolioTracker.model.dto.summaryTotalsDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SummaryTotalsDto {

    @AmountOfMoney
    private BigDecimal totalBought;
    @PositiveOrZero
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @PositiveOrZero
    private BigDecimal dividend;
    @AmountOfMoney
    private BigDecimal commission;
    @NotNull
    private BigDecimal currencyGain;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    @JsonProperty("capitalReturn %")
    private BigDecimal capitalReturn;
    @NotNull
    @JsonProperty("commissionReturn %")
    private BigDecimal commissionReturn;
    @NotNull
    @JsonProperty("currencyReturn %")
    private BigDecimal currencyReturn;
    @NotNull
    @JsonProperty("dividendReturn %")
    private BigDecimal DividendReturn;
    @NotNull
    @JsonProperty("totalReturn %")
    private BigDecimal totalReturn;

}
