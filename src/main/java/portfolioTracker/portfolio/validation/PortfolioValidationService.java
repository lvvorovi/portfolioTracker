package portfolioTracker.portfolio.validation;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioValidationService {

    void validate(PortfolioDtoUpdateRequest requestDto);

    void validate(PortfolioDtoCreateRequest requestDto);
}
