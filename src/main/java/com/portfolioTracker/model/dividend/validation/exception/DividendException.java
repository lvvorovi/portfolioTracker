package com.portfolioTracker.model.dividend.validation.exception;

import com.portfolioTracker.contract.ValidationException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class DividendException extends ValidationException {

    public DividendException(@NotEmpty String message) {
        super(message);
    }
}
