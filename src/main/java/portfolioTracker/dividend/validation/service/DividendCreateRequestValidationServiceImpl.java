package portfolioTracker.dividend.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.create.DividendCreateValidationRule;
import portfolioTracker.dividend.validation.service.DividendCreateRequestValidationService;

import java.util.List;

@Service
@AllArgsConstructor
public class DividendCreateRequestValidationServiceImpl implements DividendCreateRequestValidationService {

    private final List<DividendCreateValidationRule> validationRuleList;

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        validationRuleList.forEach(rule -> rule.validate(dto));
    }

}
