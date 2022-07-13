package com.portfolioTracker.domain.portfolio.service;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PortfolioService {

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findById(Long id);

    @PostFilter("filterObject.username == authentication.name")
    List<PortfolioDtoResponse> findAll();

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    void deleteById(Long id);

    boolean isOwner(Long id);

    List<String> findAllPortfolioCurrencies();
}
