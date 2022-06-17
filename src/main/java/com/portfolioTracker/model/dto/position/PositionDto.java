package com.portfolioTracker.model.dto.position;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioTracker.model.dto.event.EventDto;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PositionDto {

    @NotEmpty
    private String name;
    @NotNull
    private List<EventDto> eventList;
    @NotNull
    private BigDecimal netOriginalCosts;
    @PositiveOrZero
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentSharePrice;
    @NotNull
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @NotNull
    private BigDecimal dividend;
    @NotNull
    private BigDecimal currencyGain;
    @PositiveOrZero
    private BigDecimal commission;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    @JsonProperty("capitalReturn_%")
    private BigDecimal capitalReturn;
    @NotNull
    @JsonProperty("dividendReturn_%")
    private BigDecimal dividendReturn;
    @NotNull
    @JsonProperty("currencyReturn_%")
    private BigDecimal currencyReturn;
    @NotNull
    @JsonProperty("commissionReturn_%")
    private BigDecimal commissionReturn;
    @NotNull
    @JsonProperty("totalReturn_%")
    private BigDecimal totalReturn;
    @NotNull
    @JsonIgnore
    private BigDecimal totalBought;
    @NotNull
    @JsonIgnore
    private BigDecimal totalSold;

}
