package portfolioTracker.dividend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.DividendValidationService;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;
import portfolioTracker.dividend.validation.exception.PortfolioNotFoundDividendException;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.repository.PortfolioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static portfolioTracker.portfolio.PortfolioTestUtil.newPortfolioEntitySkipEvents;
import static portfolioTracker.util.DividendTestUtil.*;

@ExtendWith(MockitoExtension.class)
@Import(SecurityContext.class)
class DividendServiceImplTest {

    @Mock
    DividendValidationService validationService;
    @Mock
    DividendMapper mapper;
    @Mock
    DividendRepository repository;
    @Mock
    PortfolioRepository portfolioRepository;
    @InjectMocks
    DividendServiceImpl victim;

    @Test
    void save_whenDto_thenDelegateToRepo_andReturnDto() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(entity);
        DividendDtoResponse expected = newDividendResponseDto(entity);
        PortfolioEntity portfolioEntity = newPortfolioEntitySkipEvents();
        doNothing().when(validationService).validate(requestDto);
        when(mapper.createToEntity(requestDto)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.of(portfolioEntity));
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.save(requestDto);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(mockedEntity, times(1)).setPortfolio(portfolioEntity);
        verify(mockedEntity, times(1)).setId(any());
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository, mockedEntity);
        assertThatNoException().isThrownBy(() -> victim.save(requestDto));
    }

    @Test
    void save_whenPortfolioIdDoesNotExist_thenThrowException() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(newDividendEntity());
        doNothing().when(validationService).validate(requestDto);
        when(mapper.createToEntity(requestDto)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.save(requestDto))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage("Portfolio not found for: " + requestDto);

        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(mapper, portfolioRepository, validationService);
        verifyNoInteractions(repository, mockedEntity);
    }

    @Test
    void saveAll_whenDtoList_thenDelegateToRepo_andReturnDtoList() {
        PortfolioEntity portfolioEntity = newPortfolioEntitySkipEvents();
        List<DividendEntity> mockedEntityList = newDividendEntityMockList();
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoCreateRequest> requestDtoList = newDividendDtoCreateList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();

        requestDtoList.forEach(dto -> doNothing().when(validationService).validate(dto));
        requestDtoList.forEach(dto ->
                when(portfolioRepository.findById(dto.getPortfolioId()))
                        .thenReturn(Optional.of(portfolioEntity)));

        for (int i = 0; i < requestDtoList.size(); i++) {
            when(mapper.createToEntity(requestDtoList.get(i)))
                    .thenReturn(mockedEntityList.get(i));
        }

        for (int i = 0; i < entityList.size(); i++) {
            when(repository.save(mockedEntityList.get(i)))
                    .thenReturn(entityList.get(i));
        }

        for (int i = 0; i < responseDtoList.size(); i++) {
            when(mapper.toDto(entityList.get(i)))
                    .thenReturn(responseDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.saveAll(requestDtoList);

        assertEquals(responseDtoList, resultDtoList);

        requestDtoList.forEach(dto -> {
            verify(validationService, times(1)).validate(dto);
            verify(mapper, times(1)).createToEntity(dto);
            verify(portfolioRepository, times(1)).findById(dto.getPortfolioId());
        });

        mockedEntityList.forEach(mock -> verify(mock, times(1)).setPortfolio(portfolioEntity));
        mockedEntityList.forEach(mock -> verify(mock, times(1)).setId(any()));
        mockedEntityList.forEach(mock -> verify(repository, times(1)).save(mock));
        entityList.forEach(entity -> verify(mapper, times(1)).toDto(entity));
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository);
        mockedEntityList.forEach(Mockito::verifyNoMoreInteractions);
    }

    @Test
    void findById_whenExist_thenReturn() {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse expected = newDividendResponseDto(entity);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.findById(id);

        assertEquals(expected, result);
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(validationService, portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.findById(id));
    }

    @Test
    void findById_whenDoesNotExist_thenThrowException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.findById(id))
                .isInstanceOf(DividendNotFoundDividendException.class)
                .hasMessage("Dividend not found for id:" + id);

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper, portfolioRepository);
    }

    @Test
    void findAllByUsername_whenExist_thenReturnList() {
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();
        when(repository.findAllByUsername(username)).thenReturn(entityList);
        for (int i = 0; i < entityList.size(); i++) {
            when(mapper.toDto(entityList.get(i)))
                    .thenReturn(responseDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.findAllByUsername(username);

        assertEquals(responseDtoList, resultDtoList);
        verify(repository, times(1)).findAllByUsername(username);
        entityList.forEach(entity -> verify(mapper, times(1)).toDto(entity));
        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(portfolioRepository, validationService);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(username));
    }

    @Test
    void findAllByUsername_whenNothingFound_thenReturnEmptyList() {
        when(repository.findAllByUsername(username)).thenReturn(new ArrayList<>());

        List<DividendDtoResponse> result = victim.findAllByUsername(username);

        assertThat(result.size()).isEqualTo(0);
        verify(repository, times(1)).findAllByUsername(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, mapper, portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.findAllByUsername(username));
    }

    @Test
    void findAllByPortfolioId_whenExist_thenReturnListOf() {
        List<DividendEntity> entityList = newDividendEntityList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();
        when(repository.findAllByPortfolioId(portfolioId)).thenReturn(entityList);
        for (int i = 0; i < entityList.size(); i++) {
            when(mapper.toDto(entityList.get(i)))
                    .thenReturn(responseDtoList.get(i));
        }

        List<DividendDtoResponse> resultDtoList = victim.findAllByPortfolioId(portfolioId);

        assertEquals(responseDtoList, resultDtoList);
        verify(repository, times(1)).findAllByPortfolioId(portfolioId);
        entityList.forEach(dto -> verify(mapper, times(1)).toDto(dto));
        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(validationService, portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(portfolioId));
    }

    @Test
    void findALlByPortfolioId_whenNothingFound_ReturnsEmptyList() {
        when(repository.findAllByPortfolioId(portfolioId)).thenReturn(new ArrayList<>());

        List<DividendDtoResponse> result = victim.findAllByPortfolioId(portfolioId);

        assertThat(result.size()).isEqualTo(0);
        verify(repository, times(1)).findAllByPortfolioId(portfolioId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, validationService, portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.findAllByPortfolioId(portfolioId));
    }

    @Test
    void update_whenDto_thenDelegatesToRepo_andReturnsDto() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse expected = newDividendResponseDto(entity);
        PortfolioEntity portfolioEntity = newPortfolioEntitySkipEvents();
        doNothing().when(validationService).validate(requestDto);
        when(mapper.updateToEntity(requestDto)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.of(portfolioEntity));
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.update(requestDto);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(mockedEntity, times(1)).setPortfolio(portfolioEntity);
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository, mockedEntity);
        assertThatNoException().isThrownBy(() -> victim.update(requestDto));
    }

    @Test
    void update_whenPortfolioNotFound_thenThrowException() {
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(entity);
        doNothing().when(validationService).validate(requestDto);
        when(mapper.updateToEntity(requestDto)).thenReturn(entity);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.update(requestDto))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage("Portfolio not found for: " + requestDto);

        verify(validationService, times(1)).validate(requestDto);
        verify(mapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(validationService, mapper, portfolioRepository);
        verifyNoInteractions(repository);
    }

    @Test
        // No need to verify for existence. Will be verified by this.IsOwner(),
        // called by Spring Security through @PreAuthorize().
    void delete_whenId_thenDelete() {
        doNothing().when(repository).deleteById(id);

        assertThatNoException().isThrownBy(() -> victim.deleteById(id));

        verify(repository, times(1)).deleteById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, mapper, portfolioRepository);
    }

    @WithMockUser(username = username)
    @Test
    void isOwner_whenAuthNameMatchesRepoUsername_thenReturnTrue() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        when(repository.findById(id)).thenReturn(Optional.of(mockedEntity));
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(mockedEntity.getUsername()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertTrue(result);
        verify(repository, times(1)).findById(id);
        verify(mockedEntity, times(1)).getUsername();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(mockedEntity, repository, securityContext, authentication);
        verifyNoInteractions(validationService, portfolioRepository, mapper);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @WithMockUser(username = username)
    @Test
    void isOwner_whenNoResourceFound_thenThrowException() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username + "1");

        assertThatThrownBy(() -> victim.isOwner(id))
                .isInstanceOf(DividendNotFoundDividendException.class)
                .hasMessage("Dividend not found for id:" + id);

        verify(repository, times(1)).findById(id);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verifyNoMoreInteractions(repository, securityContext, authentication);
        verifyNoInteractions(validationService, portfolioRepository, mapper);
    }

}