package portfolioTracker.portfolio.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dto.currency.service.CurrencyService;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.exception.CurrencyNotSupportedPortfolioException;
import portfolioTracker.portfolio.validation.rule.createRequestRule.PortfolioCreateValidationRule;
import portfolioTracker.portfolio.validation.rule.updateRequestRule.PortfolioUpdateValidationRule;

import javax.annotation.Priority;

import static portfolioTracker.core.ExceptionErrors.CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
@Priority(100)
public class PortfolioCurrencyValidationRule implements PortfolioCreateValidationRule, PortfolioUpdateValidationRule {

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
            throw new CurrencyNotSupportedPortfolioException(
                    CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE + currency);
    }
}
