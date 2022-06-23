package com.portfolioTracker.model.transaction.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class PortfolioNotFoundTransactionException extends TransactionException {

    public PortfolioNotFoundTransactionException(@NotBlank String message) {
        super(message);
    }
}
