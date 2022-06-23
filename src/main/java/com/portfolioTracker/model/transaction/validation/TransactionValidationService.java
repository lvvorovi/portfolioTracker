package com.portfolioTracker.model.transaction.validation;

import com.portfolioTracker.core.contract.ValidationService;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.validation.rule.TransactionValidationRule;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Service
public class TransactionValidationService implements ValidationService<TransactionRequestDto> {

    private final List<TransactionValidationRule> validationRuleList;

    public TransactionValidationService(List<TransactionValidationRule> validationRuleList) {
        this.validationRuleList = validationRuleList;
    }

    @Override
    public void validate(@NotNull TransactionRequestDto dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }

}
