package portfolioTracker.dividend.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static portfolioTracker.util.DividendTestUtil.*;

@ExtendWith(MockitoExtension.class)
class DividendMapperImplTest {

    @InjectMocks
    DividendMapperImpl victim;

    @Test
    void createToEntity_whenCreate_thenEntity() {
        DividendEntity expected = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(expected);

        DividendEntity result = victim.createToEntity(request);

        assertEquals(expected.getAmount(), result.getAmount());
        assertEquals(expected.getDate(), result.getDate());
        assertEquals(expected.getExDate(), result.getExDate());
        assertEquals(expected.getUsername(), result.getUsername());
        assertEquals(expected.getTicker(), result.getTicker());
        assertEquals(expected.getType(), result.getType());
        assertNull(result.getPortfolio());
        assertNull(result.getId());
    }

    @Test
    void updateToEntity_whenUpdate_thenEntity() {
        DividendEntity expected = newDividendEntity();
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(expected);

        DividendEntity result = victim.updateToEntity(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getUsername(), result.getUsername());
        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getAmount(), result.getAmount());
        assertEquals(expected.getExDate(), result.getExDate());
        assertEquals(expected.getDate(), result.getDate());
        assertEquals(expected.getTicker(), result.getTicker());
        assertNull(result.getPortfolio());
    }

    @Test
    void toDto_whenEntity_thenResponse() {
        DividendEntity request = newDividendEntity();
        DividendDtoResponse expected = newDividendDtoResponse(request);

        DividendDtoResponse result = victim.toDto(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getAmount(), result.getAmount());
        assertEquals(expected.getDate(), result.getDate());
        assertEquals(expected.getTicker(), result.getTicker());
        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getPortfolioId(), result.getPortfolioId());
        assertEquals(expected.getExDate(), result.getExDate());
        assertEquals(expected.getUsername(), result.getUsername());

    }
}