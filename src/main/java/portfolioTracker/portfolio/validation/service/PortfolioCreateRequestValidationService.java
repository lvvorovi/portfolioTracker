package portfolioTracker.portfolio.validation.service;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;

public interface PortfolioCreateRequestValidationService {

    void validate(PortfolioDtoCreateRequest requestDto);

}
