package com.portfolioTracker.domain.transaction;

import com.portfolioTracker.domain.dto.event.eventType.EventType;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Validated
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;

}
