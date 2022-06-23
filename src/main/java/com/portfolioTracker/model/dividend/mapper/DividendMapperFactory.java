package com.portfolioTracker.model.dividend.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.core.contract.ModelMapperFactory;
import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;

public class DividendMapperFactory implements ModelMapperFactory {

    public ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> getInstance() {
        return new CustomDividendMapper();
    }
}
