package com.portfolioTracker.model.dividend.validation.exception;

import com.portfolioTracker.contract.ValidationException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class DividendException extends ValidationException {

    public DividendException(@NotNull String message) {
        super(message);
    }
}
