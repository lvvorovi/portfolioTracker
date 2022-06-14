package com.portfolioTracker.model.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class PortfolioCurrencyException extends PortfolioValidationException {

    public PortfolioCurrencyException(@NotNull String message) {
        super(message);
    }
}
