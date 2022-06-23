package com.portfolioTracker.model.portfolio.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.core.contract.ModelMapperFactory;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;

public class PortfolioMapperFactory implements ModelMapperFactory {

    public ModelMapperContract<PortfolioEntity, PortfolioRequestDto, PortfolioResponseDto> getInstance() {
        return new CustomPortfolioMapper();
    }

}
