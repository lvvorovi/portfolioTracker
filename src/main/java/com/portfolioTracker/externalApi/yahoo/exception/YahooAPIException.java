package com.portfolioTracker.externalApi.yahoo.exception;


import com.portfolioTracker.contract.ValidationException;

public class YahooAPIException extends ValidationException {

    public YahooAPIException(String message) {
        super(message);
    }
}
