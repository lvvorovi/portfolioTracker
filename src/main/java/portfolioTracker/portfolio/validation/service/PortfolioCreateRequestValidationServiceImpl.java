package portfolioTracker.portfolio.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.validation.rule.createRequestRule.PortfolioCreateValidationRule;

import java.util.List;

@Component
@AllArgsConstructor
public class PortfolioCreateRequestValidationServiceImpl implements PortfolioCreateRequestValidationService {

    private final List<PortfolioCreateValidationRule> validationRuleList;

    @Override
    public void validate(PortfolioDtoCreateRequest requestDto) {
        validationRuleList.forEach(rule -> rule.validate(requestDto));
    }
}
