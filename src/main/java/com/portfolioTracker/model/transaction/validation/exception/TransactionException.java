package com.portfolioTracker.model.transaction.validation.exception;

import com.portfolioTracker.contract.ValidationException;

public class TransactionException extends ValidationException {

    public TransactionException(String message) {
        super(message);
    }
}
