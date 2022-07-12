package com.portfolioTracker.domain.portfolio.validation.exception;

import com.portfolioTracker.core.contract.ValidationException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioValidationException extends ValidationException {

    public PortfolioValidationException(@NotEmpty String message) {
        super(message);
    }
}
