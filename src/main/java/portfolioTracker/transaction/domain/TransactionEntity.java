package portfolioTracker.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;
import portfolioTracker.portfolio.domain.PortfolioEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static portfolioTracker.core.ExceptionErrors.*;

@Data
@Validated
@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @Size(min = 36, max = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = TICKER_MAX_LENGTH_ERROR_MESSAGE)
    @Column(name = "ticker")
    private String ticker;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    @Column(name = "trade_date")
    private LocalDate date;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    @Column(name = "quantity")
    private BigDecimal shares;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    @Column(name = "price")
    private BigDecimal price;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PositiveOrZero(message = POSITIVE_OR_ZERO_ERROR_MESSAGE)
    @Column(name = "commission")
    private BigDecimal commission;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Column(name = "event_type")
    private EventType type;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    @Column(name = "username")
    private String username;

}
