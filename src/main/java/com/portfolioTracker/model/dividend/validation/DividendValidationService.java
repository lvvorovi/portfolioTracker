package com.portfolioTracker.model.dividend.validation;

import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.validation.rule.DividendValidationRule;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class DividendValidationService implements ValidationService<DividendRequestDto> {

    private final List<DividendValidationRule> validationRules;

    public DividendValidationService(List<DividendValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    @Override
    public void validate(@NotNull DividendRequestDto dtoRequest) {
        validationRules.forEach(rule -> rule.validate(dtoRequest));
    }

}
