package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioCurrencyException;
import org.springframework.stereotype.Component;

@Component
public class PortfolioCurrencyValidationRule implements PortfolioValidationRule {

    private final ApiCurrencyService apiCurrencyService;

    public PortfolioCurrencyValidationRule(ApiCurrencyService apiCurrencyService) {
        this.apiCurrencyService = apiCurrencyService;
    }

    @Override
    public void validate(PortfolioRequestDto portfolioRequestDto) {
        if (!apiCurrencyService.isCurrencySupported(portfolioRequestDto.getCurrency()))
            throw new PortfolioCurrencyException("Currency " + portfolioRequestDto.getCurrency()
                    + " is not supported");
    }
}
