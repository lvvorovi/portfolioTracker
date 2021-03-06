package portfolioTracker.dividend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import portfolioTracker.dividend.repository.service.DividendRepositoryService;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.service.DividendCreateRequestValidationService;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;
import portfolioTracker.dividend.validation.service.DividendUpdateRequestValidationService;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.DividendTestUtil.*;

@ExtendWith(MockitoExtension.class)
class DividendServiceImplTest {

    @Mock
    DividendCreateRequestValidationService createRequestValidationService;
    @Mock
    DividendUpdateRequestValidationService updateRequestValidationService;
    @Mock
    PortfolioRelationMappingService mappingService;
    @Mock
    DividendRepositoryService dataManagementService;
    @InjectMocks
    DividendServiceImpl victim;

    @Test
    void save_whenDto_thenDelegateToRepo_ReturnDto_andNoException() {
        DividendEntity entityMock = mock(DividendEntity.class);
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(entity);
        DividendDtoResponse expected = newDividendDtoResponse(entity);
        doNothing().when(createRequestValidationService).validate(requestDto);
        when(mappingService.createToEntity(requestDto)).thenReturn(entityMock);
        doNothing().when(entityMock).setId(any());
        when(dataManagementService.save(entityMock)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(createRequestValidationService, times(1)).validate(requestDto);
        verify(mappingService, times(1)).createToEntity(requestDto);
        verify(entityMock, times(1)).setId(any());
        verify(dataManagementService, times(1)).save(entityMock);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(createRequestValidationService, mappingService, dataManagementService, entityMock);
        verifyNoInteractions(updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.save(requestDto));
    }

    @Test
    void saveAll_whenDtoList_thenDelegateToRepo_andReturnDtoList_andNoException() {
        List<DividendEntity> mockedEntityList = newDividendEntityMockList();
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoCreateRequest> requestDtoList = newDividendDtoCreateList(entityList);
        List<DividendDtoResponse> expectedDtoList = newDividendDtoResponseList(entityList);
        requestDtoList.forEach(dto -> doNothing().when(createRequestValidationService).validate(dto));
        mockedEntityList.forEach(mock -> doNothing().when(mock).setId(any()));

        for (int i = 0; i < requestDtoList.size(); i++) {
            when(mappingService.createToEntity(requestDtoList.get(i)))
                    .thenReturn(mockedEntityList.get(i));
        }

        for (int i = 0; i < entityList.size(); i++) {
            when(dataManagementService.save(mockedEntityList.get(i)))
                    .thenReturn(entityList.get(i));
        }

        for (int i = 0; i < expectedDtoList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(expectedDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.saveAll(requestDtoList);

        assertEquals(expectedDtoList, resultDtoList);
        mockedEntityList.forEach(mock -> verify(mock, times(1)).setId(any()));
        mockedEntityList.forEach(mock -> verify(dataManagementService, times(1)).save(mock));
        entityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));

        requestDtoList.forEach(dto -> {
            verify(createRequestValidationService, times(1)).validate(dto);
            verify(mappingService, times(1)).createToEntity(dto);
        });

        verifyNoMoreInteractions(createRequestValidationService, mappingService, dataManagementService);
        mockedEntityList.forEach(Mockito::verifyNoMoreInteractions);
        verifyNoInteractions(updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.saveAll(requestDtoList));
    }

    @Test
    void findById_whenExist_thenReturn_andNoExcetion() {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse expected = newDividendDtoResponse(entity);
        when(dataManagementService.findById(id)).thenReturn(Optional.of(entity));
        when(mappingService.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.findById(id);

        assertEquals(expected, result);
        verify(dataManagementService, times(1)).findById(id);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(dataManagementService, mappingService);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.findById(id));
    }

    @Test
    void findById_whenDoesNotExist_thenThrowException() {
        when(dataManagementService.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findById(id))
                .isInstanceOf(DividendNotFoundDividendException.class)
                .hasMessage(DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(dataManagementService, times(1)).findById(id);
        verifyNoMoreInteractions(dataManagementService);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService, mappingService);
    }

    @Test
    void findAllByUsername_whenExist_thenReturnList_andNoException() {
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList(entityList);
        when(dataManagementService.findAllByUsername(username)).thenReturn(entityList);

        for (int i = 0; i < entityList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(responseDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.findAllByUsername(username);

        assertEquals(responseDtoList, resultDtoList);
        verify(dataManagementService, times(1)).findAllByUsername(username);
        entityList.forEach(entity -> verify(mappingService, times(1)).toDto(entity));
        verifyNoMoreInteractions(dataManagementService, mappingService);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(username));
    }

    @Test
    void findAllByUsername_whenNothingFound_thenReturnEmptyList_andNoException() {
        when(dataManagementService.findAllByUsername(username)).thenReturn(new ArrayList<>());

        List<DividendDtoResponse> result = victim.findAllByUsername(username);

        assertThat(result.size()).isEqualTo(0);
        verify(dataManagementService, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(dataManagementService);
        verifyNoInteractions(mappingService, createRequestValidationService, updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void findAllByPortfolioId_whenExist_thenReturnList_andNoException() {
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList(entityList);
        when(dataManagementService.findAllByPortfolioId(portfolioId)).thenReturn(entityList);

        for (int i = 0; i < entityList.size(); i++) {
            when(mappingService.toDto(entityList.get(i)))
                    .thenReturn(responseDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.findAllByPortfolioId(portfolioId);

        assertEquals(responseDtoList, resultDtoList);
        verify(dataManagementService, times(1)).findAllByPortfolioId(portfolioId);
        entityList.forEach(dto -> verify(mappingService, times(1)).toDto(dto));
        verifyNoMoreInteractions(dataManagementService, mappingService);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(portfolioId));
    }

    @Test
    void findALlByPortfolioId_whenNothingFound_ReturnsEmptyList_andNoException() {
        when(dataManagementService.findAllByPortfolioId(portfolioId)).thenReturn(new ArrayList<>());

        List<DividendDtoResponse> result = victim.findAllByPortfolioId(portfolioId);

        assertThat(result.size()).isEqualTo(0);
        verify(dataManagementService, times(1)).findAllByPortfolioId(portfolioId);
        verifyNoMoreInteractions(dataManagementService);
        verifyNoInteractions(mappingService, createRequestValidationService, updateRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(portfolioId));
    }

    @Test
    void update_whenDto_thenDelegatesToRepo_ReturnsDto_andNoException() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse expected = newDividendDtoResponse(entity);
        doNothing().when(updateRequestValidationService).validate(requestDto);
        when(mappingService.updateToEntity(requestDto)).thenReturn(mockedEntity);
        when(dataManagementService.save(mockedEntity)).thenReturn(entity);
        when(mappingService.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.update(requestDto);

        assertEquals(expected, result);
        verify(updateRequestValidationService, times(1)).validate(requestDto);
        verify(mappingService, times(1)).updateToEntity(requestDto);
        verify(dataManagementService, times(1)).save(mockedEntity);
        verify(mappingService, times(1)).toDto(entity);
        verifyNoMoreInteractions(updateRequestValidationService, mappingService, dataManagementService, mockedEntity);
        verifyNoInteractions(createRequestValidationService);
        assertThatNoException().isThrownBy(() -> victim.update(requestDto));
    }

    @Test
    void delete_whenId_thenDelegateToRepo_andNoException() {
        doNothing().when(dataManagementService).deleteById(id);

        assertThatNoException().isThrownBy(() -> victim.deleteById(id));

        verify(dataManagementService, times(1)).deleteById(id);
        verifyNoMoreInteractions(dataManagementService);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService , mappingService);
    }

    @Test
    void isOwner_whenAuthNameMatchesRepoUsername_thenReturnTrue_andNoException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        DividendEntity entityMock = mock(DividendEntity.class);
        when(dataManagementService.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username);

        assertTrue(victim.isOwner(id));

        verify(dataManagementService, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verifyNoMoreInteractions(entityMock, dataManagementService, securityContextMock, authenticationMock);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService , mappingService);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenAuthNameDoesNotMatchRepoUsername_thenReturnFalse_andNoException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContextMock);
        DividendEntity entityMock = mock(DividendEntity.class);
        when(dataManagementService.findById(id)).thenReturn(Optional.of(entityMock));
        when(entityMock.getUsername()).thenReturn(username.concat("a"));
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(username);

        assertFalse(victim.isOwner(id));

        verify(dataManagementService, times(1)).findById(id);
        verify(entityMock, times(1)).getUsername();
        verify(securityContextMock, times(1)).getAuthentication();
        verify(authenticationMock, times(1)).getName();
        verifyNoMoreInteractions(entityMock, dataManagementService, securityContextMock, authenticationMock);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService , mappingService);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @Test
    void isOwner_whenNoResourceFound_thenThrowException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(dataManagementService.findById(id)).thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username + "1");

        assertThatThrownBy(() -> victim.isOwner(id))
                .isInstanceOf(DividendNotFoundDividendException.class)
                .hasMessage(DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE + id);

        verify(dataManagementService, times(1)).findById(id);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(dataManagementService, securityContext, authentication);
        verifyNoInteractions(createRequestValidationService, updateRequestValidationService , mappingService);
    }

}