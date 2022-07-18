package portfolioTracker.dividend.validation.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.exception.TickerNotSupportedDividendException;
import portfolioTracker.dto.ticker.service.TickerService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickerDividendValidationRuleTest {

    @Mock
    TickerService service;
    @InjectMocks
    TickerDividendValidationRule victim;

    @Test
    void validate_whenCreateRequestSupported_thenNoException() {
        DividendDtoCreateRequest createRequest = mock(DividendDtoCreateRequest.class);
        when(service.isTickerSupported(any())).thenReturn(true);

        assertDoesNotThrow(() -> victim.validate(createRequest));

        verify(createRequest, times(1)).getTicker();
        verify(service, times(1)).isTickerSupported(createRequest.getTicker());
    }

    @Test
    void validate_whenCreateRequestNotSupported_thenThrowException() {
        DividendDtoCreateRequest createRequest = mock(DividendDtoCreateRequest.class);
        when(service.isTickerSupported(any())).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(createRequest))
                .isInstanceOf(TickerNotSupportedDividendException.class)
                .hasMessage("Not supported ticker: " + createRequest.getTicker());

        verify(createRequest, times(2)).getTicker();
        verify(service, times(1)).isTickerSupported(createRequest.getTicker());
    }

    @Test
    void validate_whenUpdateRequestSupported_thenNoException() {
        DividendDtoUpdateRequest updateRequest = mock(DividendDtoUpdateRequest.class);
        when(service.isTickerSupported(any())).thenReturn(true);

        assertDoesNotThrow(() -> victim.validate(updateRequest));

        verify(updateRequest, times(1)).getTicker();
        verify(service, times(1)).isTickerSupported(updateRequest.getTicker());
    }

    @Test
    void validate_whenUpdateRequestNotSupported_thenThrowException() {
        DividendDtoUpdateRequest updateRequest = mock(DividendDtoUpdateRequest.class);
        when(service.isTickerSupported(any())).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(updateRequest))
                .isInstanceOf(TickerNotSupportedDividendException.class)
                .hasMessage("Not supported ticker: " + updateRequest.getTicker());

        verify(updateRequest, times(2)).getTicker();
        verify(service, times(1)).isTickerSupported(updateRequest.getTicker());
    }
}