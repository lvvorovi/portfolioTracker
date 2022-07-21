package portfolioTracker.portfolio.validation.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dto.currency.service.CurrencyService;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.exception.CurrencyNotSupportedPortfolioException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE;

@ExtendWith(MockitoExtension.class)
class PortfolioCurrencyValidationRuleTest {

    @Mock
    CurrencyService currencyService;
    @InjectMocks
    PortfolioCurrencyValidationRule victim;

    @Test
    void validate_whenCreateRequest_andSupported_thenNoException() {
        PortfolioDtoCreateRequest requestDtoMock = mock(PortfolioDtoCreateRequest.class);
        when(currencyService.isCurrencySupported(requestDtoMock.getCurrency())).thenReturn(true);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(currencyService, times(1)).isCurrencySupported(requestDtoMock.getCurrency());
        verify(requestDtoMock, times(3)).getCurrency();
        verifyNoMoreInteractions(currencyService, requestDtoMock);
    }

    @Test
    void validate_whenCreateRequest_andNotSupported_thenThrowCurrencyNotSupportedPortfolioException() {
        PortfolioDtoCreateRequest requestDtoMock = mock(PortfolioDtoCreateRequest.class);
        when(currencyService.isCurrencySupported(requestDtoMock.getCurrency())).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(CurrencyNotSupportedPortfolioException.class)
                .hasMessage(CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE + requestDtoMock.getCurrency());

        verify(currencyService, times(1)).isCurrencySupported(requestDtoMock.getCurrency());
        verify(requestDtoMock, times(4)).getCurrency();
        verifyNoMoreInteractions(requestDtoMock, currencyService);
    }

    @Test
    void validate_whenUpdateRequest_andSupported_thenNoException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(currencyService.isCurrencySupported(requestDtoMock.getCurrency())).thenReturn(true);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(currencyService, times(1)).isCurrencySupported(requestDtoMock.getCurrency());
        verify(requestDtoMock, times(3)).getCurrency();
        verifyNoMoreInteractions(currencyService, requestDtoMock);
    }

    @Test
    void validate_whenUpdateRequest_andNotSupported_thenThrowCurrencyNotSupportedPortfolioException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(currencyService.isCurrencySupported(requestDtoMock.getCurrency())).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(CurrencyNotSupportedPortfolioException.class)
                .hasMessage(CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE + requestDtoMock.getCurrency());

        verify(currencyService, times(1)).isCurrencySupported(requestDtoMock.getCurrency());
        verify(requestDtoMock, times(4)).getCurrency();
        verifyNoMoreInteractions(requestDtoMock, currencyService);
    }

}