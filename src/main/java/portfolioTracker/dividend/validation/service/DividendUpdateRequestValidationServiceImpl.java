package portfolioTracker.dividend.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.create.DividendCreateValidationRule;
import portfolioTracker.dividend.validation.rule.update.DividendUpdateValidationRule;

import java.util.List;

@Service
@AllArgsConstructor
public class DividendUpdateRequestValidationServiceImpl implements DividendUpdateRequestValidationService {

    private final List<DividendUpdateValidationRule> validationRuleList;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        validationRuleList.forEach(rule -> rule.validate(dto));
    }

}
