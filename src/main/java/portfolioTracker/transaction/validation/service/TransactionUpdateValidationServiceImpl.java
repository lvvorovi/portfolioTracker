package portfolioTracker.transaction.validation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.validation.rule.updateRequest.TransactionUpdateValidationRule;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionUpdateValidationServiceImpl implements TransactionUpdateValidationService {

    private final List<TransactionUpdateValidationRule> validationRuleList;

    @Override
    public void validate(TransactionDtoUpdateRequest dtoRequest) {
        validationRuleList.forEach(rule -> rule.validate(dtoRequest));
    }

}
