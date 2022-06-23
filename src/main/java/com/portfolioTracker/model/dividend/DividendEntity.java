package com.portfolioTracker.model.dividend;

import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Validated
@Entity
@Table(name = "dividends")
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;


}
