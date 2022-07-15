package portfolioTracker.portfolio.mapper;

import portfolioTracker.core.contract.DomainMapper;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface PortfolioMapper extends DomainMapper<PortfolioEntity, PortfolioDtoCreateRequest, PortfolioDtoUpdateRequest, PortfolioDtoResponse> {

    @Override
    @Valid PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest dto);

    @Override
    @Valid PortfolioEntity createToEntity(PortfolioDtoCreateRequest dto);

    @Override
    @Valid PortfolioDtoResponse toDto(PortfolioEntity entity);

}
