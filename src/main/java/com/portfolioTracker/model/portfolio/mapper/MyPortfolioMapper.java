package com.portfolioTracker.model.portfolio.mapper;

import com.portfolioTracker.contract.ModelMapperContract;
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

    public MyPortfolioMapper(ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> transactionMapper) {
        this.transactionMapper = transactionMapper;
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
        responseDto.setTransactions(transactionListToDto(portfolioEntity.getTransactionEntities()));

        return responseDto;
    }

    private List<TransactionResponseDto> transactionListToDto(@NotNull List<TransactionEntity> entities) {
        return entities.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

}

