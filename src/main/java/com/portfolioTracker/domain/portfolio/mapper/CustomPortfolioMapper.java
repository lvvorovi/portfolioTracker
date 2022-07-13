package com.portfolioTracker.domain.portfolio.mapper;

import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomPortfolioMapper implements PortfolioMapper {

    @Override
    public PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest dto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStrategy(dto.getStrategy());
        entity.setCurrency(dto.getCurrency());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public PortfolioEntity createToEntity(PortfolioDtoCreateRequest dto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setName(dto.getName());
        entity.setStrategy(dto.getStrategy());
        entity.setCurrency(dto.getCurrency());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public PortfolioDtoResponse toDto(PortfolioEntity entity) {
        PortfolioDtoResponse responseDto = new PortfolioDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setStrategy(entity.getStrategy());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setUsername(entity.getUsername());
        return responseDto;
    }

}