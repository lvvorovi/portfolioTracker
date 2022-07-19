package portfolioTracker.portfolio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.PortfolioMapper;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.PortfolioValidationService;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundPortfolioException;
import portfolioTracker.transaction.validation.exception.PortfolioNotFoundTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.PortfolioTestUtil.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Mock
    PortfolioValidationService validationService;
    @Mock
    PortfolioRepository repository;
    @Mock
    PortfolioMapper mapper;
    @InjectMocks
    PortfolioServiceImpl victim;

    @Test
    void findById_whenFound_thenReturnDto_noExceptionThrown() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.findById(id);

        assertEquals(expected, result);
        assertNull(result.getDividendList());
        assertNull(result.getTransactionList());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(repository, mapper);
        assertThatNoException().isThrownBy(() -> victim.findById(id));
        verifyNoInteractions(validationService);
    }

    @Test
    void whenEntityNotFound_thenThrowPortfolioNotFoundTransactionException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findById(id))
                .isInstanceOf(PortfolioNotFoundTransactionException.class)
                .hasMessage(PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);
        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, validationService);
    }

    @Test
    void findAllByUsername_whenFound_thenReturnDtoList_noExceptionThrown() {
        List<PortfolioEntity> portfolioEntityList = newPortfolioEntityList();
        List<PortfolioDtoResponse> expected = newPortfolioDtoResponseDtoList(portfolioEntityList);
        when(repository.findAllByUsername(username)).thenReturn(portfolioEntityList);
        for (int i = 0; i < portfolioEntityList.size(); i++) {
            when(mapper.toDto(portfolioEntityList.get(i)))
                    .thenReturn(expected.get(i));
        }

        List<PortfolioDtoResponse> result = victim.findAllByUsername(username);

        assertEquals(expected, result);
        verify(repository, times(1)).findAllByUsername(username);
        portfolioEntityList.forEach(entity -> verify(mapper, times(1)).toDto(entity));
        verifyNoMoreInteractions(repository, mapper);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
        verifyNoInteractions(validationService);
    }

    @Test
    void findAllByUsername_whenNothingFound_thenReturnEmptyList_noExceptionThrown() {
        when(repository.findAllByUsername(username)).thenReturn(new ArrayList<>());

        List<PortfolioDtoResponse> result = victim.findAllByUsername(username);

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, validationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void save_whenRequestDto_thenResponseDto_andNoException() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(entity);
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        doNothing().when(validationService).validate(requestDto);
        when(mapper.createToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).createToEntity(requestDto);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, mapper, repository);
        assertThatNoException().isThrownBy(() -> victim.save(requestDto));
    }

    @Test
    void update_whenRequestDto_thenResponseDto_andNoException() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(entity);
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        doNothing().when(validationService).validate(requestDto);
        when(mapper.updateToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.update(requestDto);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).updateToEntity(requestDto);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, repository, mapper);
        assertThatNoException().isThrownBy(() -> victim.update(requestDto));
    }

    @Test
        //No need to validate for existence. Will be verified through Security, isOwner().
    void deleteById_whenID_theDelegateToRepository_andNoException() {
        doNothing().when(repository).deleteById(id);

        assertThatNoException().isThrownBy(() -> victim.deleteById(id));

        verify(repository, times(1)).deleteById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper);
    }

    @Test
    void isOwner_whenUsernameMatches_thenReturnTrue() {
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        when(securityContextMock.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertTrue(result);
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(repository, entityMock, authentication, securityContextMock);
        verifyNoInteractions(validationService, mapper);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenUsernameDoesNotMatch_thenReturnFalse() {
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        when(securityContextMock.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username.concat("a"));
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertFalse(result);
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(repository, entityMock, authentication, securityContextMock);
        verifyNoInteractions(validationService, mapper);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenResourceNotFound_thenException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.isOwner(id))
                .isInstanceOf(PortfolioNotFoundPortfolioException.class)
                .hasMessage(PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository, authenticationMock, securityContextMock);
        verifyNoInteractions(validationService, mapper);
    }

    @Test
    void findAllPortfolioCurrencies_whenFound_thenReturnList_noException() {
        List<String> currencyList = List.of("a", "b", "c");
        when(repository.findAllPortfolioCurrencies()).thenReturn(currencyList);

        List<String> result = victim.findAllPortfolioCurrencies();

        assertEquals(currencyList, result);
        verify(repository, times(1)).findAllPortfolioCurrencies();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper);
        assertThatNoException().isThrownBy(() -> victim.findAllPortfolioCurrencies());
    }

    @Test
    void findAllPortfolioCurrencies_whenNothingFound_thenEmptyList_noException() {
        when(repository.findAllPortfolioCurrencies()).thenReturn(List.of());

        List<String> result = victim.findAllPortfolioCurrencies();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllPortfolioCurrencies();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper);
        assertThatNoException().isThrownBy(() -> victim.findAllPortfolioCurrencies());
    }

}