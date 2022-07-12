package com.portfolioTracker.domain.portfolio.mapper;

import com.portfolioTracker.core.contract.DomainMapper;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
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
