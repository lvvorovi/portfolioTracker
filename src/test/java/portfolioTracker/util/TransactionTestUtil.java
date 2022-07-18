package portfolioTracker.util;

import org.assertj.core.api.AbstractStringAssert;
import org.springframework.boot.test.system.CapturedOutput;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static portfolioTracker.dto.eventType.EventType.BUY;
import static portfolioTracker.portfolio.PortfolioTestUtil.newPortfolioEntity;

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
                .portfolio(newPortfolioEntity())
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

    public static List<TransactionDtoResponse> newTransactionDtoResponseList() {
        return List.of(
                newTransactionDtoResponse(newTransactionEntity()),
                newTransactionDtoResponse(newTransactionEntity()),
                newTransactionDtoResponse(newTransactionEntity())
        );
    }

    public static List<TransactionDtoCreateRequest> newTransactionDtoCreateRequestList() {
        return List.of(
                newTransactionDtoCreateRequest(newTransactionEntity()),
                newTransactionDtoCreateRequest(newTransactionEntity()),
                newTransactionDtoCreateRequest(newTransactionEntity())
        );
    }

    public static AbstractStringAssert<?> assertOutputContainsExpected(CapturedOutput output, String method, URI uri) {
        return assertThat(output.toString())
                .contains("Request id", "requested url", "with method " + method.toUpperCase(), uri.toString());
    }
}
