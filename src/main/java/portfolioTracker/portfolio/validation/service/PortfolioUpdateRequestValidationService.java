package portfolioTracker.portfolio.validation.service;

import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioUpdateRequestValidationService {

    void validate(PortfolioDtoUpdateRequest requestDto);

}
