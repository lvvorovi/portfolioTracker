package portfolioTracker.portfolio.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dto.currency.service.CurrencyService;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.exception.PortfolioCurrencyException;

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
