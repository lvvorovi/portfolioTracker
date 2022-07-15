package portfolioTracker.transaction.validation;

import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionValidationService {

    void validate(TransactionDtoUpdateRequest dtoRequest);

    void validate(TransactionDtoCreateRequest dtoRequest);
}
