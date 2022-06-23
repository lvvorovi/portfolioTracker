package com.portfolioTracker.model.dividend.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.transaction.validation.exception.PortfolioNotFoundTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Component
public class CustomDividendMapper implements
        ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public DividendEntity toEntity(@NotNull DividendRequestDto requestDto) {
        DividendEntity entity = new DividendEntity();
        entity.setId(requestDto.getId());
        entity.setTicker((requestDto.getTicker()));
        entity.setExDate(requestDto.getExDate());
        entity.setDate(requestDto.getDate());
        entity.setAmount(requestDto.getAmount());
        entity.setType(requestDto.getType());
        entity.setPortfolio(portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + requestDto.getPortfolioId() + " was not found")));
        return entity;
    }

    @Override
    public DividendResponseDto toDto(@NotNull DividendEntity entity) {
        DividendResponseDto responseDto = new DividendResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setTicker((entity.getTicker()));
        responseDto.setExDate(entity.getExDate());
        responseDto.setDate(entity.getDate());
        responseDto.setAmount(entity.getAmount());
        responseDto.setType(entity.getType());
        responseDto.setPortfolioId(entity.getPortfolio().getId());

        return responseDto;
    }
}