package portfolioTracker.dividend.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
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
import static portfolioTracker.dividend.DividendTestUtil.*;
import static portfolioTracker.portfolio.PortfolioTestUtil.newPortfolioEntity;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class DividendServiceImplTest {

    private final String username = "user";
    private final String id = "id";
    private final String portfolioId = "portfolioId";

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
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);
        DividendDtoResponse expected = newDividendResponseDto(entity);
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        doNothing().when(validationService).validate(request);
        when(mapper.createToEntity(request)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(request.getPortfolioId())).thenReturn(Optional.of(portfolioEntity));
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.save(request);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(request);
        verify(mapper, times(1)).createToEntity(request);
        verify(portfolioRepository, times(1)).findById(request.getPortfolioId());
        verify(mockedEntity, times(1)).setPortfolio(portfolioEntity);
        verify(mockedEntity, times(1)).setId(any());
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository, mockedEntity);
        assertThatNoException().isThrownBy(() -> victim.save(request));
    }

    @Test
    void save_whenPortfolioIdDoesNotExist_thenThrowException() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(newDividendEntity());
        doNothing().when(validationService).validate(request);
        when(mapper.createToEntity(request)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(request.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.save(request))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage("Portfolio not found for: " + request);

        verify(validationService, times(1)).validate(request);
        verify(mapper, times(1)).createToEntity(request);
        verify(portfolioRepository, times(1)).findById(request.getPortfolioId());
        verifyNoMoreInteractions(mapper, portfolioRepository, validationService);
        verifyNoInteractions(repository, mockedEntity);
    }

    @Test
    void saveAll_whenDtoList_thenDelegateToRepo_andReturnDtoList() {
        DividendEntity mockedEntity = mock(DividendEntity.class);
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);
        List<DividendDtoCreateRequest> requestList = newDividendDtoCreateList(request);
        DividendDtoResponse response = newDividendResponseDto(entity);
        List<DividendDtoResponse> expected = newDividendDtoResponseList(response);
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        doNothing().when(validationService).validate(request);
        when(mapper.createToEntity(request)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(request.getPortfolioId())).thenReturn(Optional.of(portfolioEntity));
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);

        List<DividendDtoResponse> resultList = victim.saveAll(requestList);

        assertEquals(expected, resultList);
        resultList.forEach(result -> {
            verify(validationService, times(requestList.size())).validate(request);
            verify(mapper, times(requestList.size())).createToEntity(request);
            verify(portfolioRepository, times(requestList.size())).findById(request.getPortfolioId());
            verify(mockedEntity, times(requestList.size())).setPortfolio(portfolioEntity);
            verify(mockedEntity, times(requestList.size())).setId(any());
            verify(repository, times(requestList.size())).save(mockedEntity);
            verify(mapper, times(requestList.size())).toDto(entity);
        });
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository, mockedEntity);
        assertThatNoException().isThrownBy(() -> victim.save(request));
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
        DividendEntity entity = newDividendEntity();
        List<DividendEntity> entityList = newDividendEntityList(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        List<DividendDtoResponse> expected = newDividendDtoResponseList(response);
        when(repository.findAllByUsername(username)).thenReturn(entityList);
        entityList.forEach(index -> when(mapper.toDto(index)).thenReturn(response));

        List<DividendDtoResponse> result = victim.findAllByUsername(username);

        assertEquals(expected, result);
        verify(repository, times(1)).findAllByUsername(username);
        entityList.forEach(index -> verify(mapper, times(entityList.size())).toDto(index));
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
        DividendEntity entity = newDividendEntity();
        List<DividendEntity> entityList = newDividendEntityList(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        List<DividendDtoResponse> expected = newDividendDtoResponseList(response);
        when(repository.findAllByPortfolioId(portfolioId)).thenReturn(entityList);
        entityList.forEach(index -> when(mapper.toDto(index)).thenReturn(response));

        List<DividendDtoResponse> result = victim.findAllByPortfolioId(portfolioId);

        assertEquals(expected, result);
        verify(repository, times(1)).findAllByPortfolioId(portfolioId);
        entityList.forEach(index -> verify(mapper, times(entityList.size())).toDto(index));
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
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse expected = newDividendResponseDto(entity);
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        doNothing().when(validationService).validate(request);
        when(mapper.updateToEntity(request)).thenReturn(mockedEntity);
        when(portfolioRepository.findById(request.getPortfolioId())).thenReturn(Optional.of(portfolioEntity));
        when(repository.save(mockedEntity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.update(request);

        assertEquals(expected, result);
        verify(validationService, times(1)).validate(request);
        verify(mapper, times(1)).updateToEntity(request);
        verify(portfolioRepository, times(1)).findById(request.getPortfolioId());
        verify(mockedEntity, times(1)).setPortfolio(portfolioEntity);
        verify(repository, times(1)).save(mockedEntity);
        verify(mapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(validationService, mapper, repository, portfolioRepository, mockedEntity);
        assertThatNoException().isThrownBy(() -> victim.update(request));
    }

    @Test
    void update_whenPortfolioNotFound_thenThrowException() {
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(entity);
        doNothing().when(validationService).validate(request);
        when(mapper.updateToEntity(request)).thenReturn(entity);
        when(portfolioRepository.findById(request.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.update(request))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage("Portfolio not found for: " + request);

        verify(validationService, times(1)).validate(request);
        verify(mapper, times(1)).updateToEntity(request);
        verify(portfolioRepository, times(1)).findById(request.getPortfolioId());
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
        when(mockedEntity.getUsername()).thenReturn(username);

        boolean result = victim.isOwner(id);

        assertTrue(result);
        verify(repository, times(1)).findById(id);
        verify(mockedEntity, times(1)).getUsername();
        verifyNoMoreInteractions(mockedEntity, repository);
        verifyNoInteractions(validationService, portfolioRepository, mapper);
        assertThatNoException().isThrownBy(() -> victim.isOwner(id));
    }

    @WithMockUser(username = username)
    @Test
    void isOwner_whenNoResourceFound_thenThrowException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.isOwner(id))
                .isInstanceOf(DividendNotFoundDividendException.class)
                .hasMessage("Dividend not found for id:" + id);

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(validationService, portfolioRepository, mapper);
    }

}