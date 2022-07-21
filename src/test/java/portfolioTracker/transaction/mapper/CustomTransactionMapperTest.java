package portfolioTracker.transaction.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static portfolioTracker.util.TransactionTestUtil.newTransactionDtoResponse;
import static portfolioTracker.util.TransactionTestUtil.newTransactionEntity;

@ExtendWith(MockitoExtension.class)
class CustomTransactionMapperTest {

    @InjectMocks
    CustomTransactionMapper victim;

    @Test
    void updateToEntity_whenUpdate_thenEntity() {
        TransactionEntity expected = newTransactionEntity();
        expected.setPortfolio(null);
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        when(requestDtoMock.getId()).thenReturn(expected.getId());
        when(requestDtoMock.getCommission()).thenReturn(expected.getCommission());
        when(requestDtoMock.getDate()).thenReturn(expected.getDate());
        when(requestDtoMock.getPrice()).thenReturn(expected.getPrice());
        when(requestDtoMock.getTicker()).thenReturn(expected.getTicker());
        when(requestDtoMock.getType()).thenReturn(expected.getType());
        when(requestDtoMock.getShares()).thenReturn(expected.getShares());
        when(requestDtoMock.getUsername()).thenReturn(expected.getUsername());

        TransactionEntity result = victim.updateToEntity(requestDtoMock);

        assertEquals(expected, result);
        verify(requestDtoMock, times(1)).getId();
        verify(requestDtoMock, times(1)).getCommission();
        verify(requestDtoMock, times(1)).getDate();
        verify(requestDtoMock, times(1)).getPrice();
        verify(requestDtoMock, times(1)).getTicker();
        verify(requestDtoMock, times(1)).getType();
        verify(requestDtoMock, times(1)).getShares();
        verify(requestDtoMock, times(1)).getUsername();
        verifyNoMoreInteractions(requestDtoMock);
    }

    @Test
    void createToEntity_whenCreate_thenEntity() {
        TransactionEntity expected = newTransactionEntity();
        expected.setPortfolio(null);
        expected.setId(null);
        TransactionDtoCreateRequest requestDtoMock = mock(TransactionDtoCreateRequest.class);
        when(requestDtoMock.getCommission()).thenReturn(expected.getCommission());
        when(requestDtoMock.getDate()).thenReturn(expected.getDate());
        when(requestDtoMock.getPrice()).thenReturn(expected.getPrice());
        when(requestDtoMock.getTicker()).thenReturn(expected.getTicker());
        when(requestDtoMock.getType()).thenReturn(expected.getType());
        when(requestDtoMock.getShares()).thenReturn(expected.getShares());
        when(requestDtoMock.getUsername()).thenReturn(expected.getUsername());

        TransactionEntity result = victim.createToEntity(requestDtoMock);

        assertEquals(expected, result);
        verify(requestDtoMock, times(1)).getCommission();
        verify(requestDtoMock, times(1)).getDate();
        verify(requestDtoMock, times(1)).getPrice();
        verify(requestDtoMock, times(1)).getTicker();
        verify(requestDtoMock, times(1)).getType();
        verify(requestDtoMock, times(1)).getShares();
        verify(requestDtoMock, times(1)).getUsername();
        verifyNoMoreInteractions(requestDtoMock);
    }

    @Test
    void toDto_whenEntity_thenDto() {
        TransactionEntity entity = newTransactionEntity();

        TransactionDtoResponse expected = newTransactionDtoResponse(entity);

        TransactionEntity entityMock = mock(TransactionEntity.class);

        when(entityMock.getId()).thenReturn(expected.getId());
        when(entityMock.getCommission()).thenReturn(expected.getCommission());
        when(entityMock.getDate()).thenReturn(expected.getDate());
        when(entityMock.getPrice()).thenReturn(expected.getPrice());
        when(entityMock.getTicker()).thenReturn(expected.getTicker());
        when(entityMock.getType()).thenReturn(expected.getType());
        when(entityMock.getShares()).thenReturn(expected.getShares());
        when(entityMock.getUsername()).thenReturn(expected.getUsername());
        when(entityMock.getPortfolio()).thenReturn(entity.getPortfolio());

        TransactionDtoResponse result = victim.toDto(entityMock);

        assertEquals(expected, result);
        verify(entityMock, times(1)).getId();
        verify(entityMock, times(1)).getCommission();
        verify(entityMock, times(1)).getDate();
        verify(entityMock, times(1)).getPrice();
        verify(entityMock, times(1)).getTicker();
        verify(entityMock, times(1)).getType();
        verify(entityMock, times(1)).getShares();
        verify(entityMock, times(1)).getUsername();
        verify(entityMock, times(1)).getPortfolio();
        verifyNoMoreInteractions(entityMock);
    }


}