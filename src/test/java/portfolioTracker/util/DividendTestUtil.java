package portfolioTracker.util;

import portfolioTracker.core.ValidList;
import portfolioTracker.dividend.DividendController;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static portfolioTracker.dto.eventType.EventType.DIVIDEND;
import static portfolioTracker.portfolio.PortfolioTestUtil.newPortfolioEntitySkipEvents;

public class DividendTestUtil extends TestUtil {

    public static final URI findByIdUri = linkTo(methodOn(DividendController.class).findById(id)).toUri();
    public static final URI findAllUri = linkTo(DividendController.class).toUri();
    public static final URI saveUri = linkTo(DividendController.class).toUri();
    public static final URI saveAllUri = linkTo(methodOn(DividendController.class).saveAll(new ValidList<>())).toUri();
    public static final URI deleteByIdUri = linkTo(methodOn(DividendController.class).deleteById(id)).toUri();
    public static final URI updateUri = linkTo(DividendController.class).toUri();


    public static DividendEntity newDividendEntity() {
        DividendEntity entity = new DividendEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setUsername("john@email.com");
        entity.setPortfolio(newPortfolioEntitySkipEvents());
        entity.setAmount(new BigDecimal(100));
        entity.setDate(LocalDate.now());
        entity.setTicker("BRK-B");
        entity.setExDate(LocalDate.now().minusDays(2));
        entity.setType(DIVIDEND);
        return entity;
    }

    public static DividendDtoUpdateRequest newDividendDtoUpdateRequest(DividendEntity entity) {
        DividendDtoUpdateRequest dto = new DividendDtoUpdateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setId(entity.getId());
        dto.setTicker(entity.getTicker());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        return dto;
    }

    public static DividendDtoCreateRequest newDividendDtoCreateRequest(DividendEntity entity) {
        DividendDtoCreateRequest dto = new DividendDtoCreateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        dto.setTicker(entity.getTicker());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setType(entity.getType());
        return dto;
    }

    public static DividendDtoResponse newDividendResponseDto(DividendEntity entity) {
        DividendDtoResponse dto = new DividendDtoResponse();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setUsername(entity.getUsername());
        dto.setDate(entity.getDate());
        dto.setTicker(entity.getTicker());
        dto.setExDate(entity.getExDate());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        return dto;
    }

    public static List<DividendDtoResponse> newDividendDtoResponseList() {
        return List.of(
                newDividendResponseDto(newDividendEntity()),
                newDividendResponseDto(newDividendEntity()),
                newDividendResponseDto(newDividendEntity())
        );
    }

    public static List<DividendDtoCreateRequest> newDividendDtoCreateList() {
        return List.of(
                newDividendDtoCreateRequest(newDividendEntity()),
                newDividendDtoCreateRequest(newDividendEntity()),
                newDividendDtoCreateRequest(newDividendEntity())
        );
    }

    public static List<DividendEntity> newDividendEntityList() {
        return List.of(
                newDividendEntity(),
                newDividendEntity(),
                newDividendEntity()
        );
    }

    public static List<DividendEntity> newDividendEntityMockList() {
        return List.of(
                mock(DividendEntity.class),
                mock(DividendEntity.class),
                mock(DividendEntity.class)
        );
    }

    /*
    TODO migrate to Logging test
        public static AbstractStringAssert<?> assertOutputContainsExpected(CapturedOutput output, String method, URI uri) {
        return assertThat(output.toString())
                .contains("Request id", "requested url", "with method " + method.toUpperCase(), uri.toString());
    }
    * */

    public static String extractMessagesFromViolationSetCreateRequest(Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    public static String extractMessagesFromViolationSetUpdateRequest(Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    public static String extractMessagesFromViolationSetEntity(Set<ConstraintViolation<DividendEntity>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

}
