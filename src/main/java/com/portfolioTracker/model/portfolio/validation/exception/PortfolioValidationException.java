package com.portfolioTracker.model.portfolio.validation.exception;

import com.portfolioTracker.contract.ValidationException;

public class PortfolioValidationException extends ValidationException {

    public PortfolioValidationException(String message) {
        super(message);
    }
}
