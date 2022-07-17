package portfolioTracker.dividend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;
import portfolioTracker.portfolio.domain.PortfolioEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
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
    private String id;
    @NotBlank
    @Column(name = "ticker")
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
    @NumberFormat
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
    private String username;


}
