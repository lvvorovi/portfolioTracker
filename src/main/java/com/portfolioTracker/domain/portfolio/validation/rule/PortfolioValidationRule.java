package com.portfolioTracker.domain.portfolio.validation.rule;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;

public interface PortfolioValidationRule {


    void validate(PortfolioDtoUpdateRequest requestDto);

    void validate(PortfolioDtoCreateRequest requestDto);
}
