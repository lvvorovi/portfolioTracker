package portfolioTracker.portfolio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundPortfolioException;
import portfolioTracker.portfolio.validation.service.PortfolioCreateRequestValidationService;
import portfolioTracker.portfolio.validation.service.PortfolioUpdateRequestValidationService;
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
    PortfolioUpdateRequestValidationService validationServiceUpdateRequest;
    @Mock
    PortfolioCreateRequestValidationService validationServiceCreateRequest;
    @Mock
    PortfolioRepository repository;
    @Mock
    PortfolioRelationMappingService mappingService;
    @InjectMocks
    PortfolioServiceImpl victim;

    @Test
    void save_whenRequestDto_thenResponseDto_andNoException() {
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(entity);
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        doNothing().when(validationServiceCreateRequest).validate(requestDto);
        when(mappingService.createToEntity(requestDto)).thenReturn(entityMock);
        doNothing().when(entityMock).setId(any());
        when(repository.save(entityMock)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(validationServiceCreateRequest, times(1)).validate(requestDto);
        verify(mappingService, times(1)).createToEntity(requestDto);
        verify(repository, times(1)).save(entityMock);
        verify(mappingService, times(1)).toDto(entity);
        verify(entityMock, times(1)).setId(any());
        verifyNoMoreInteractions(validationServiceCreateRequest, mappingService, repository, entityMock);
        verifyNoInteractions(validationServiceUpdateRequest);
        assertThatNoException().isThrownBy(() -> victim.save(requestDto));
    }

    @Test
    void findById_whenFound_thenReturnDto_andNoException() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        expected.setTransactionList(null);
        expected.setDividendList(null);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mappingService.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.findById(id);

        assertEquals(expected, result);
        assertNull(result.getDividendList());
        assertNull(result.getTransactionList());
        verify(repository, times(1)).findById(id);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(validationServiceCreateRequest, validationServiceUpdateRequest);
        assertThatNoException().isThrownBy(() -> victim.findById(id));
    }

    @Test
    void findById_whenEntityNotFound_thenThrowPortfolioNotFoundPortfolioException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findById(id))
                .isInstanceOf(PortfolioNotFoundPortfolioException.class)
                .hasMessage(PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, validationServiceCreateRequest, validationServiceUpdateRequest);
    }

    @Test
    void findByIdIncludeEvents_whenFound_thenReturnList_andNoException() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        expected.setTransactionList(null);
        expected.setDividendList(null);
        when(repository.findByIdIncludeEvents(id)).thenReturn(Optional.of(entity));
        when(mappingService.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.findByIdIncludeEvents(id);

        assertEquals(expected, result);
        assertNull(result.getDividendList());
        assertNull(result.getTransactionList());
        verify(repository, times(1)).findByIdIncludeEvents(id);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest);
        assertThatNoException().isThrownBy(() -> victim.findByIdIncludeEvents(id));
    }

    @Test
    void findByIdIncludeEvents_whenNotFound_thenThrowPortfolioNotFoundPortfolioException() {
        when(repository.findByIdIncludeEvents(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findByIdIncludeEvents(id))
                .isInstanceOf(PortfolioNotFoundPortfolioException.class)
                .hasMessage(PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(repository, times(1)).findByIdIncludeEvents(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, validationServiceCreateRequest, validationServiceUpdateRequest);
    }

    @Test
    void findAllByUsername_whenFound_thenReturnList_andNoException() {
        List<PortfolioEntity> portfolioEntityList = newPortfolioEntityList();
        List<PortfolioDtoResponse> expectedDtoList = newPortfolioDtoResponseDtoList(portfolioEntityList);
        expectedDtoList.forEach(dto -> dto.setDividendList(null));
        expectedDtoList.forEach(dto -> dto.setTransactionList(null));
        when(repository.findAllByUsername(username)).thenReturn(portfolioEntityList);

        for (int i = 0; i < portfolioEntityList.size(); i++) {
            when(mappingService.toDto(portfolioEntityList.get(i)))
                    .thenReturn(expectedDtoList.get(i));
        }

        List<PortfolioDtoResponse> resultDtoList = victim.findAllByUsername(username);

        assertEquals(expectedDtoList, resultDtoList);
        resultDtoList.forEach(dto -> assertNull(dto.getTransactionList()));
        resultDtoList.forEach(dto -> assertNull(dto.getDividendList()));
        verify(repository, times(1)).findAllByUsername(username);
        portfolioEntityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void findAllByUsername_whenNotFound_thenReturnEmptyList_andNoException() {
        when(repository.findAllByUsername(username)).thenReturn(new ArrayList<>());

        List<PortfolioDtoResponse> result = victim.findAllByUsername(username);

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, validationServiceUpdateRequest, validationServiceCreateRequest);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void findAllByUsernameIncludeEvents_whenFound_thenReturnList_andNoException() {
        List<PortfolioEntity> portfolioEntityList = newPortfolioEntityList();
        List<PortfolioDtoResponse> expectedDtoList = newPortfolioDtoResponseDtoList(portfolioEntityList);
        when(repository.findAllByUsernameIncludeEvents(username)).thenReturn(portfolioEntityList);

        for (int i = 0; i < portfolioEntityList.size(); i++) {
            when(mappingService.toDto(portfolioEntityList.get(i)))
                    .thenReturn(expectedDtoList.get(i));
        }

        List<PortfolioDtoResponse> resultDtoList = victim.findAllByUsernameIncludeEvents(username);

        assertEquals(expectedDtoList, resultDtoList);
        resultDtoList.forEach(dto -> assertNotNull(dto.getTransactionList()));
        resultDtoList.forEach(dto -> assertNotNull(dto.getDividendList()));
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(validationServiceCreateRequest, validationServiceUpdateRequest);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsernameIncludeEvents(username));
    }

    @Test
    void findAllByUsernameIncludeEvents_whenNotFound_thenEmptyList_andNoException() {
        when(repository.findAllByUsernameIncludeEvents(username)).thenReturn(new ArrayList<>());

        List<PortfolioDtoResponse> resultDtoList = victim.findAllByUsernameIncludeEvents(username);

        assertThat(resultDtoList).isEmpty();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, validationServiceCreateRequest, validationServiceUpdateRequest);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsernameIncludeEvents(username));
    }

    @Test
    void findAllPortfolioCurrencies_whenFound_thenReturnList_noException() {
        List<String> currencyList = List.of("a", "b", "c");
        when(repository.findAllPortfolioCurrencies()).thenReturn(currencyList);

        List<String> result = victim.findAllPortfolioCurrencies();

        assertEquals(currencyList, result);
        verify(repository, times(1)).findAllPortfolioCurrencies();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest, mappingService);
        assertThatNoException().isThrownBy(() -> victim.findAllPortfolioCurrencies());
    }

    @Test
    void findAllPortfolioCurrencies_whenNothingFound_thenEmptyList_noException() {
        when(repository.findAllPortfolioCurrencies()).thenReturn(List.of());

        List<String> result = victim.findAllPortfolioCurrencies();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllPortfolioCurrencies();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest, mappingService);
        assertThatNoException().isThrownBy(() -> victim.findAllPortfolioCurrencies());
    }

    @Test
    void update_whenRequestDto_thenResponseDto_andNoException() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(entity);
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        doNothing().when(validationServiceUpdateRequest).validate(requestDto);
        when(mappingService.updateToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        PortfolioDtoResponse result = victim.update(requestDto);

        assertEquals(expected, result);
        verify(validationServiceUpdateRequest, times(1)).validate(requestDto);
        verify(mappingService, times(1)).updateToEntity(requestDto);
        verify(repository, times(1)).save(entity);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationServiceUpdateRequest, repository, mappingService);
        verifyNoInteractions(validationServiceCreateRequest);
        assertThatNoException().isThrownBy(() -> victim.update(requestDto));
    }

    @Test
    void deleteById_whenId_theDelegateToRepository_andNoException() {
        doNothing().when(repository).deleteById(id);

        assertThatNoException().isThrownBy(() -> victim.deleteById(id));

        verify(repository, times(1)).deleteById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest, mappingService);
    }

    @Test
    void isOwner_whenUsernameMatches_thenReturnTrue() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        when(securityContextMock.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        assertTrue(victim.isOwner(id));

        verify(securityContextMock, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(repository, entityMock, authentication, securityContextMock);
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest, mappingService);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenUsernameDoesNotMatch_thenReturnFalse() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        when(securityContextMock.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username.concat("a"));
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        assertFalse(victim.isOwner(id));

        verify(securityContextMock, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(repository, entityMock, authentication, securityContextMock);
        verifyNoInteractions(validationServiceCreateRequest, validationServiceUpdateRequest, mappingService);
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
        verifyNoInteractions(validationServiceUpdateRequest, validationServiceCreateRequest, mappingService);
    }

}