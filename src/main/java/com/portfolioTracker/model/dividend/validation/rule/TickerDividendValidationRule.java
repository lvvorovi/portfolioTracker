package com.portfolioTracker.model.dividend.validation.rule;

import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.validation.exception.TickerNotFoundDividendException;
import com.portfolioTracker.model.dividend.validation.exception.TickerNullDividendException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;

@Validated
@Component
@Priority(1)
public class TickerDividendValidationRule implements DividendValidationRule {

    private final ApiTickerService apiTickerService;

    public TickerDividendValidationRule(ApiTickerService apiTickerService) {
        this.apiTickerService = apiTickerService;
    }

    @Override
    public synchronized void validate(@NotNull DividendRequestDto dtoRequest) {
        if (dtoRequest.getTicker() == null)
            throw new TickerNullDividendException("Ticker in " + dtoRequest + " shall not be null");

        if (apiTickerService.isTickerSupported(dtoRequest.getTicker())) {
            return;
        }

        throw new TickerNotFoundDividendException("Ticker " + dtoRequest.getTicker() + " is not supported");

    }


}
