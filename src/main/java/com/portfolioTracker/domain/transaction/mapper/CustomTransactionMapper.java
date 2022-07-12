package com.portfolioTracker.domain.transaction.mapper;

import com.portfolioTracker.domain.dto.event.eventType.EventType;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.validation.exception.PortfolioNotFoundTransactionException;
import com.portfolioTracker.domain.transaction.validation.exception.UnknownTransactionTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CustomTransactionMapper implements TransactionMapper {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public TransactionEntity updateToEntity(TransactionDtoUpdateRequest dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.getId());
        entity.setTicker((dto.getTicker()));
        entity.setShares(dto.getShares());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setCommission(dto.getCommission());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
        entity.setPortfolio(portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + dto.getPortfolioId() + " was not found")));
        return entity;
    }

    @Override
    public TransactionEntity createToEntity(TransactionDtoCreateRequest dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTicker((dto.getTicker()));
        entity.setShares(dto.getShares());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setCommission(dto.getCommission());
        entity.setType(dto.getType());
        entity.setUsername(dto.getUsername());
        entity.setPortfolio(portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + dto.getPortfolioId() + " was not found")));
        return entity;
    }

    @Override
    public TransactionDtoResponse toDto(TransactionEntity entity) {
        TransactionDtoResponse responseDto = new TransactionDtoResponse();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setDate(entity.getDate());
        responseDto.setPrice(entity.getPrice());
        responseDto.setShares(entity.getShares());
        responseDto.setCommission(entity.getCommission());
        responseDto.setType(entity.getType());
        responseDto.setPortfolioId(entity.getPortfolio().getId());
        responseDto.setUsername(entity.getUsername());
        if (entity.getType().equals(EventType.BUY)) {
            responseDto.setBought(entity.getPrice().multiply(entity.getShares()));
            responseDto.setSold(new BigDecimal(0));
        } else if (entity.getType().equals(EventType.SELL)) {
            responseDto.setSold(entity.getPrice().multiply(entity.getShares()));
            responseDto.setBought(new BigDecimal(0));
        } else {
            throw new UnknownTransactionTypeException("Transaction type "
                    + entity.getType() + "is not known to " + this.getClass());
        }
        return responseDto;
    }
}