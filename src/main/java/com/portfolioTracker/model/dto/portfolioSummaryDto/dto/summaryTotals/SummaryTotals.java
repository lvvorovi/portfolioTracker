package com.portfolioTracker.model.dto.portfolioSummaryDto.dto.summaryTotals;

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
public class SummaryTotals {

    @AmountOfMoney
    private BigDecimal totalBought;
    @PositiveOrZero
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGains;
    @PositiveOrZero
    private BigDecimal dividends;
    @AmountOfMoney
    private BigDecimal commission;
    @NotNull
    private BigDecimal currencyGain;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    @JsonProperty("capitalReturn %")
    private BigDecimal capitalGainsPerc;
    @NotNull
    @JsonProperty("commissionReturn %")
    private BigDecimal commissionGainsPerc;
    @NotNull
    @JsonProperty("currencyReturn %")
    private BigDecimal currencyGainsPerc;
    @NotNull
    @JsonProperty("dividendReturn %")
    private BigDecimal DividendsPerc;
    @NotNull
    @JsonProperty("totalReturn %")
    private BigDecimal totalGainsPerc;

}
