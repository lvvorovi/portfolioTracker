package portfolioTracker.portfolio.validation.rule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNameAlreadyExistsPortfolioException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE;
import static portfolioTracker.util.TestUtil.username;

@ExtendWith(MockitoExtension.class)
class PortfolioNameValidationRuleTest {

    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;
    @Mock
    PortfolioRepository repository;
    @InjectMocks
    PortfolioNameValidationRule victim;

    @BeforeEach
    void setUpBefore() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void validate_whenCreateRequest_andDoesNotExist_thenNoException() {
        PortfolioDtoCreateRequest requestDtoMock = mock(PortfolioDtoCreateRequest.class);
        when(requestDtoMock.getName()).thenReturn(username);
        when(repository.findByNameAndUsername(username, username))
                .thenReturn(Optional.empty());

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(repository, times(1)).findByNameAndUsername(username, username);
        verify(requestDtoMock, times(1)).getName();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(repository, securityContext, authentication, requestDtoMock);
    }

    @Test
    void validate_whenCreateRequest_andExists_thenThrowPortfolioNameAlreadyExistsPortfolioException() {
        PortfolioDtoCreateRequest requestDtoMock = mock(PortfolioDtoCreateRequest.class);
        when(requestDtoMock.getName()).thenReturn(username);
        when(repository.findByNameAndUsername(username, username)).thenReturn(Optional.of(new PortfolioEntity()));

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(PortfolioNameAlreadyExistsPortfolioException.class)
                .hasMessage(PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE + requestDtoMock.getName());

        verify(repository, times(1)).findByNameAndUsername(username, username);
        verify(requestDtoMock, times(2)).getName();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(repository, requestDtoMock, securityContext, authentication);
    }

    @Test
    void validate_whenUpdateRequest_andDoesNotExist_thenNoException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(requestDtoMock.getName()).thenReturn(username);
        when(repository.findByNameAndUsername(username, username))
                .thenReturn(Optional.empty());

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(repository, times(1)).findByNameAndUsername(username, username);
        verify(requestDtoMock, times(1)).getName();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(repository, securityContext, authentication, requestDtoMock);
    }

    @Test
    void validate_whenUpdateRequest_andExists_thenThrowPortfolioNameAlreadyExistsPortfolioException() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        when(requestDtoMock.getName()).thenReturn(username);
        when(repository.findByNameAndUsername(username, username)).thenReturn(Optional.of(new PortfolioEntity()));

        assertThatThrownBy(() -> victim.validate(requestDtoMock))
                .isInstanceOf(PortfolioNameAlreadyExistsPortfolioException.class)
                .hasMessage(PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE + requestDtoMock.getName());

        verify(repository, times(1)).findByNameAndUsername(username, username);
        verify(requestDtoMock, times(2)).getName();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(repository, requestDtoMock, securityContext, authentication);
    }

}