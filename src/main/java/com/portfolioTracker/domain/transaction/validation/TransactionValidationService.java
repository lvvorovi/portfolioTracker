package com.portfolioTracker.domain.transaction.validation;

import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionValidationService {

    void validate(TransactionDtoUpdateRequest dtoRequest);

    void validate(TransactionDtoCreateRequest dtoRequest);
}
