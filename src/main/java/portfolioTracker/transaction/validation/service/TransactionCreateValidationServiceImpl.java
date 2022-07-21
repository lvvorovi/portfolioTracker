package portfolioTracker.transaction.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.validation.rule.createRequest.TransactionCreateValidationRule;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionCreateValidationServiceImpl implements TransactionCreateValidationService {

    private final List<TransactionCreateValidationRule> validationRuleList;

    @Override
    public void validate(TransactionDtoCreateRequest dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }

}
