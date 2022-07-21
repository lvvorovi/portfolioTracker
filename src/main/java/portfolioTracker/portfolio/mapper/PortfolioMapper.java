package portfolioTracker.portfolio.mapper;

import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioMapper {
    PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest requestDto);

    PortfolioEntity createToEntity(PortfolioDtoCreateRequest requestDto);

    PortfolioDtoResponse toDto(PortfolioEntity entity);

}
