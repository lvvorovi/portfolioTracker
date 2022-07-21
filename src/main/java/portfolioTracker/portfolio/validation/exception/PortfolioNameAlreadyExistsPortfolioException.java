package portfolioTracker.portfolio.validation.exception;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class PortfolioNameAlreadyExistsPortfolioException extends PortfolioValidationException {

    public PortfolioNameAlreadyExistsPortfolioException(@NotEmpty String message) {
        super(message);
    }
}
