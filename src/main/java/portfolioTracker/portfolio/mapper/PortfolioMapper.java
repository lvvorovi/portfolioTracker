package portfolioTracker.portfolio.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface PortfolioMapper {
    @Valid PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest dto);

    @Valid PortfolioEntity createToEntity(PortfolioDtoCreateRequest dto);

    @Valid PortfolioDtoResponse toDto(PortfolioEntity entity);

}
