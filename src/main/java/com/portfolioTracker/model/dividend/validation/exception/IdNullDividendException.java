package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class IdNullDividendException extends DividendException {

    public IdNullDividendException(@NotEmpty String message) {
        super(message);
    }
}
