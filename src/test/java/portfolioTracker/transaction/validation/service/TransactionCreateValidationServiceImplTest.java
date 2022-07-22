package portfolioTracker.transaction.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.validation.rule.TickerTransactionValidationRule;
import portfolioTracker.transaction.validation.rule.createRequest.TransactionCreateValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionCreateValidationServiceImplTest {

    @Mock
    TickerTransactionValidationRule tickerTransactionValidationRule;

    List<TransactionCreateValidationRule> ruleList;
    TransactionCreateValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(tickerTransactionValidationRule);
        victim = new TransactionCreateValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenValidate_thenDelegatesToEachRule() {
        TransactionDtoCreateRequest requestDtoMock = mock(TransactionDtoCreateRequest.class);
        ruleList.forEach(rule -> doNothing().when(rule).validate(requestDtoMock));

        victim.validate(requestDtoMock);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validate(requestDtoMock));
    }
}