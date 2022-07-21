package portfolioTracker.portfolio.validation.rule.updateRequestRule;

import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioUpdateValidationRule {

    void validate(PortfolioDtoUpdateRequest requestDto);

}
