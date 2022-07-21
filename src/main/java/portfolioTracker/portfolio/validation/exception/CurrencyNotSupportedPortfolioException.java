package portfolioTracker.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class CurrencyNotSupportedPortfolioException extends PortfolioValidationException {

    public CurrencyNotSupportedPortfolioException(@NotEmpty String message) {
        super(message);
    }
}
