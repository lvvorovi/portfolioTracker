package com.portfolioTracker.model.portfolio.validation;

import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.validation.rule.PortfolioValidationRule;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Service
public class PortfolioValidationService implements ValidationService<PortfolioRequestDto> {

    private final List<PortfolioValidationRule> validationRules;

    public PortfolioValidationService(List<PortfolioValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    @Override
    public void validate(@NotNull PortfolioRequestDto portfolioRequestDto) {
        validationRules.forEach(rule -> rule.validate(portfolioRequestDto));
    }
}
