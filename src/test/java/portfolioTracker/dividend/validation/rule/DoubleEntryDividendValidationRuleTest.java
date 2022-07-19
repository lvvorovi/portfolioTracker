package portfolioTracker.dividend.validation.rule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.exception.DividendExistsDividendException;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.*;
import static portfolioTracker.util.DividendTestUtil.*;


@ExtendWith(MockitoExtension.class)
public class DoubleEntryDividendValidationRuleTest {

    @Mock
    private DividendRepository repository;

    @InjectMocks
    private DoubleEntryDividendValidationRule victim;

    @Test
    void validate_whenCreateRequestDoesNotExist_thenNoException() {
        DividendDtoCreateRequest mockedRequestDto = mock(DividendDtoCreateRequest.class);
        when(repository
                .existsByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId()))
                .thenReturn(false);

        assertDoesNotThrow(() -> victim.validate(mockedRequestDto));

        verify(repository, times(1))
                .existsByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId());
        verify(mockedRequestDto, times(3)).getTicker();
        verify(mockedRequestDto, times(3)).getExDate();
        verify(mockedRequestDto, times(3)).getPortfolioId();
        verifyNoMoreInteractions(mockedRequestDto, repository);
    }

    @Test
    void validate_whenCreateRequestAlreadyExists_thenThrowException() {
        DividendDtoCreateRequest mockedRequestDto = mock(DividendDtoCreateRequest.class);
        when(repository
                .existsByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId()))
                .thenReturn(true);

        assertThatThrownBy(() -> victim.validate(mockedRequestDto))
                .isInstanceOf(DividendExistsDividendException.class)
                .hasMessage(DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE + mockedRequestDto);

        verify(repository, times(1))
                .existsByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId());
        verify(mockedRequestDto, times(3)).getTicker();
        verify(mockedRequestDto, times(3)).getExDate();
        verify(mockedRequestDto, times(3)).getPortfolioId();
        verifyNoMoreInteractions(mockedRequestDto, repository);
    }

    @Test
    void validate_whenUpdateRequestDoesNotExistById_thenNoException() {
        DividendDtoUpdateRequest mockedRequestDto = mock(DividendDtoUpdateRequest.class);
        when(repository.existsById(mockedRequestDto.getId())).thenReturn(false);

        assertThatThrownBy(() -> victim.validate(mockedRequestDto))
                .isInstanceOf(DividendNotFoundDividendException.class)
                        .hasMessage(DIVIDEND_NOT_FOUND_EXCEPTION_MESSAGE + mockedRequestDto);

        verify(repository, times(1)).existsById(mockedRequestDto.getId());
        verify(mockedRequestDto, times(3)).getId();
        verifyNoMoreInteractions(repository, mockedRequestDto);
    }

    @Test
    void validate_whenUpdateRequestAlreadyExistsInPortfolio_thenThrowException() {
        DividendDtoUpdateRequest mockedRequestDto = mock(DividendDtoUpdateRequest.class);
        DividendEntity mockedEntity = mock(DividendEntity.class);
        when(mockedEntity.getId()).thenReturn(id);
        when(mockedRequestDto.getId()).thenReturn(id.concat("a"));
        when(repository.existsById(mockedRequestDto.getId())).thenReturn(true);
        when(repository
                .findByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId()))
                .thenReturn(Optional.of(mockedEntity));

        assertThatThrownBy(() -> victim.validate(mockedRequestDto))
                .isInstanceOf(DividendExistsDividendException.class)
                .hasMessage(DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE + mockedRequestDto);

        verify(repository, times(1))
                .findByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId());
        verify(repository, times(1)).existsById(mockedRequestDto.getId());
        verify(mockedRequestDto, times(4)).getId();
        verify(mockedRequestDto, times(3)).getTicker();
        verify(mockedRequestDto, times(3)).getExDate();
        verify(mockedRequestDto, times(3)).getPortfolioId();
        verify(mockedEntity, times(1)).getId();
        verifyNoMoreInteractions(mockedRequestDto, repository);
    }

    @Test
    void validate_whenUpdateRequestDoesNotExistInPortfolio_thenNoException() {
        DividendDtoUpdateRequest mockedRequestDto = mock(DividendDtoUpdateRequest.class);
        DividendEntity mockedEntity = mock(DividendEntity.class);
        when(mockedEntity.getId()).thenReturn(id);
        when(mockedRequestDto.getId()).thenReturn(id);
        when(repository.existsById(mockedRequestDto.getId())).thenReturn(true);
        when(repository
                .findByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId()))
                .thenReturn(Optional.of(mockedEntity));

        assertThatNoException().isThrownBy(() -> victim.validate(mockedRequestDto));

        verify(repository, times(1))
                .findByTickerAndExDateAndPortfolioId(
                        mockedRequestDto.getTicker(), mockedRequestDto.getExDate(), mockedRequestDto.getPortfolioId());
        verify(repository, times(1)).existsById(mockedRequestDto.getId());
        verify(mockedRequestDto, times(4)).getId();
        verify(mockedRequestDto, times(3)).getTicker();
        verify(mockedRequestDto, times(3)).getExDate();
        verify(mockedRequestDto, times(3)).getPortfolioId();
        verify(mockedEntity, times(1)).getId();
        verifyNoMoreInteractions(mockedRequestDto, repository, mockedEntity);
    }



}