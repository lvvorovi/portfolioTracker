package com.portfolioTracker.domain.portfolio.validation;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.validation.rule.PortfolioValidationRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PortfolioValidationServiceImpl implements PortfolioValidationService {

    private final List<PortfolioValidationRule> validationRules;

        @Override
        public void validate(PortfolioDtoUpdateRequest requestDto) {
                validationRules.forEach(rule -> rule.validate(requestDto));

        }

        @Override
        public void validate(PortfolioDtoCreateRequest requestDto) {
                validationRules.forEach(rule -> rule.validate(requestDto));

        }
}
