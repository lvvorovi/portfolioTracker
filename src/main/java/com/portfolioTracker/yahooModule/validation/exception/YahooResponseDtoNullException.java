package com.portfolioTracker.yahooModule.validation.exception;

import javax.validation.constraints.NotEmpty;

public class YahooResponseDtoNullException extends YahooAPIException {

    public YahooResponseDtoNullException(@NotEmpty String message) {
        super(message);
    }
}
