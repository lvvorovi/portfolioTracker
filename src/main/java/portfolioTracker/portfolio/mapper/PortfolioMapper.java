package portfolioTracker.portfolio.mapper;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import javax.validation.Valid;

@Validated
public interface PortfolioMapper {
    @Valid PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest requestDto);

    @Valid PortfolioEntity createToEntity(PortfolioDtoCreateRequest requestDto);

    @Valid PortfolioDtoResponse toDto(PortfolioEntity entity);

}
