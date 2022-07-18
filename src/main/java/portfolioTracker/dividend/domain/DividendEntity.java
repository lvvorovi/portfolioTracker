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
    @Size(max = 36, min = 36, message = "must be exactly 36 characters")
    private String id;
    @NotBlank
    @Column(name = "ticker")
    @Size(max = 50, message = "max 50 characters")
    private String ticker;
    @NotNull
    @PastOrPresent
    @Column(name = "ex_dividend_date")
    private LocalDate exDate;
    @NotNull
    @PastOrPresent
    @Column(name = "payment_date")
    private LocalDate date;
    @NotNull
    @Positive
    @Column(name = "amount")
    private BigDecimal amount;
    @NotNull
    @Column(name = "event_type")
    private EventType type;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;
    @Column(name = "username")
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;
}
