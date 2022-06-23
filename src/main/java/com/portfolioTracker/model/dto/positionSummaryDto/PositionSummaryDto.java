package com.portfolioTracker.model.dto.positionSummaryDto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Data
public class PositionSummaryDto {

    private String ticker;
    private BigDecimal totalBough;
    private BigDecimal totalShares;
    private BigDecimal currentSharePrice;
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @NotNull
    private BigDecimal dividend;
    @NotNull
    private BigDecimal commission;
    @NotNull
    private BigDecimal currencyGain;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    private BigDecimal totalReturn;

}
