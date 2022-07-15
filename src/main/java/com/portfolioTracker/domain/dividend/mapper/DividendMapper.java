package com.portfolioTracker.domain.dividend.mapper;

import com.portfolioTracker.core.contract.DomainMapper;
import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface DividendMapper extends DomainMapper<DividendEntity, DividendDtoCreateRequest, DividendDtoUpdateRequest, DividendDtoResponse> {

    @Override
    @Valid DividendEntity updateToEntity(DividendDtoUpdateRequest dto);

    @Override
    @Valid DividendEntity createToEntity(DividendDtoCreateRequest dto);

    @Override
    @Valid DividendDtoResponse toDto(DividendEntity entity);

}
