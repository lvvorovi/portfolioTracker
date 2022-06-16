package com.portfolioTracker.yahooModule.validation.exception;


import javax.validation.constraints.NotEmpty;

public class YahooAPIException extends RuntimeException {

    public YahooAPIException(@NotEmpty String message) {
        super(message);
    }
}
