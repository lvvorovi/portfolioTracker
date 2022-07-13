package com.portfolioTracker.domain.transaction.validation.rule;

import com.portfolioTracker.domain.dto.ticker.TickerService;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.validation.exception.TransactionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@AllArgsConstructor
@Priority(100)
public class TickerTransactionValidationRule implements TransactionValidationRule {

    private final TickerService tickerService;

    @Override
    public void validate(TransactionDtoCreateRequest dtoRequest) {
        validateTicker(dtoRequest.getTicker());
    }

    @Override
    public void validate(TransactionDtoUpdateRequest dtoRequest) {
        validateTicker(dtoRequest.getTicker());
    }

    private void validateTicker(String ticker) {
        if (!tickerService.isTickerSupported(ticker)) {
            throw new TransactionException("Not supported ticker: " + ticker);
        }
    }
}
