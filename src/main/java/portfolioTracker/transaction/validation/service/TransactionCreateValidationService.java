package portfolioTracker.transaction.validation.service;

import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;

public interface TransactionCreateValidationService {

    void validate(TransactionDtoCreateRequest dtoRequest);


}
