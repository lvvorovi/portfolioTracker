package com.portfolioTracker.model.transaction.validation.exception;

public class CurrencyNotFoundTransactionException extends TransactionException {

    public CurrencyNotFoundTransactionException(String message) {
        super(message);
    }
}
