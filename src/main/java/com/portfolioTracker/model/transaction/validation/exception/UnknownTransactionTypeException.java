package com.portfolioTracker.model.transaction.validation.exception;

public class UnknownTransactionTypeException extends TransactionException {

    public UnknownTransactionTypeException(String message) {
        super(message);
    }
}
