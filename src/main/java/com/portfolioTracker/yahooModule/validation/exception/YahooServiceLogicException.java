package com.portfolioTracker.yahooModule.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class YahooServiceLogicException extends YahooAPIException {

    public YahooServiceLogicException(@NotBlank String message) {
        super(message);
    }
}
