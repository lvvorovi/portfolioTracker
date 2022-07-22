package portfolioTracker.transaction.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.validation.rule.TickerTransactionValidationRule;
import portfolioTracker.transaction.validation.rule.updateRequest.TransactionExistsOnUpdateValidationRule;
import portfolioTracker.transaction.validation.rule.updateRequest.TransactionUpdateValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionUpdateValidationServiceImplTest {

    @Mock
    TickerTransactionValidationRule tickerTransactionValidationRule;
    @Mock
    TransactionExistsOnUpdateValidationRule transactionExistsOnUpdateValidationRule;

    List<TransactionUpdateValidationRule> ruleList;
    TransactionUpdateValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(tickerTransactionValidationRule);
        ruleList.add(transactionExistsOnUpdateValidationRule);
        victim = new TransactionUpdateValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenValidate_thenDelegatesToEachRule() {
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        ruleList.forEach(rule -> doNothing().when(rule).validate(requestDtoMock));

        victim.validate(requestDtoMock);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validate(requestDtoMock));
    }

}