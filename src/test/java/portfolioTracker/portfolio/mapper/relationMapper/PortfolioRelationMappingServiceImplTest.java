package portfolioTracker.portfolio.mapper.relationMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.PortMapper;
import org.springframework.web.bind.annotation.RequestAttribute;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.dividend.validation.exception.PortfolioNotFoundDividendException;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.PortfolioMapper;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.mapper.TransactionMapper;
import portfolioTracker.transaction.validation.exception.PortfolioNotFoundTransactionException;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.core.ExceptionErrors.TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE;
import static portfolioTracker.util.DividendTestUtil.*;
import static portfolioTracker.util.PortfolioTestUtil.*;
import static portfolioTracker.util.TransactionTestUtil.*;

@ExtendWith(MockitoExtension.class)
class PortfolioRelationMappingServiceImplTest {

    @Mock
    DividendMapper dividendMapper;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    PortfolioMapper portfolioMapper;
    @Mock
    PortfolioRepository portfolioRepository;
    @InjectMocks
    PortfolioRelationMappingServiceImpl victim;

    @Test
    void updateToEntity_whenDividendUpdateRequest_thenDelegateToDividendMapper_setPortfolio_andNoException() {
        DividendEntity dividendEntityMock = mock(DividendEntity.class);
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(newDividendEntity());
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        when(dividendMapper.updateToEntity(requestDto)).thenReturn(dividendEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId()))
                .thenReturn(Optional.of(portfolioEntity));
        doNothing().when(dividendEntityMock).setPortfolio(portfolioEntity);

        DividendEntity result = victim.updateToEntity(requestDto);

        assertEquals(dividendEntityMock, result);
        verify(dividendMapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(dividendEntityMock, times(1)).setPortfolio(portfolioEntity);
        verifyNoMoreInteractions(dividendMapper, portfolioRepository, dividendEntityMock);
        verifyNoInteractions(transactionMapper, portfolioMapper);
        assertThatNoException().isThrownBy(() -> victim.updateToEntity(requestDto));
    }

