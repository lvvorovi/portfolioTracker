package com.portfolioTracker.domain.dividend.validation.exception;

import com.portfolioTracker.core.contract.ValidationException;

public class DividendException extends ValidationException {

    public DividendException(String message) {
        super(message);
    }
}
