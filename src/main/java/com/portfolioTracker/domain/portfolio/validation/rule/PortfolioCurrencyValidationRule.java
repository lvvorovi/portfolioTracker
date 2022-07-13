package com.portfolioTracker.domain.portfolio.validation.rule;

import com.portfolioTracker.domain.dto.currency.CurrencyService;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.validation.exception.PortfolioCurrencyException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@AllArgsConstructor
@Priority(1)
public class PortfolioCurrencyValidationRule implements PortfolioValidationRule {

    private final CurrencyService currencyService;

    @Override
    public void validate(PortfolioDtoUpdateRequest requestDto) {
        validateCurrency(requestDto.getCurrency());
    }

    @Override
    public void validate(PortfolioDtoCreateRequest requestDto) {
        validateCurrency(requestDto.getCurrency());
    }

    private void validateCurrency(String currency) {
        if (!currencyService.isCurrencySupported(currency))
            throw new PortfolioCurrencyException("Not supported currency: " + currency);
    }
}
