package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class TickerNullDividendException extends DividendException {

    public TickerNullDividendException(@NotEmpty String message) {
        super(message);
    }
}
