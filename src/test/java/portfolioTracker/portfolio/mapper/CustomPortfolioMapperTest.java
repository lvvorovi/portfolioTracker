package portfolioTracker.portfolio.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.transaction.mapper.TransactionMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import static portfolioTracker.util.PortfolioTestUtil.*;

@ExtendWith(MockitoExtension.class)
class CustomPortfolioMapperTest {

    @Mock
    TransactionMapper transactionMapper;
    @Mock
    DividendMapper dividendMapper;

    @InjectMocks
    CustomPortfolioMapper victim;

    @Test
    void updateToEntity_whenUpdate_thenEntity() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(entity);

        PortfolioEntity result = victim.updateToEntity(requestDto);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getUsername(), result.getUsername());
        assertEquals(entity.getCurrency(), result.getCurrency());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getStrategy(), result.getStrategy());
        assertNotEquals(entity.getDividendEntityList(), result.getDividendEntityList());
        assertNotEquals(entity.getTransactionEntityList(), result.getTransactionEntityList());
        verifyNoInteractions(dividendMapper, transactionMapper);
    }

    @Test
    void createToEntity_whenUpdate_thenEntity() {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(entity);

        PortfolioEntity result = victim.createToEntity(requestDto);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getUsername(), result.getUsername());
        assertEquals(entity.getCurrency(), result.getCurrency());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getStrategy(), result.getStrategy());
        assertNotEquals(entity.getDividendEntityList(), result.getDividendEntityList());
        assertNotEquals(entity.getTransactionEntityList(), result.getTransactionEntityList());
        verifyNoInteractions(dividendMapper, transactionMapper);
    }

    @Test
    void toDto_whenEntityWithEvents_thenDtoAndEventsToDto() {
        PortfolioEntity mockedEntity = mock(PortfolioEntity.class);
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse responseDto = newPortfolioDtoResponse(entity);
        when(mockedEntity.getDividendEntityList()).thenReturn(entity.getDividendEntityList());
        when(mockedEntity.getTransactionEntityList()).thenReturn(entity.getTransactionEntityList());

        for (int i = 0; i < entity.getDividendEntityList().size(); i++) {
            when(dividendMapper.toDto(entity.getDividendEntityList().get(i)))
                    .thenReturn(responseDto.getDividendList().get(i));
        }

        for (int i = 0; i < entity.getTransactionEntityList().size(); i++) {
            when(transactionMapper.toDto(entity.getTransactionEntityList().get(i)))
                    .thenReturn(responseDto.getTransactionList().get(i));
        }

        PortfolioDtoResponse result = victim.toDto(mockedEntity);

        assertEquals(responseDto.getTransactionList(), result.getTransactionList());
        assertEquals(responseDto.getDividendList(), result.getDividendList());
        verify(mockedEntity, times(1)).getId();
        verify(mockedEntity, times(1)).getName();
        verify(mockedEntity, times(1)).getCurrency();
        verify(mockedEntity, times(1)).getUsername();
        verify(mockedEntity, times(1)).getStrategy();
        verify(mockedEntity, times(1)).getTransactionEntityList();
        verify(mockedEntity, times(1)).getDividendEntityList();

        for (int i = 0; i < entity.getTransactionEntityList().size(); i++) {
            verify(transactionMapper, times(1))
                    .toDto(entity.getTransactionEntityList().get(i));
        }

        for (int i = 0; i < entity.getDividendEntityList().size(); i++) {
            verify(dividendMapper, times(1))
                    .toDto(entity.getDividendEntityList().get(i));
        }

        verifyNoMoreInteractions(dividendMapper, transactionMapper, mockedEntity);
        assertEquals(mockedEntity.getId(), result.getId());
        assertEquals(mockedEntity.getUsername(), result.getUsername());
        assertEquals(mockedEntity.getName(), result.getName());
        assertEquals(mockedEntity.getCurrency(), result.getCurrency());
        assertEquals(mockedEntity.getStrategy(), result.getStrategy());
    }

}