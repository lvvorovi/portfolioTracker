package com.portfolioTracker.domain.transaction.validation.exception;

public class IncorrectTransactionTypeForMethodException extends TransactionException {

    public IncorrectTransactionTypeForMethodException(String message) {
        super(message);
    }
}
