package portfolioTracker.portfolio.validation.rule.createRequestRule;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;

public interface PortfolioCreateValidationRule {

    void validate(PortfolioDtoCreateRequest requestDto);

}
