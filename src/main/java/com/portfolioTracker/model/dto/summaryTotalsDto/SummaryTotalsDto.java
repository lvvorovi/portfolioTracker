package com.portfolioTracker.model.dto.summaryTotalsDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Validated
@Data
public class SummaryTotalsDto {

    private BigDecimal totalBought;
    @PositiveOrZero
    private BigDecimal totalShares;
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @PositiveOrZero
    private BigDecimal dividend;
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
