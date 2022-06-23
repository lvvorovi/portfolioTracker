package com.portfolioTracker.yahooModule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioTracker.yahooModule.validation.annotation.AmountOfMoney;
import com.portfolioTracker.yahooModule.validation.annotation.Date;
import com.portfolioTracker.yahooModule.validation.annotation.Quantity;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Data
@JsonIgnoreProperties({"data", "type", "numerator", "denominator", "splitRatio", "amount"})
public class YahooPriceDto {

    @Date
    private String date;
    @AmountOfMoney
    private BigDecimal open;
    @AmountOfMoney
    private BigDecimal high;
    @AmountOfMoney
    private BigDecimal low;
    @AmountOfMoney
    private BigDecimal close;
    @Quantity
    private BigDecimal volume;
    @AmountOfMoney
    @JsonProperty("adjclose")
    private BigDecimal adjClose;

}
