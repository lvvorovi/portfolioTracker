package com.portfolioTracker.yahooModule.validation.exception;

import javax.validation.constraints.NotEmpty;

public class YahooServiceLogicException extends YahooAPIException {

    public YahooServiceLogicException(@NotEmpty String message) {
        super(message);
    }
}
