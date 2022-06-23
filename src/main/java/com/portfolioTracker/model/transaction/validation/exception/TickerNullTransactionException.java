package com.portfolioTracker.model.transaction.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class TickerNullTransactionException extends TransactionException {

    public TickerNullTransactionException(@NotBlank String message) {
        super(message);
    }
}
