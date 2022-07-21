package portfolioTracker.transaction.validation.service;

import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionUpdateValidationService {

    void validate(TransactionDtoUpdateRequest dtoRequest);

}
