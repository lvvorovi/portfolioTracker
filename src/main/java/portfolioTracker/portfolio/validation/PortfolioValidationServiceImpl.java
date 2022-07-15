package portfolioTracker.portfolio.validation;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.validation.rule.PortfolioValidationRule;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
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
