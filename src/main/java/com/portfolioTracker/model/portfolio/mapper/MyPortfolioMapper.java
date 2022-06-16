package com.portfolioTracker.model.portfolio.mapper;

import com.portfolioTracker.contract.ModelMapperContract;
import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Component
public class MyPortfolioMapper implements ModelMapperContract<PortfolioEntity, PortfolioRequestDto, PortfolioResponseDto> {

    private final ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> transactionMapper;
    private final ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> dividendMapper;

    public MyPortfolioMapper(ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> transactionMapper, ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> dividendMapper) {
        this.transactionMapper = transactionMapper;
        this.dividendMapper = dividendMapper;
    }

    @Override
    public PortfolioEntity toEntity(@NotNull PortfolioRequestDto requestDto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(requestDto.getId());
        entity.setName(requestDto.getName());
        entity.setStrategy(requestDto.getStrategy());
        entity.setCurrency(requestDto.getCurrency());
        return entity;
    }

    @Override
    public PortfolioResponseDto toDto(@NotNull PortfolioEntity portfolioEntity) {
        PortfolioResponseDto responseDto = new PortfolioResponseDto();
        responseDto.setId(portfolioEntity.getId());
        responseDto.setName(portfolioEntity.getName());
        responseDto.setStrategy(portfolioEntity.getStrategy());
        responseDto.setCurrency(portfolioEntity.getCurrency());
        responseDto.setTransactionList(mapTransactionEntityListToDtoList(portfolioEntity.getTransactionEntityList()));
        responseDto.setDividendList(mapDividendEntityListToDtoList(portfolioEntity.getDividendEntityList()));
        return responseDto;
    }

    private List<TransactionResponseDto> mapTransactionEntityListToDtoList(List<TransactionEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return null;
    }

    private List<DividendResponseDto> mapDividendEntityListToDtoList(List<DividendEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(dividendMapper::toDto)
                .collect(Collectors.toList());
        return null;
    }

}

