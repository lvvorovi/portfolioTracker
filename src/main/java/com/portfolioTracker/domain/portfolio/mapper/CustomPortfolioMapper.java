package com.portfolioTracker.domain.portfolio.mapper;

import com.portfolioTracker.core.contract.ModelMapperFactory;
import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.mapper.DividendMapper;
import com.portfolioTracker.domain.dividend.mapper.DividendMapperFactory;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.mapper.TransactionMapper;
import com.portfolioTracker.domain.transaction.mapper.TransactionMapperFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.portfolioTracker.core.contract.ModelMapperFactory.ModelType.DIVIDEND;
import static com.portfolioTracker.core.contract.ModelMapperFactory.ModelType.TRANSACTION;

@Component
public class CustomPortfolioMapper implements PortfolioMapper {

    private final TransactionMapper transactionMapper;

    private final DividendMapper dividendMapper;

    public CustomPortfolioMapper() {
        DividendMapperFactory dividendFactory = (DividendMapperFactory) ModelMapperFactory.getFactoryOfType(DIVIDEND);
        this.dividendMapper = dividendFactory.getInstance();
        TransactionMapperFactory transactionFactory = (TransactionMapperFactory) ModelMapperFactory.getFactoryOfType(TRANSACTION);
        this.transactionMapper = transactionFactory.getInstance();
    }

    @Override
    public PortfolioEntity updateToEntity(PortfolioDtoUpdateRequest dto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStrategy(dto.getStrategy());
        entity.setCurrency(dto.getCurrency());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public PortfolioEntity createToEntity(PortfolioDtoCreateRequest dto) {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setName(dto.getName());
        entity.setStrategy(dto.getStrategy());
        entity.setCurrency(dto.getCurrency());
        entity.setUsername(dto.getUsername());
        return entity;
    }

    @Override
    public PortfolioDtoResponse toDto(PortfolioEntity entity) {
        PortfolioDtoResponse responseDto = new PortfolioDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        responseDto.setStrategy(entity.getStrategy());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setUsername(entity.getUsername());
        responseDto.setTransactionList(mapTransactionEntityListToDtoList(entity.getTransactionEntityList()));
        responseDto.setDividendList(mapDividendEntityListToDtoList(entity.getDividendEntityList()));
        return responseDto;
    }

    private List<TransactionDtoResponse> mapTransactionEntityListToDtoList(List<TransactionEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    private List<DividendDtoResponse> mapDividendEntityListToDtoList(List<DividendEntity> entityList) {
        if (entityList != null) return entityList.stream()
                .map(dividendMapper::toDto)
                .collect(Collectors.toList());
        return new ArrayList<>();
    }

}

