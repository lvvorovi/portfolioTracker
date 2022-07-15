package com.portfolioTracker.domain.dividend.validation;

import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.validation.rule.DividendValidationRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DividendValidationServiceImpl implements DividendValidationService {

    private final List<DividendValidationRule> validationRuleList;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        validationRuleList.forEach(rule -> rule.validate(dto));
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        validationRuleList.forEach(rule -> rule.validate(dto));
    }

}
