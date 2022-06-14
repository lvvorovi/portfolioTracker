package com.portfolioTracker.model.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class PortfolioNameValidationException extends PortfolioValidationException {

    public PortfolioNameValidationException(@NotNull String message) {
        super(message);
    }
}
