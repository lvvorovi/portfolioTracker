package com.portfolioTracker.domain.portfolio.service;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface PortfolioService {

    PortfolioDtoResponse findById(Long id);

    List<PortfolioDtoResponse> findAll();

    PortfolioDtoResponse save(PortfolioDtoCreateRequest dto);

    PortfolioDtoResponse update(PortfolioDtoUpdateRequest dto);

    void deleteById(Long id);

    void loadTickersToContext();
}
