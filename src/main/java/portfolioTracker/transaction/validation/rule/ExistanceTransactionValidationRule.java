package portfolioTracker.transaction.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;

import java.util.Optional;

import static portfolioTracker.core.ExceptionErrors.TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
public class ExistanceTransactionValidationRule implements TransactionValidationRule {

    private final TransactionRepository repository;

    @Override
    public void validate(TransactionDtoUpdateRequest dto) {
        Optional<TransactionEntity> optionalEntity = repository
                .findById(dto.getId());
        if (optionalEntity.isEmpty()) throw new TransactionNotFoundTransactionException(
                TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE + dto);
    }

    @Override
    public void validate(TransactionDtoCreateRequest dto) {
        //not applicable to TransactionDtoCreateRequest
    }
}
