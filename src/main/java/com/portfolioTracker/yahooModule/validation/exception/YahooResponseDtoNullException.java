package com.portfolioTracker.yahooModule.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class YahooResponseDtoNullException extends YahooAPIException {

    public YahooResponseDtoNullException(@NotBlank String message) {
        super(message);
    }
}
