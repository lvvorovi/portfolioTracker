package com.portfolioTracker.model.transaction.validation.rule;

import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.validation.exception.TickerNullTransactionException;
import com.portfolioTracker.model.transaction.validation.exception.TransactionException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Component
public class TickerTransactionValidationRule implements TransactionValidationRule {

    private final ApiTickerService apiTickerService;

    public TickerTransactionValidationRule(ApiTickerService apiTickerService) {
        this.apiTickerService = apiTickerService;
    }

    @Override
    public synchronized void validate(@NotNull TransactionRequestDto dtoRequest) {
        if (dtoRequest.getTicker() == null)
            throw new TickerNullTransactionException("Ticker in " + dtoRequest + " shall not be null");

        if (!apiTickerService.isTickerSupported(dtoRequest.getTicker())) {
            throw new TransactionException("Ticker " + dtoRequest.getTicker() + " is not supported");
        }
    }
}
