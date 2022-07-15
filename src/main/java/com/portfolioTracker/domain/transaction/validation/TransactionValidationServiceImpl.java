package com.portfolioTracker.domain.transaction.validation;

import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.validation.rule.TransactionValidationRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionValidationServiceImpl implements TransactionValidationService {

    private final List<TransactionValidationRule> validationRuleList;

    @Override
    public void validate(TransactionDtoUpdateRequest dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }

    @Override
    public void validate(TransactionDtoCreateRequest dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }
}
