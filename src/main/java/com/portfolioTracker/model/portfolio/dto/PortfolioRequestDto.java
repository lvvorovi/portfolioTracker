package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.core.validation.annotation.Currency;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class PortfolioRequestDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String strategy;
    @Currency
    private String currency;
}
