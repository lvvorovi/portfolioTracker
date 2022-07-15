package com.portfolioTracker.domain.dividend.mapper;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomDividendMapper implements DividendMapper {

    @Override
    public DividendEntity updateToEntity(DividendDtoUpdateRequest dto) {
        DividendEntity entity = new DividendEntity();
        entity.setId(dto.getId());
        entity.setTicker((dto.getTicker()));
        entity.setExDate(dto.getExDate());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public DividendEntity createToEntity(DividendDtoCreateRequest dto) {
        DividendEntity entity = new DividendEntity();
        entity.setTicker((dto.getTicker()));
        entity.setExDate(dto.getExDate());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public DividendDtoResponse toDto(DividendEntity entity) {
        DividendDtoResponse responseDto = new DividendDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setExDate(entity.getExDate());
        responseDto.setDate(entity.getDate());
        responseDto.setAmount(entity.getAmount());
        responseDto.setType(entity.getType());
        responseDto.setUsername(entity.getUsername());
        responseDto.setPortfolioId(entity.getPortfolio().getId());
        return responseDto;
    }
}