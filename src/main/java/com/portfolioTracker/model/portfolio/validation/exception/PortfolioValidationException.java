package com.portfolioTracker.model.portfolio.validation.exception;

import com.portfolioTracker.contract.ValidationException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class PortfolioValidationException extends ValidationException {

    public PortfolioValidationException(@NotNull String message) {
        super(message);
    }
}
