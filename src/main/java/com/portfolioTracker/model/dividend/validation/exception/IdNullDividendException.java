package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class IdNullDividendException extends DividendException {

    public IdNullDividendException(@NotNull String message) {
        super(message);
    }
}
