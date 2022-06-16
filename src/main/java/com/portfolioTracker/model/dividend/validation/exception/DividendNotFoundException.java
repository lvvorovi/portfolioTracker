package com.portfolioTracker.model.dividend.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class DividendNotFoundException extends DividendException {

    public DividendNotFoundException(@NotEmpty String message) {
        super(message);
    }
}