    @Test
    void updateToEntity_whenDividendUpdateRequest_PortfolioNotFound_thenThrowPortfolioNotFoundDividendException() {
        DividendEntity dividendEntityMock = mock(DividendEntity.class);
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(newDividendEntity());
        when(dividendMapper.updateToEntity(requestDto)).thenReturn(dividendEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.updateToEntity(requestDto))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage(PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);

        verify(dividendMapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(dividendMapper, portfolioRepository);
        verifyNoInteractions(dividendEntityMock, transactionMapper, portfolioMapper);
    }

    @Test
    void createToEntity_whenDividendCreateRequest_thenDelegateToDividendMapper_setPortfolio_andNoException() {
        DividendEntity dividendEntityMock = mock(DividendEntity.class);
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(newDividendEntity());
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        when(dividendMapper.createToEntity(requestDto)).thenReturn(dividendEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId()))
                .thenReturn(Optional.of(portfolioEntity));
        doNothing().when(dividendEntityMock).setPortfolio(portfolioEntity);

        DividendEntity result = victim.createToEntity(requestDto);

        assertEquals(dividendEntityMock, result);
        verify(dividendMapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(dividendEntityMock, times(1)).setPortfolio(portfolioEntity);
        verifyNoMoreInteractions(dividendMapper, portfolioRepository, dividendEntityMock);
        verifyNoInteractions(transactionMapper, portfolioMapper);
        assertThatNoException().isThrownBy(() -> victim.createToEntity(requestDto));
    }

    @Test
    void createToEntity_whenDividendCreateRequest_PortfolioNotFound_thenThrowPortfolioNotFoundDividendException() {
        DividendEntity dividendEntityMock = mock(DividendEntity.class);
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(newDividendEntity());
        when(dividendMapper.createToEntity(requestDto)).thenReturn(dividendEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.createToEntity(requestDto))
                .isInstanceOf(PortfolioNotFoundDividendException.class)
                .hasMessage(PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);

        verify(dividendMapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(dividendMapper, portfolioRepository);
        verifyNoInteractions(dividendEntityMock, transactionMapper, portfolioMapper);
    }

    @Test
    void toDto_whenDividendEntity_thenDelegateToDividendMapper_andNoException() {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse expected = newDividendDtoResponse(entity);
        when(dividendMapper.toDto(entity)).thenReturn(expected);

        DividendDtoResponse result = victim.toDto(entity);

        assertEquals(expected, result);
        verify(dividendMapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(dividendMapper);
        verifyNoInteractions(portfolioRepository, portfolioMapper, transactionMapper);
        assertThatNoException().isThrownBy(() -> victim.toDto(entity));
    }

    @Test
    void updateToEntity_whenTransactionUpdateRequest_thenDelegateToTransactionMapper_setPortfolio_andNoException() {
        TransactionEntity transactionEntityMock = mock(TransactionEntity.class);
        TransactionDtoUpdateRequest requestDto = newTransactionDtoUpdateRequest(newTransactionEntity());
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        when(transactionMapper.updateToEntity(requestDto)).thenReturn(transactionEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId()))
                .thenReturn(Optional.of(portfolioEntity));
        doNothing().when(transactionEntityMock).setPortfolio(portfolioEntity);

        TransactionEntity result = victim.updateToEntity(requestDto);

        assertEquals(transactionEntityMock, result);
        verify(transactionMapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(transactionEntityMock, times(1)).setPortfolio(portfolioEntity);
        verifyNoMoreInteractions(transactionMapper, portfolioRepository, transactionEntityMock);
        verifyNoInteractions(dividendMapper, portfolioMapper);
        assertThatNoException().isThrownBy(() -> victim.updateToEntity(requestDto));
    }

    @Test
    void updateToEntity_whenTransactionUpdateRequest_PortfolioNotFound_thenThrowPortfolioNotFoundDividendException() {
        TransactionEntity transactionEntityMock = mock(TransactionEntity.class);
        TransactionDtoUpdateRequest requestDto = newTransactionDtoUpdateRequest(newTransactionEntity());
        when(transactionMapper.updateToEntity(requestDto)).thenReturn(transactionEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.updateToEntity(requestDto))
                .isInstanceOf(PortfolioNotFoundTransactionException.class)
                .hasMessage(PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);

        verify(transactionMapper, times(1)).updateToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(transactionMapper, portfolioRepository);
        verifyNoInteractions(transactionEntityMock, dividendMapper, portfolioMapper);
    }

    @Test
    void createToEntity_whenTransactionCreateRequest_thenDelegateToTransactionMapper_setPortfolio_andNoException() {
        TransactionEntity transactionEntityMock = mock(TransactionEntity.class);
        TransactionDtoCreateRequest requestDto = newTransactionDtoCreateRequest(newTransactionEntity());
        PortfolioEntity portfolioEntity = newPortfolioEntity();
        when(transactionMapper.createToEntity(requestDto)).thenReturn(transactionEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId()))
                .thenReturn(Optional.of(portfolioEntity));
        doNothing().when(transactionEntityMock).setPortfolio(portfolioEntity);

        TransactionEntity result = victim.createToEntity(requestDto);

        assertEquals(transactionEntityMock, result);
        verify(transactionMapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verify(transactionEntityMock, times(1)).setPortfolio(portfolioEntity);
        verifyNoMoreInteractions(transactionMapper, portfolioRepository, transactionEntityMock);
        verifyNoInteractions(portfolioMapper, dividendMapper);
        assertThatNoException().isThrownBy(() -> victim.createToEntity(requestDto));
    }

    @Test
    void createToEntity_whenTransactionCreateRequest_PortfolioNotFound_thenThrowPortfolioNotFoundDividendException() {
        TransactionEntity transactionEntityMock = mock(TransactionEntity.class);
        TransactionDtoCreateRequest requestDto = newTransactionDtoCreateRequest(newTransactionEntity());
        when(transactionMapper.createToEntity(requestDto)).thenReturn(transactionEntityMock);
        when(portfolioRepository.findById(requestDto.getPortfolioId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> victim.createToEntity(requestDto))
                .isInstanceOf(PortfolioNotFoundTransactionException.class)
                .hasMessage(PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE + requestDto);

        verify(transactionMapper, times(1)).createToEntity(requestDto);
        verify(portfolioRepository, times(1)).findById(requestDto.getPortfolioId());
        verifyNoMoreInteractions(transactionMapper, portfolioRepository);
        verifyNoInteractions(transactionEntityMock, dividendMapper, portfolioMapper);
    }

    @Test
    void toDto_whenTransactionEntity_thenDelegateToTransactionMapper_andNoException() {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoResponse expected = newTransactionDtoResponse(entity);
        when(transactionMapper.toDto(entity)).thenReturn(expected);

        TransactionDtoResponse result = victim.toDto(entity);

        assertEquals(expected, result);
        verify(transactionMapper, times(1)).toDto(entity);
        verifyNoMoreInteractions(transactionMapper);
        verifyNoInteractions(portfolioRepository, portfolioMapper, dividendMapper);
        assertThatNoException().isThrownBy(() -> victim.toDto(entity));
    }

    @Test
    void updateToEntity_whenPortfolioUpdateRequest_thenDelegateToPortfolioMapper_andNoException() {
        PortfolioEntity expected = newPortfolioEntity();
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(expected);
        when(portfolioMapper.updateToEntity(requestDto)).thenReturn(expected);

        PortfolioEntity result = victim.updateToEntity(requestDto);

        assertEquals(expected, result);
        verify(portfolioMapper, times(1)).updateToEntity(requestDto);
        verifyNoMoreInteractions(portfolioMapper);
        verifyNoInteractions(portfolioRepository, dividendMapper, transactionMapper);
        assertThatNoException().isThrownBy(() -> victim.updateToEntity(requestDto));
    }

    @Test
    void createToEntity_whenPortfolioCreateRequest_thenDelegateToPortfolioMapper_andNoException() {
        PortfolioEntity expected = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(expected);
        when(portfolioMapper.createToEntity(requestDto)).thenReturn(expected);

        PortfolioEntity result = victim.createToEntity(requestDto);

        assertEquals(expected, result);
        verify(portfolioMapper, times(1)).createToEntity(requestDto);
        verifyNoMoreInteractions(portfolioMapper);
        verifyNoInteractions(portfolioRepository, dividendMapper, transactionMapper);
        assertThatNoException().isThrownBy(() -> victim.createToEntity(requestDto));
    }

    @Test
    void toDto_whenPortfolioEntity_transactionListIsNull_dividendListIsNull_thenDto_andNoException() {
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        PortfolioDtoResponse responseDtoMock = mock(PortfolioDtoResponse.class);
        when(portfolioMapper.toDto(entityMock)).thenReturn(responseDtoMock);
        when(entityMock.getDividendEntityList()).thenReturn(null);
        when(entityMock.getTransactionEntityList()).thenReturn(null);

        PortfolioDtoResponse result = victim.toDto(entityMock);

        assertEquals(responseDtoMock, result);
        verify(portfolioMapper, times(1)).toDto(entityMock);
        verify(entityMock, times(1)).getDividendEntityList();
        verify(entityMock, times(1)).getTransactionEntityList();
        verifyNoMoreInteractions(entityMock, responseDtoMock, portfolioMapper);
        verifyNoInteractions(transactionMapper, dividendMapper, portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.toDto(entityMock));
    }

    @Test
    void toDto_whenPortfolioEntity_transactionListIsNotNull_dividendListIsNotNull_thenDtoWithEvents_andNoException() {
        PortfolioEntity entityMock = mock(PortfolioEntity.class);
        PortfolioDtoResponse responseDtoMock = mock(PortfolioDtoResponse.class);
        List<DividendEntity> dividendEntityList = newDividendEntityList();
        List<DividendDtoResponse> dividendDtoResponseList = newDividendDtoResponseList(dividendEntityList);
        List<TransactionEntity> transactionEntityList = newTransactionEntityList();
        List<TransactionDtoResponse> transactionDtoResponseList = newTransactionDtoResponseList(transactionEntityList);
        when(portfolioMapper.toDto(entityMock)).thenReturn(responseDtoMock);
        when(entityMock.getDividendEntityList()).thenReturn(dividendEntityList);
        when(entityMock.getTransactionEntityList()).thenReturn(transactionEntityList);
        doNothing().when(responseDtoMock).setTransactionList(transactionDtoResponseList);
        doNothing().when(responseDtoMock).setDividendList(dividendDtoResponseList);

        for (int i = 0; i < dividendEntityList.size(); i++) {
            when(dividendMapper.toDto(dividendEntityList.get(i)))
                    .thenReturn(dividendDtoResponseList.get(i));
        }

        for (int i = 0; i < transactionEntityList.size(); i++) {
            when(transactionMapper.toDto(transactionEntityList.get(i)))
                    .thenReturn(transactionDtoResponseList.get(i));
        }

        PortfolioDtoResponse result = victim.toDto(entityMock);

        assertEquals(responseDtoMock, result);
        verify(portfolioMapper, times(1)).toDto(entityMock);
        verify(responseDtoMock, times(1)).setTransactionList(transactionDtoResponseList);
        verify(responseDtoMock, times(1)).setDividendList(dividendDtoResponseList);
        verify(entityMock, times(2)).getDividendEntityList();
        verify(entityMock, times(2)).getTransactionEntityList();

        dividendEntityList.forEach(entity ->
                verify(dividendMapper, times(1)).toDto(entity));

        transactionEntityList.forEach(entity ->
                verify(transactionMapper, times(1)).toDto(entity));

        verifyNoMoreInteractions(entityMock, responseDtoMock, portfolioMapper, dividendMapper, transactionMapper);
        verifyNoInteractions(portfolioRepository);
        assertThatNoException().isThrownBy(() -> victim.toDto(entityMock));
    }





}