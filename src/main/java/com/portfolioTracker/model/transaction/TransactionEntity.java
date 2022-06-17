package com.portfolioTracker.model.transaction;

import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Quantity;
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
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Ticker
    @Column(name = "ticker")
    private String ticker;
    @Date
    @Column(name = "trade_date")
    private LocalDate date;
    @Quantity
    @Column(name = "quantity")
    private BigDecimal shares;
    @Column(name = "price")
    @AmountOfMoney
    private BigDecimal price;
    @AmountOfMoney
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
