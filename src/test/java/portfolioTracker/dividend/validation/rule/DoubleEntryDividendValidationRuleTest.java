package portfolioTracker.dividend.validation.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.exception.DividendAlreadyExists;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
class DoubleEntryDividendValidationRuleTest {

    @Mock
    DividendRepository repository;
    @InjectMocks
    DoubleEntryDividendValidationRule victim;

    @Test
    void validate_whenCreateRequestDoesNotExist_thenNoException() {
        DividendDtoCreateRequest request = mock(DividendDtoCreateRequest.class);
        when(repository
                .existsByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId()))
                .thenReturn(false);

        assertDoesNotThrow(() -> victim.validate(request));

        verify(request, times(2)).getTicker();
        verify(request, times(2)).getExDate();
        verify(request, times(2)).getPortfolioId();
        verify(repository, times(1))
                .existsByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId());
    }

    @Test
    void validate_whenCreateRequestAlreadyExists_thenThrowException() {
        DividendDtoCreateRequest request = mock(DividendDtoCreateRequest.class);
        when(repository
                .existsByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId()))
                .thenReturn(true);

        assertThatThrownBy(() -> victim.validate(request))
                .isInstanceOf(DividendAlreadyExists.class)
                .hasMessage("Following Dividend event already registered in requested portfolio" + request);

        verify(request, times(2)).getTicker();
        verify(request, times(2)).getExDate();
        verify(request, times(2)).getPortfolioId();
        verify(repository, times(1))
                .existsByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId());
    }

    @Test
    void validate_whenUpdateRequestDoesNotExist_thenNoException() {
        DividendDtoUpdateRequest request = mock(DividendDtoUpdateRequest.class);
        when(repository
                .findByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId()))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> victim.validate(request));

        verify(request, times(2)).getTicker();
        verify(request, times(2)).getExDate();
        verify(request, times(2)).getPortfolioId();
        verify(repository, times(1))
                .findByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId());
    }

    @Test
    void validate_whenUpdateRequestAlreadyExists_thenThrowException() {
        String id = "";
        DividendDtoUpdateRequest request = mock(DividendDtoUpdateRequest.class);
        DividendEntity mockedEntity = mock(DividendEntity.class);
        when(mockedEntity.getId()).thenReturn(id);
        when(repository
                .findByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId()))
                .thenReturn(Optional.of(mockedEntity));

        assertThatThrownBy(() -> victim.validate(request))
                .isInstanceOf(DividendAlreadyExists.class)
                .hasMessage("Following Dividend event already registered in requested portfolio" + request);

        verify(request, times(1)).getId();
        verify(request, times(2)).getTicker();
        verify(request, times(2)).getExDate();
        verify(request, times(2)).getPortfolioId();
        verify(mockedEntity, times(1)).getId();
        verify(repository, times(1))
                .findByTickerAndExDateAndPortfolioId(
                        request.getTicker(), request.getExDate(), request.getPortfolioId());
    }

}