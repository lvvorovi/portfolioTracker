package portfolioTracker.portfolio.validation.rule.updateRequestRule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundPortfolioException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.TestUtil.id;

@ExtendWith(MockitoExtension.class)
class PortfolioExistsValidationRuleTest {

    @Mock
    PortfolioRepository repository;
    @InjectMocks
    PortfolioExistsValidationRule victim;

    @Test
    void validate_whenExists_thenNotException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(requestDtoMock.getId()).thenReturn(id);
        when(repository.findById(id)).thenReturn(Optional.of(new PortfolioEntity()));

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(repository, times(1)).findById(id);
        verify(requestDtoMock, times(1)).getId();
        verifyNoMoreInteractions(repository, requestDtoMock);
    }

    @Test
    void validate_whenDoesNotExist_thenThrowPortfolioNotFoundPortfolioException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(requestDtoMock.getId()).thenReturn(id);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(PortfolioNotFoundPortfolioException.class)
                .hasMessage(PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDtoMock);

        verify(repository, times(1)).findById(id);
        verify(requestDtoMock, times(1)).getId();
        verifyNoMoreInteractions(repository, requestDtoMock);
    }

}