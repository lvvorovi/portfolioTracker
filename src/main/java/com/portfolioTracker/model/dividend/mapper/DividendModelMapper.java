//package com.portfolioTracker.model.dividend.mapper;
//
//import com.portfolioTracker.core.contract.ModelMapperContract;
//import com.portfolioTracker.model.dividend.DividendEntity;
//import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
//import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//
//import javax.validation.constraints.NotNull;
//
//@Component
//@Validated
//public class DividendModelMapper implements
//        ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> {
//
//    @Autowired
//    private ModelMapper mapper;
//
//    @Override
//    public DividendEntity toEntity(@NotNull DividendRequestDto requestDto) {
//        return mapper.map(requestDto, DividendEntity.class);
//    }
//
//    @Override
//    public DividendResponseDto toDto(@NotNull DividendEntity dividendEntity) {
//        return mapper.map(dividendEntity, DividendResponseDto.class);
//    }
//}
