package com.portfolioTracker.domain.transaction.validation.exception;

public class PortfolioNotFoundTransactionException extends TransactionException {

    public PortfolioNotFoundTransactionException(String message) {
        super(message);
    }
}
