package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class DividendNotFoundException extends DividendException {

    public DividendNotFoundException(@NotNull String message) {
        super(message);
    }
}
