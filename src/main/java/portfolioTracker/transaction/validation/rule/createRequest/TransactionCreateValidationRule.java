package portfolioTracker.transaction.validation.rule.createRequest;

import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;

public interface TransactionCreateValidationRule {

    void validate(TransactionDtoCreateRequest dtoRequest);

}
