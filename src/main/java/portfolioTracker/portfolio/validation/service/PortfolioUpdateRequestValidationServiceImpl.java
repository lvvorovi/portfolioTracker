package portfolioTracker.portfolio.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.rule.updateRequestRule.PortfolioUpdateValidationRule;

import java.util.List;

@Component
@AllArgsConstructor
public class PortfolioUpdateRequestValidationServiceImpl implements PortfolioUpdateRequestValidationService {

    private final List<PortfolioUpdateValidationRule> validationRuleList;

    @Override
    public void validate(PortfolioDtoUpdateRequest requestDto) {
        validationRuleList.forEach(rule -> rule.validate(requestDto));
    }
}
