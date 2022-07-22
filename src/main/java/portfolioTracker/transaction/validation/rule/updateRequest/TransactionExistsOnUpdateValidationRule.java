package portfolioTracker.transaction.validation.rule.updateRequest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;

import static portfolioTracker.core.ExceptionErrors.TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
public class TransactionExistsOnUpdateValidationRule implements TransactionUpdateValidationRule {

    private final TransactionRepository repository;

    @Override
    public void validate(TransactionDtoUpdateRequest requestDto) {
        boolean exists = repository.existsById(requestDto.getId());
        if (!exists) throw new TransactionNotFoundTransactionException(
                TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);
    }

}
