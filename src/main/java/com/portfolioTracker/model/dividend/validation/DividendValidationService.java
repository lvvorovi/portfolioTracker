package com.portfolioTracker.model.dividend.validation;

import com.portfolioTracker.core.contract.ValidationService;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.validation.rule.DividendValidationRule;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class DividendValidationService implements ValidationService<DividendRequestDto> {

    private final List<DividendValidationRule> validationRuleList;

    public DividendValidationService(List<DividendValidationRule> validationRuleList) {
        this.validationRuleList = validationRuleList;
    }

    @Override
    public void validate(@NotNull DividendRequestDto dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }

}
