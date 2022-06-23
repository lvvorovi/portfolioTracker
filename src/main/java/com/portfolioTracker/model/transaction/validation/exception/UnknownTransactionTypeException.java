package com.portfolioTracker.model.transaction.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class UnknownTransactionTypeException extends TransactionException {

    public UnknownTransactionTypeException(@NotBlank String message) {
        super(message);
    }
}
