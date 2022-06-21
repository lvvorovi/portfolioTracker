package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.core.validation.annotation.ModelName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
public class PortfolioRequestDto {

    private Long id;
    @ModelName
    private String name;
    @NotEmpty
    private String strategy;
    @Currency
    private String currency;
}
