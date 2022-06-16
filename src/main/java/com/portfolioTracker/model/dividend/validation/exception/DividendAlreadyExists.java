package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class DividendAlreadyExists extends DividendException {

    public DividendAlreadyExists(@NotEmpty String message) {
        super(message);
    }
}
