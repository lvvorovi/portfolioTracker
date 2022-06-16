package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class TickerNotFoundDividendException extends DividendException {

    public TickerNotFoundDividendException(@NotEmpty String message) {
        super(message);
    }
}
