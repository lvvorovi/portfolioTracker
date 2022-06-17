package com.portfolioTracker.model.dto.positionSummaryDto;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Quantity;
import com.portfolioTracker.validation.annotation.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PositionSummaryDto {

    @Ticker
    private String ticker;
    @AmountOfMoney
    private BigDecimal totalBough;
    @Quantity
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentSharePrice;
    @AmountOfMoney
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
