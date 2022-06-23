package com.portfolioTracker.model.portfolio.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.core.contract.ModelMapperFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolioTracker.core.contract.ModelMapperFactory.ModelType.DIVIDEND;
import static com.portfolioTracker.core.contract.ModelMapperFactory.ModelType.TRANSACTION;

@Validated
@Component
public class CustomPortfolioMapper implements ModelMapperContract<PortfolioEntity, PortfolioRequestDto, PortfolioResponseDto> {

    private final ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> transactionMapper =
            ModelMapperFactory.getFactoryOfType(TRANSACTION).getInstance();

    private final ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> dividendMapper =
            ModelMapperFactory.getFactoryOfType(DIVIDEND).getInstance();


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
    public PortfolioResponseDto toDto(@NotNull PortfolioEntity entity) {
        PortfolioResponseDto responseDto = new PortfolioResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setStrategy(entity.getStrategy());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setTransactionList(mapTransactionEntityListToDtoList(entity.getTransactionEntityList()));
        responseDto.setDividendList(mapDividendEntityListToDtoList(entity.getDividendEntityList()));
        return responseDto;
    }

    private List<TransactionResponseDto> mapTransactionEntityListToDtoList(List<TransactionEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    private List<DividendResponseDto> mapDividendEntityListToDtoList(List<DividendEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(dividendMapper::toDto)
                .collect(Collectors.toList());
        return new ArrayList<>();
    }

}

