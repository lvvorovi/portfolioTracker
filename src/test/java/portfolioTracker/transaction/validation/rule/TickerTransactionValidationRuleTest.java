package portfolioTracker.transaction.validation.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dto.ticker.service.TickerService;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.validation.exception.TickerNotSupportedTransactionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE;
import static portfolioTracker.util.TestUtil.id;
import static portfolioTracker.util.TestUtil.ticker;

@ExtendWith(MockitoExtension.class)
class TickerTransactionValidationRuleTest {

    @Mock
    TickerService tickerService;
    @InjectMocks
    TickerTransactionValidationRule victim;

    @Test
    void validate_whenUpdateRequest_andIsSupported_thenNoException() {
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        when(requestDtoMock.getTicker()).thenReturn(ticker);
        when(tickerService.isTickerSupported(ticker)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(requestDtoMock, times(1)).getTicker();
        verify(tickerService, times(1)).isTickerSupported(ticker);
        verifyNoMoreInteractions(requestDtoMock, tickerService);
    }

    @Test
    void validate_whenUpdateRequest_andIsNotSupported_thenThrowTickerNotSupportedTransactionException() {
        TransactionDtoUpdateRequest requestDtoMock = mock(TransactionDtoUpdateRequest.class);
        when(requestDtoMock.getTicker()).thenReturn(ticker);
        when(tickerService.isTickerSupported(ticker)).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(TickerNotSupportedTransactionException.class)
                .hasMessage(TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE + ticker);

        verify(requestDtoMock, times(1)).getTicker();
        verify(tickerService, times(1)).isTickerSupported(ticker);
        verifyNoMoreInteractions(tickerService, requestDtoMock);
    }

    @Test
    void validate_whenCreateRequest_andIsSupported_thenNoException() {
        TransactionDtoCreateRequest requestDtoMock = mock(TransactionDtoCreateRequest.class);
        when(requestDtoMock.getTicker()).thenReturn(ticker);
        when(tickerService.isTickerSupported(ticker)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(requestDtoMock, times(1)).getTicker();
        verify(tickerService, times(1)).isTickerSupported(ticker);
        verifyNoMoreInteractions(requestDtoMock, tickerService);
    }

    @Test
    void validate_whenCreateRequest_andIsNotSupported_thenThrowTickerNotSupportedTransactionException() {
        TransactionDtoCreateRequest requestDtoMock = mock(TransactionDtoCreateRequest.class);
        when(requestDtoMock.getTicker()).thenReturn(ticker);
        when(tickerService.isTickerSupported(ticker)).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(TickerNotSupportedTransactionException.class)
                .hasMessage(TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE + ticker);

        verify(requestDtoMock, times(1)).getTicker();
        verify(tickerService, times(1)).isTickerSupported(ticker);
        verifyNoMoreInteractions(tickerService, requestDtoMock);
    }




}