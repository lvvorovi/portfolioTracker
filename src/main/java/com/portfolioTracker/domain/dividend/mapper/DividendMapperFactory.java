package com.portfolioTracker.domain.dividend.mapper;

import com.portfolioTracker.core.contract.ModelMapperFactory;

public class DividendMapperFactory implements ModelMapperFactory {

    public DividendMapper getInstance() {
        return new CustomDividendMapper();
    }
}
