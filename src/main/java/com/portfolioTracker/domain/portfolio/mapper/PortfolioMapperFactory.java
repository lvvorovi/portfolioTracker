package com.portfolioTracker.domain.portfolio.mapper;

import com.portfolioTracker.core.contract.DomainMapper;
import com.portfolioTracker.core.contract.ModelMapperFactory;

public class PortfolioMapperFactory implements ModelMapperFactory {

    public DomainMapper getInstance() {
        return new CustomPortfolioMapper();
    }

}
