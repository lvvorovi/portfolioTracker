package com.portfolioTracker.externalApi.yahoo.validation.exception;


import com.portfolioTracker.contract.ValidationException;

public class YahooAPIException extends ValidationException {

    public YahooAPIException(String message) {
        super(message);
    }
}
