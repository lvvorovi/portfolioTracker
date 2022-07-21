package portfolioTracker.transaction.validation.rule.updateRequest;

import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

public interface TransactionUpdateValidationRule {

    void validate(TransactionDtoUpdateRequest dtoRequest);
}
