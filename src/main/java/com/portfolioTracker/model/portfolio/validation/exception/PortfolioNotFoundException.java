package com.portfolioTracker.model.portfolio.validation.exception;

import javax.validation.constraints.NotEmpty;

public class PortfolioNotFoundException extends PortfolioValidationException {

    public PortfolioNotFoundException(@NotEmpty String message) {
        super(message);
    }
}
