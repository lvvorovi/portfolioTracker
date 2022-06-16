package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.model.dto.currencyRateDto.service.CurrencyRateService;
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

    private final CurrencyRateService currencyRateService;

    public PortfolioCurrencyValidationRule(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @Override
    public void validate(@NotNull PortfolioRequestDto portfolioRequestDto) {
        if (!currencyRateService.isCurrencySupported(portfolioRequestDto.getCurrency()))
            throw new PortfolioCurrencyException("Currency " + portfolioRequestDto.getCurrency()
                    + " is not supported");
    }
}
