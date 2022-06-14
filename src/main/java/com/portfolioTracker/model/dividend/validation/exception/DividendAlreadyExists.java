package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class DividendAlreadyExists extends DividendException {

    public DividendAlreadyExists(@NotNull String message) {
        super(message);
    }
}
