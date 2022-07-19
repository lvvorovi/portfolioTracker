package portfolioTracker.portfolio.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static portfolioTracker.util.PortfolioTestUtil.*;

@ExtendWith(MockitoExtension.class)
class CustomPortfolioMapperTest {

    @InjectMocks
    CustomPortfolioMapper victim;

    @Test
    void updateToEntity_whenUpdate_thenEntity() {
        PortfolioDtoUpdateRequest mockedRequestDto = mock(PortfolioDtoUpdateRequest.class);
        PortfolioEntity expected = newPortfolioEntity();
        expected.setDividendEntityList(null);
        expected.setTransactionEntityList(null);
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(expected);
        when(mockedRequestDto.getId()).thenReturn(requestDto.getId());
        when(mockedRequestDto.getStrategy()).thenReturn(requestDto.getStrategy());
        when(mockedRequestDto.getUsername()).thenReturn(requestDto.getUsername());
        when(mockedRequestDto.getCurrency()).thenReturn(requestDto.getCurrency());
        when(mockedRequestDto.getName()).thenReturn(requestDto.getName());

        PortfolioEntity result = victim.updateToEntity(mockedRequestDto);

        assertEquals(expected, result);
        verify(mockedRequestDto, times(1)).getId();
        verify(mockedRequestDto, times(1)).getName();
        verify(mockedRequestDto, times(1)).getStrategy();
        verify(mockedRequestDto, times(1)).getCurrency();
        verify(mockedRequestDto, times(1)).getUsername();
        verifyNoMoreInteractions(mockedRequestDto);
    }

    @Test
    void createToEntity_whenUpdate_thenEntity() {
        PortfolioDtoCreateRequest mockedRequestDto = mock(PortfolioDtoCreateRequest.class);
        PortfolioEntity expected = newPortfolioEntitySkipEvents();
        expected.setId(null);
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(expected);
        when(mockedRequestDto.getCurrency()).thenReturn(requestDto.getCurrency());
        when(mockedRequestDto.getUsername()).thenReturn(requestDto.getUsername());
        when(mockedRequestDto.getName()).thenReturn(requestDto.getName());
        when(mockedRequestDto.getStrategy()).thenReturn(requestDto.getStrategy());

        PortfolioEntity result = victim.createToEntity(mockedRequestDto);

        assertEquals(expected, result);
        verify(mockedRequestDto, times(1)).getStrategy();
        verify(mockedRequestDto, times(1)).getName();
        verify(mockedRequestDto, times(1)).getUsername();
        verify(mockedRequestDto, times(1)).getCurrency();
        verifyNoMoreInteractions(mockedRequestDto);
    }

    @Test
    void toDto_whenEntityWithEvents_thenDtoAndEventsToDto() {
        PortfolioEntity mockedEntity = mock(PortfolioEntity.class);
        PortfolioEntity entity = newPortfolioEntitySkipEvents();
        PortfolioDtoResponse expected = newPortfolioDtoResponse(entity);
        when(mockedEntity.getId()).thenReturn(entity.getId());
        when(mockedEntity.getName()).thenReturn(entity.getName());
        when(mockedEntity.getCurrency()).thenReturn(entity.getCurrency());
        when(mockedEntity.getStrategy()).thenReturn(entity.getStrategy());
        when(mockedEntity.getUsername()).thenReturn(entity.getUsername());


        PortfolioDtoResponse result = victim.toDto(mockedEntity);

        assertEquals(expected, result);
        verify(mockedEntity, times(1)).getId();
        verify(mockedEntity, times(1)).getName();
        verify(mockedEntity, times(1)).getCurrency();
        verify(mockedEntity, times(1)).getUsername();
        verify(mockedEntity, times(1)).getStrategy();
        verifyNoMoreInteractions(mockedEntity);
    }

}