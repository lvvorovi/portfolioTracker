package com.portfolioTracker.model.transaction.validation.exception;

import com.portfolioTracker.core.contract.ValidationException;

public class TransactionException extends ValidationException {

    public TransactionException(String message) {
        super(message);
    }
}
