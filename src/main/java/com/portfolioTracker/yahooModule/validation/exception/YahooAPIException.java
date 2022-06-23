package com.portfolioTracker.yahooModule.validation.exception;


import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class YahooAPIException extends RuntimeException {

    public YahooAPIException(@NotBlank String message) {
        super(message);
    }
}
