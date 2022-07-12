package com.portfolioTracker.domain.dividend.mapper;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.transaction.validation.exception.PortfolioNotFoundTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
public class CustomDividendMapper implements DividendMapper {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public DividendEntity updateToEntity(DividendDtoUpdateRequest dto) {
        DividendEntity entity = new DividendEntity();
        entity.setId(dto.getId());
        entity.setTicker((dto.getTicker()));
        entity.setExDate(dto.getExDate());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setPortfolio(portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + dto.getPortfolioId() + " was not found")));
        return entity;
    }

    public DividendEntity createToEntity(DividendDtoCreateRequest dto) {
        DividendEntity entity = new DividendEntity();
        entity.setTicker((dto.getTicker()));
        entity.setExDate(dto.getExDate());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setPortfolio(portfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id " + dto.getPortfolioId() + " was not found")));
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
        responseDto.setPortfolioId(entity.getPortfolio().getId());

        return responseDto;
    }
}