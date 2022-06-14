package com.portfolioTracker.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class ValidationException extends RuntimeException {

    public ValidationException(@NotNull String message) {
        super(message);
    }
}
