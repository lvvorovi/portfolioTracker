package com.portfolioTracker.model.transaction.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.model.transaction.validation.exception.PortfolioNotFoundTransactionException;
import com.portfolioTracker.model.transaction.validation.exception.UnknownTransactionTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Component
public class CustomTransactionMapper implements
        ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public TransactionEntity toEntity(@NotNull TransactionRequestDto requestDto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(requestDto.getId());
        entity.setTicker((requestDto.getTicker()));
        entity.setShares(requestDto.getShares());
        entity.setPrice(requestDto.getPrice());
        entity.setDate(requestDto.getDate());
        entity.setCommission(requestDto.getCommission());
        entity.setType(requestDto.getType());
        entity.setPortfolio(portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + requestDto.getPortfolioId() + " was not found")));
        return entity;
    }

    @Override
    public TransactionResponseDto toDto(@NotNull TransactionEntity entity) {
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setDate(entity.getDate());
        responseDto.setPrice(entity.getPrice());
        responseDto.setShares(entity.getShares());
        responseDto.setCommission(entity.getCommission());
        responseDto.setType(entity.getType());
        responseDto.setPortfolioId(entity.getPortfolio().getId());
        if (entity.getType().equals(EventType.BUY)) {
            responseDto.setBought(entity.getPrice().multiply(entity.getShares()));
            responseDto.setSold(new BigDecimal(0));
        } else if (entity.getType().equals(EventType.SELL)) {
            responseDto.setSold(entity.getPrice().multiply(entity.getShares()));
            responseDto.setBought(new BigDecimal(0));
        } else {
            throw new UnknownTransactionTypeException("Transaction type "
                    + entity.getType() + "is not known to TransactionMapper of "
                    + this.getClass());
        }
        return responseDto;
    }
}