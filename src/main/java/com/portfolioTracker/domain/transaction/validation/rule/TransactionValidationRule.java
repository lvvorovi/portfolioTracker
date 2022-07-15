package com.portfolioTracker.domain.transaction.validation.rule;

import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionValidationRule {

    void validate(TransactionDtoCreateRequest dtoRequest);

    void validate(TransactionDtoUpdateRequest dtoRequest);
}
