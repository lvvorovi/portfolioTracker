package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioCurrencyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;

@Validated
@Component
@Priority(1)
public class PortfolioCurrencyValidationRule implements PortfolioValidationRule {

    private final ApiCurrencyService apiCurrencyService;

    public PortfolioCurrencyValidationRule(ApiCurrencyService apiCurrencyService) {
        this.apiCurrencyService = apiCurrencyService;
    }

    @Override
    public void validate(@NotNull PortfolioRequestDto requestDto) {
        if (!apiCurrencyService.isCurrencySupported(requestDto.getCurrency()))
            throw new PortfolioCurrencyException("Currency " + requestDto.getCurrency()
                    + " is not supported");
    }
}
