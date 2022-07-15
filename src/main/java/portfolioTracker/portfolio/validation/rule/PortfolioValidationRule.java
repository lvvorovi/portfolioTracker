package portfolioTracker.portfolio.validation.rule;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioValidationRule {

    void validate(PortfolioDtoUpdateRequest requestDto);

    void validate(PortfolioDtoCreateRequest requestDto);
}
