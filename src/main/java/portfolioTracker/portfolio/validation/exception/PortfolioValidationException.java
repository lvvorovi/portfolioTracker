package portfolioTracker.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.validation.ValidationException;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioValidationException extends ValidationException {

    public PortfolioValidationException(@NotEmpty String message) {
        super(message);
    }
}
