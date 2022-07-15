package com.portfolioTracker.domain.portfolio.validation;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioValidationService {

    void validate(PortfolioDtoUpdateRequest requestDto);

    void validate(PortfolioDtoCreateRequest requestDto);
}
