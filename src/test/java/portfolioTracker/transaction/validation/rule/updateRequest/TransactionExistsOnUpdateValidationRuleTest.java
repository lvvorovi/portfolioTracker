package portfolioTracker.transaction.validation.rule.updateRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;
import portfolioTracker.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.TestUtil.id;

@ExtendWith(MockitoExtension.class)
class TransactionExistsOnUpdateValidationRuleTest {

    @Mock
    TransactionRepository repository;
    @InjectMocks
    TransactionExistsOnUpdateValidationRule victim;

    @Test
    void validate_whenExists_thenNoException() {
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        when(requestDtoMock.getId()).thenReturn(id);
        when(repository.existsById(id)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(requestDtoMock, times(1)).getId();
        verify(repository, times(1)).existsById(id);
        verifyNoMoreInteractions(requestDtoMock, repository);
    }

    @Test
    void validate_whenDoesNotExist_thenTransactionDoesNotExistTransactionException() {
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        when(requestDtoMock.getId()).thenReturn(id);
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(TransactionNotFoundTransactionException.class)
                .hasMessage(TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE + requestDtoMock);

        verify(requestDtoMock, times(1)).getId();
        verify(repository, times(1)).existsById(id);
        verifyNoMoreInteractions(repository, requestDtoMock);
    }

}