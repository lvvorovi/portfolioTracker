package portfolioTracker.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;
import portfolioTracker.portfolio.domain.PortfolioEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Validated
@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    private String id;
    @NotBlank
    @Column(name = "ticker")
    private String ticker;
    @NotNull
    @PastOrPresent
    @Column(name = "trade_date")
    private LocalDate date;
    @NotNull
    @NumberFormat
    @Positive
    @Column(name = "quantity")
    private BigDecimal shares;
    @NotNull
    @NumberFormat
    @Positive
    @Column(name = "price")
    private BigDecimal price;
    @NotNull
    @NumberFormat
    @PositiveOrZero
    @Column(name = "commission")
    private BigDecimal commission;
    @NotNull
    @Column(name = "event_type")
    private EventType type;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;
    @NotBlank
    @Column(name = "username")
    private String username;

}
