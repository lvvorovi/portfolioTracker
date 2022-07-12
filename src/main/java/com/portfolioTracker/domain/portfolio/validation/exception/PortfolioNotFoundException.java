package com.portfolioTracker.domain.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioNotFoundException extends PortfolioValidationException {

    public PortfolioNotFoundException(@NotEmpty String message) {
        super(message);
    }
}
