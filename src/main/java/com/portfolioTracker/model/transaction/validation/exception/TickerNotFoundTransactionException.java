package com.portfolioTracker.model.transaction.validation.exception;

public class TickerNotFoundTransactionException extends TransactionException {

    public TickerNotFoundTransactionException(String message) {
        super(message);
    }
}
