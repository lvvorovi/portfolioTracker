package com.portfolioTracker.domain.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioNameValidationException extends PortfolioValidationException {

    public PortfolioNameValidationException(@NotEmpty String message) {
        super(message);
    }
}
