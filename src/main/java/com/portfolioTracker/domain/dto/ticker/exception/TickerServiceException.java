package com.portfolioTracker.domain.dto.ticker.exception;

import com.portfolioTracker.core.validation.ValidationException;

public class TickerServiceException extends ValidationException {

    public TickerServiceException(String message) {
        super(message);
    }
}
