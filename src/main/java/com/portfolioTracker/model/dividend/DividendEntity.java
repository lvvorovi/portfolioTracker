package com.portfolioTracker.model.dividend;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
@Entity
@Table(name = "dividends")
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Ticker
    @Column(name = "ticker")
    private String ticker;
    @Date
    @Column(name = "ex_dividend_date")
    private LocalDate exDate;
    @Date
    @Column(name = "payment_date")
    private LocalDate date;
    @AmountOfMoney
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
