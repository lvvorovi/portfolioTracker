package portfolioTracker.portfolio.validation.rule.updateRequestRule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundPortfolioException;

import javax.annotation.Priority;
import java.util.Optional;

import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE;

@Component
@Priority(0)
@AllArgsConstructor
public class PortfolioExistsValidationRule implements PortfolioUpdateValidationRule {

    private final PortfolioRepository portfolioRepository;

    @Override
    public void validate(PortfolioDtoUpdateRequest requestDto) {
        Optional<PortfolioEntity> optionalEntity = portfolioRepository.findById(requestDto.getId());
        if (optionalEntity.isEmpty()) throw new PortfolioNotFoundPortfolioException(
                PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);
    }

}
