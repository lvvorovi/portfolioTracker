package portfolioTracker.portfolio.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNameAlreadyExistsPortfolioException;
import portfolioTracker.portfolio.validation.rule.createRequestRule.PortfolioCreateValidationRule;
import portfolioTracker.portfolio.validation.rule.updateRequestRule.PortfolioUpdateValidationRule;

import javax.annotation.Priority;
import java.util.Optional;

import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
@Priority(1)
public class PortfolioNameValidationRule implements PortfolioUpdateValidationRule, PortfolioCreateValidationRule {

    private final PortfolioRepository portfolioRepository;

    @Override
    public void validate(PortfolioDtoUpdateRequest requestDto) {
        validateName(requestDto.getName());
    }

    @Override
    public void validate(PortfolioDtoCreateRequest requestDto) {
        validateName(requestDto.getName());
    }

    private void validateName(String portfolioName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<PortfolioEntity> optionalEntity = portfolioRepository
                .findByNameAndUsername(portfolioName, username);
        if (optionalEntity.isPresent()) throw new PortfolioNameAlreadyExistsPortfolioException(
                PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE + portfolioName);
    }
}

