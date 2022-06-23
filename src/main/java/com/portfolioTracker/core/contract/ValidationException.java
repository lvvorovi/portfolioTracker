package com.portfolioTracker.core.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
public class ValidationException extends RuntimeException {

    public ValidationException(@NotBlank String message) {
        super(message);
    }
}
