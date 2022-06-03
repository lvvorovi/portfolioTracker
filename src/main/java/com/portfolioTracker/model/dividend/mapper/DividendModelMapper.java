package com.portfolioTracker.model.dividend.mapper;

import com.portfolioTracker.contract.ModelMapperContract;
import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;

@Component
@Priority(0)
public class DividendModelMapper implements
        ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> {

    private final ModelMapper mapper;

    public DividendModelMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public DividendEntity toEntity(@NotNull DividendRequestDto requestDto) {
        return mapper.map(requestDto, DividendEntity.class);
    }

    @Override
    public DividendResponseDto toDto(@NotNull DividendEntity dividendEntity) {
        return mapper.map(dividendEntity, DividendResponseDto.class);
    }
}
