package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class TickerNullDividendException extends DividendException {

    public TickerNullDividendException(@NotNull String message) {
        super(message);
    }
}
