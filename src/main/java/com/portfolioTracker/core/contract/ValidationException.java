package com.portfolioTracker.core.contract;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
