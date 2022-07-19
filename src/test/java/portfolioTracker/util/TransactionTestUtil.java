package portfolioTracker.util;

import portfolioTracker.core.ValidList;
import portfolioTracker.transaction.TransactionController;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static portfolioTracker.dto.eventType.EventType.BUY;
import static portfolioTracker.util.PortfolioTestUtil.newPortfolioEntity;
import static portfolioTracker.util.PortfolioTestUtil.newPortfolioEntitySkipEvents;

public class TransactionTestUtil extends TestUtil {

    public static final URI findByIdUri = linkTo(methodOn(TransactionController.class).findById(id)).toUri();
    public static final URI findAllUri = linkTo(TransactionController.class).toUri();
    public static final URI saveUri = linkTo(TransactionController.class).toUri();
    public static final URI updateUri = linkTo(TransactionController.class).toUri();
    public static final URI deleteByIdUri = linkTo(methodOn(TransactionController.class).deleteById(id)).toUri();
    public static final URI saveAllUri = linkTo(methodOn(TransactionController.class).saveAll(new ValidList<>())).toUri();

    public static TransactionEntity newTransactionEntity() {
        return TransactionEntity.builder()
                .id(UUID.randomUUID().toString())
                .date(LocalDate.now())
                .portfolio(newPortfolioEntitySkipEvents())
                .commission(new BigDecimal(10))
                .ticker("BRK-B")
                .type(BUY)
                .username("john@email.com")
                .price(new BigDecimal(200))
                .shares(new BigDecimal(14))
                .build();
    }

    public static TransactionDtoCreateRequest newTransactionDtoCreateRequest(TransactionEntity entity) {
        return TransactionDtoCreateRequest.builder()
                .date(entity.getDate())
                .portfolioId(entity.getPortfolio().getId())
                .commission(entity.getCommission())
                .ticker(entity.getTicker())
                .price(entity.getPrice())
                .shares(entity.getShares())
                .type(entity.getType())
                .username(entity.getUsername())
                .build();
    }

    public static TransactionDtoUpdateRequest newTransactionDtoUpdateRequest(TransactionEntity entity) {
        return TransactionDtoUpdateRequest.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .portfolioId(entity.getPortfolio().getId())
                .commission(entity.getCommission())
                .ticker(entity.getTicker())
                .type(entity.getType())
                .price(entity.getPrice())
                .username(entity.getUsername())
                .shares(entity.getShares())
                .build();
    }

    public static TransactionDtoResponse newTransactionDtoResponse(TransactionEntity entity) {
        return TransactionDtoResponse.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .portfolioId(entity.getPortfolio().getId())
                .type(entity.getType())
                .ticker(entity.getTicker())
                .username(entity.getUsername())
                .commission(entity.getCommission())
                .price(entity.getPrice())
                .shares(entity.getShares())
                .build();
    }

    public static List<TransactionEntity> newTransactionEntityList() {
        return List.of(
                newTransactionEntity(),
                newTransactionEntity(),
                newTransactionEntity());
    }

    public static List<TransactionDtoResponse> newTransactionDtoResponseList(List<TransactionEntity> entityList) {
        return List.of(
                newTransactionDtoResponse(entityList.get(0)),
                newTransactionDtoResponse(entityList.get(1)),
                newTransactionDtoResponse(entityList.get(2)));
    }

    public static List<TransactionDtoCreateRequest> newTransactionDtoCreateRequestList(List<TransactionEntity> entityList) {
        return List.of(
                newTransactionDtoCreateRequest(entityList.get(0)),
                newTransactionDtoCreateRequest(entityList.get(1)),
                newTransactionDtoCreateRequest(entityList.get(2)));
    }

    public static List<TransactionDtoUpdateRequest> newTransactionDtoUpdateRequestList(List<TransactionEntity> entityList) {
        return List.of(
                newTransactionDtoUpdateRequest(entityList.get(0)),
                newTransactionDtoUpdateRequest(entityList.get(1)),
                newTransactionDtoUpdateRequest(entityList.get(2)));
    }

}
