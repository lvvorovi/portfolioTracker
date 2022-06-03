package com.portfolioTracker.model.transaction.validation;

import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.validation.rule.TransactionValidationRule;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class TransactionValidationService implements ValidationService<TransactionRequestDto> {

    private final List<TransactionValidationRule> validationRules;

    public TransactionValidationService(List<TransactionValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    @Override
    public void validate(@NotNull TransactionRequestDto dtoRequest) {
        validationRules.forEach(rule -> rule.validate(dtoRequest));
    }

}
