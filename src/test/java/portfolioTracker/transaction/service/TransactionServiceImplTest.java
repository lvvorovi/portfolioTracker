package portfolioTracker.transaction.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;
import portfolioTracker.transaction.validation.service.TransactionCreateValidationService;
import portfolioTracker.transaction.validation.service.TransactionUpdateValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.TransactionTestUtil.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionUpdateValidationService updateValidationService;
    @Mock
    TransactionCreateValidationService createValidationService;
    @Mock
    TransactionRepository repository;
    @Mock
    PortfolioRelationMappingService mappingService;
    @InjectMocks
    TransactionServiceImpl victim;

    @Test
    void save_whenDto_thenDto_andNoException() {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoCreateRequest requestDto = newTransactionDtoCreateRequest(entity);
        TransactionDtoResponse expected = newTransactionDtoResponse(entity);
        TransactionEntity entityMock = mock(TransactionEntity.class);
        doNothing().when(createValidationService).validate(requestDto);
        when(mappingService.createToEntity(requestDto)).thenReturn(entityMock);
        doNothing().when(entityMock).setId(any());
        when(repository.save(entityMock)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        TransactionDtoResponse result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(createValidationService, times(1)).validate(requestDto);
        verify(mappingService, times(1)).createToEntity(requestDto);
        verify(entityMock, times(1)).setId(any());
        verify(repository, times(1)).save(entityMock);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(createValidationService, mappingService, repository, entityMock);
        verifyNoInteractions(updateValidationService);
    }

    @Test
    void saveAll_wheDtoLIst_thenDtoList_andNoException() {
        List<TransactionEntity> entityList = newTransactionEntityList();
        List<TransactionDtoCreateRequest> requestDtoList = newTransactionDtoCreateRequestList(entityList);
        List<TransactionDtoResponse> expected = newTransactionDtoResponseList(entityList);

        List<TransactionEntity> entityMockList = List.of(
                mock(TransactionEntity.class),
                mock(TransactionEntity.class),
                mock(TransactionEntity.class));

        requestDtoList.forEach(dto -> doNothing().when(createValidationService).validate(dto));
        entityMockList.forEach(mock -> doNothing().when(mock).setId(any()));

        for (int i = 0; i < requestDtoList.size(); i++) {
            when(mappingService.createToEntity(requestDtoList.get(i)))
                    .thenReturn(entityMockList.get(i));
        }

        for (int i = 0; i < entityMockList.size(); i++) {
            when(repository.save(entityMockList.get(i)))
                    .thenReturn(entityList.get(i));
        }

        for (int i = 0; i < entityList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(expected.get(i));
        }

        var result = victim.saveAll(requestDtoList);

        assertEquals(expected, result);
        requestDtoList.forEach(dto -> verify(createValidationService, times(1)).validate(dto));
        entityMockList.forEach(mock -> verify(mock, times(1)).setId(any()));
        requestDtoList.forEach(dto -> verify(mappingService, times(1)).createToEntity(dto));
        entityMockList.forEach(mock -> verify(repository, times(1)).save(mock));
        entityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));
        verifyNoMoreInteractions(createValidationService, mappingService, repository);
        entityMockList.forEach(Mockito::verifyNoMoreInteractions);
        verifyNoInteractions(updateValidationService);
    }

    @Test
    void findById_whenFound_thenReturnDto_NoException() {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoResponse expected = newTransactionDtoResponse(entity);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mappingService.toDto(entity)).thenReturn(expected);

        var result = victim.findById(id);

        assertEquals(expected, result);
        verify(repository, times(1)).findById(id);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findById(id));
    }

    @Test
    void findById_whenNotFound_thenTransactionNotFoundTransactionException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findById(id))
                .isInstanceOf(TransactionNotFoundTransactionException.class)
                .hasMessage(TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, createValidationService, updateValidationService);
    }

    @Test
    void findAllByUsername_whenFound_thenReturnDtoList_NoException() {
        List<TransactionEntity> entityList = newTransactionEntityList();
        List<TransactionDtoResponse> expectedDtoList = newTransactionDtoResponseList(entityList);
        when(repository.findAllByUsername(username)).thenReturn(entityList);

        for (int i = 0; i < entityList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(expectedDtoList.get(i));
        }

        var resultDtoList = victim.findAllByUsername(username);

        assertEquals(expectedDtoList, resultDtoList);
        verify(repository, times(1)).findAllByUsername(username);
        entityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void findAllByUsername_whenNotFound_thenReturnEmptyList_andNoException() {
        when(repository.findAllByUsername(username)).thenReturn(new ArrayList<>());

        var result = victim.findAllByUsername(username);

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }


    @Test
    void findAllByPortfolioId_whenFound_thenReturnDtoList_NoException() {
        List<TransactionEntity> entityList = newTransactionEntityList();
        List<TransactionDtoResponse> expectedDtoList = newTransactionDtoResponseList(entityList);
        when(repository.findAllByPortfolioId(id)).thenReturn(entityList);

        for (int i = 0; i < entityList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(expectedDtoList.get(i));
        }

        var resultDtoList = victim.findAllByPortfolioId(id);

        assertEquals(expectedDtoList, resultDtoList);
        verify(repository, times(1)).findAllByPortfolioId(id);
        entityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));
        verifyNoMoreInteractions(repository, mappingService);
        verifyNoInteractions(createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(id));
    }

    @Test
    void findAllByPortfolioId_whenNotFound_thenReturnEmptyList_andNoException() {
        when(repository.findAllByPortfolioId(id)).thenReturn(new ArrayList<>());

        var result = victim.findAllByPortfolioId(id);

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllByPortfolioId(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(id));
    }

    @Test
    void findAllUniqueTickers_whenFound_thenReturnList_noException() {
        List<String> expected = List.of("a", "b", "c");
        when(repository.findAllUniqueTickers()).thenReturn(expected);

        var result = victim.findAllUniqueTickers();

        assertEquals(expected, result);
        verify(repository, times(1)).findAllUniqueTickers();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, createValidationService, updateValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllUniqueTickers());
    }

    @Test
    void findAllUniqueTickers_whenNotFound_returnEmptyList_noException() {
        when(repository.findAllUniqueTickers()).thenReturn(new ArrayList<>());

        var result = victim.findAllUniqueTickers();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAllUniqueTickers();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mappingService, createValidationService, updateValidationService);
    }

    @Test
    void update_whenDto_thenDto_andNoException() {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoUpdateRequest requestDto = newTransactionDtoUpdateRequest(entity);
        TransactionDtoResponse expected = newTransactionDtoResponse(entity);
        doNothing().when(updateValidationService).validate(requestDto);
        when(mappingService.updateToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        TransactionDtoResponse result = victim.update(requestDto);

        assertEquals(expected, result);
        verify(updateValidationService, times(1)).validate(requestDto);
        verify(mappingService, times(1)).updateToEntity(requestDto);
        verify(repository, times(1)).save(entity);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(updateValidationService, mappingService, repository);
        verifyNoInteractions(createValidationService);
    }

    @Test
    void deleteById_delegatesToRepository_noException() {
        doNothing().when(repository).deleteById(id);

        assertThatNoException().isThrownBy(() -> victim.deleteById(id));

        verify(repository, times(1)).deleteById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(createValidationService, updateValidationService, mappingService);
    }

    @Test
    void isOwner_whenResourceFound_andBelongsToPrincipal_thenTrue_noException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        TransactionEntity entityMock = mock(TransactionEntity.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username);
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertTrue(result);
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(securityContextMock, authenticationMock, repository, entityMock);
        verifyNoInteractions(createValidationService, updateValidationService, mappingService);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenResourceFound_andDoesNoBelongToPrincipal_thenFalse_noException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        TransactionEntity entityMock = mock(TransactionEntity.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username.concat("a"));
        when(repository.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertFalse(result);
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verifyNoMoreInteractions(securityContextMock, authenticationMock, repository, entityMock);
        verifyNoInteractions(mappingService, updateValidationService, createValidationService);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void osOwner_whenResourceNotFound_thenException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username.concat("a"));
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.isOwner(id))
                .isInstanceOf(TransactionNotFoundTransactionException.class)
                .hasMessage(TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(securityContextMock, authenticationMock, repository);
        verifyNoInteractions(mappingService, updateValidationService, createValidationService);
    }

}