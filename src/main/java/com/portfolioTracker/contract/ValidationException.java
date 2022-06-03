package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;

public class ValidationException extends RuntimeException {

    public ValidationException(@NotNull String message) {
        super(message);
    }
}
