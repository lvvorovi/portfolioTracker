package com.portfolioTracker.model.transaction.validation.exception;

import com.portfolioTracker.core.contract.ValidationException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public class TransactionException extends ValidationException {

    public TransactionException(@NotBlank String message) {
        super(message);
    }
}
