package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class TickerNotFoundDividendException extends DividendException {

    public TickerNotFoundDividendException(@NotNull String message) {
        super(message);
    }
}
