package portfolioTracker.dividend.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static portfolioTracker.dividend.DividendTestUtil.*;

@SpringBootTest(classes = DividendMapperImpl.class)
class DividendMapperImplTest {

    @Autowired
    DividendMapperImpl victim;

    @Test
    void createToEntity_whenValidRequest_thenReturnsEntity() {
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
    void updateToEntity_whenAllFields() {
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
    void toDto() {
        DividendEntity request = newDividendEntity();
        DividendDtoResponse expected = newDividendResponseDto(request);

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