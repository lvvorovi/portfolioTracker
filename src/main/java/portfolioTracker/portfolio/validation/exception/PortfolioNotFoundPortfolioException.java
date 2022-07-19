package portfolioTracker.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioNotFoundPortfolioException extends PortfolioValidationException {

    public PortfolioNotFoundPortfolioException(@NotEmpty String message) {
        super(message);
    }
}
