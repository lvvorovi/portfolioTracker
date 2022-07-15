package portfolioTracker.transaction.validation.rule;

import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionValidationRule {

    void validate(TransactionDtoCreateRequest dtoRequest);

    void validate(TransactionDtoUpdateRequest dtoRequest);
}
