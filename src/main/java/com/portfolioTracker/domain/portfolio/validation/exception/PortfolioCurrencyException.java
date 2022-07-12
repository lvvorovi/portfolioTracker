package com.portfolioTracker.domain.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioCurrencyException extends PortfolioValidationException {

    public PortfolioCurrencyException(@NotEmpty String message) {
        super(message);
    }
}
