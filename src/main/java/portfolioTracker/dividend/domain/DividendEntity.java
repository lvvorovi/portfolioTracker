package portfolioTracker.dividend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;
import portfolioTracker.portfolio.domain.PortfolioEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static portfolioTracker.core.ExceptionErrors.*;

@Data
@Validated
@Entity
@Table(name = "dividends")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendEntity implements Serializable {

    @Id
    @Column(name = "id")
    @Size(max = 36, min = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Column(name = "ticker")
    @Size(max = 50, message = TICKER_MAX_LENGTH_ERROR_MESSAGE)
    private String ticker;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    @Column(name = "ex_dividend_date")
    private LocalDate exDate;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    @Column(name = "payment_date")
    private LocalDate date;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    @Column(name = "amount")
    private BigDecimal amount;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Column(name = "event_type")
    private EventType type;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;
    @Column(name = "username")
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    private String username;
}
