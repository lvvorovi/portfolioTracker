package com.portfolioTracker.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class ValidationException extends RuntimeException {

    public ValidationException(@NotEmpty String message) {
        super(message);
    }
}
