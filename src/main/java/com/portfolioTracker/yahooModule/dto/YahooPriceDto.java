package com.portfolioTracker.yahooModule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Data
@JsonIgnoreProperties({"data", "type", "numerator", "denominator", "splitRatio", "amount"})
public class YahooPriceDto {

    private String date;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    @JsonProperty("adjclose")
    private BigDecimal adjClose;

}
