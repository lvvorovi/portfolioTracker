package portfolioTracker.util;

import portfolioTracker.portfolio.PortfolioController;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import javax.validation.ConstraintViolation;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static portfolioTracker.util.DividendTestUtil.newDividendDtoResponseList;
import static portfolioTracker.util.DividendTestUtil.newDividendEntityList;
import static portfolioTracker.util.TransactionTestUtil.*;

public class PortfolioTestUtil extends TestUtil {

    public static final URI saveUri = linkTo(methodOn(PortfolioController.class).save(new PortfolioDtoCreateRequest())).toUri();
    public static final URI findByIdUri = linkTo(methodOn(PortfolioController.class).findById(id, null)).toUri();
    public static final URI findAllByUsernameUri = linkTo(methodOn(PortfolioController.class).findAllByUsername(null, null)).toUri();
    public static final URI updateUri = linkTo(methodOn(PortfolioController.class).update(new PortfolioDtoUpdateRequest())).toUri();
    public static final URI deleteByIdUri = linkTo(methodOn(PortfolioController.class).deleteById(id)).toUri();

    public static PortfolioEntity newPortfolioEntitySkipEvents() {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDividendEntityList(null);
        entity.setTransactionEntityList(null);
        entity.setUsername("john@email.com");
        entity.setCurrency("EUR");
        entity.setName("portfolioName");
        entity.setStrategy("portfolioStrategy");
        return entity;
    }

    public static PortfolioEntity newPortfolioEntity() {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDividendEntityList(newDividendEntityList());
        entity.setTransactionEntityList(newTransactionEntityList());
        entity.setUsername("john@email.com");
        entity.setCurrency("EUR");
        entity.setName("portfolioName");
        entity.setStrategy("portfolioStrategy");
        return entity;
    }

    public static PortfolioDtoUpdateRequest newPortfolioDtoUpdateRequest(PortfolioEntity entity) {
        PortfolioDtoUpdateRequest dto = new PortfolioDtoUpdateRequest();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setCurrency(entity.getCurrency());
        dto.setName(entity.getName());
        dto.setStrategy(entity.getStrategy());
        return dto;
    }

    public static PortfolioDtoCreateRequest newPortfolioDtoCreateRequest(PortfolioEntity entity) {
        return PortfolioDtoCreateRequest.builder()
                .currency(entity.getCurrency())
                .name(entity.getName())
                .strategy(entity.getStrategy())
                .username(entity.getUsername())
                .build();
    }

    public static PortfolioDtoResponse newPortfolioDtoResponse(PortfolioEntity entity) {
        return PortfolioDtoResponse.builder()
                .id(entity.getId())
                .currency(entity.getCurrency())
                .name(entity.getName())
                .strategy(entity.getStrategy())
                .username(entity.getUsername())
                .transactionList(newTransactionDtoResponseList(entity.getTransactionEntityList()))
                .dividendList(newDividendDtoResponseList(entity.getDividendEntityList()))
                .build();
    }

    public static List<PortfolioEntity> newPortfolioEntityList() {
        return List.of(
                newPortfolioEntity(),
                newPortfolioEntity(),
                newPortfolioEntity());
    }

    public static List<PortfolioDtoResponse> newPortfolioDtoResponseDtoList(List<PortfolioEntity> entityList) {
        return List.of(
                newPortfolioDtoResponse(entityList.get(0)),
                newPortfolioDtoResponse(entityList.get(1)),
                newPortfolioDtoResponse(entityList.get(2)));
    }

    public static List<PortfolioDtoUpdateRequest> newPortfolioDtoUpdateRequestList(List<PortfolioEntity> entityList) {
        return List.of(
                newPortfolioDtoUpdateRequest(entityList.get(0)),
                newPortfolioDtoUpdateRequest(entityList.get(1)),
                newPortfolioDtoUpdateRequest(entityList.get(2)));
    }

    public static List<PortfolioDtoCreateRequest> newPortfolioDtoCreateRequestList(List<PortfolioEntity> entityList) {
        return List.of(
                newPortfolioDtoCreateRequest(entityList.get(0)),
                newPortfolioDtoCreateRequest(entityList.get(1)),
                newPortfolioDtoCreateRequest(entityList.get(2)));
    }


    public static String extractMessagesFromViolationSetCreateRequest(Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    public static String extractMessagesFromViolationSetUpdateRequest(Set<ConstraintViolation<PortfolioDtoUpdateRequest>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    public static String extractMessagesFromViolationSetEntity(Set<ConstraintViolation<PortfolioEntity>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }
}
