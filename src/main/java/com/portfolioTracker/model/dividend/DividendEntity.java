package com.portfolioTracker.model.dividend;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty
    @Column(name = "event_type")
    private EventType type;
/*

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public LocalDate getExDate() {
        return exDate;
    }

    public void setExDate(LocalDate exDate) {
        this.exDate = exDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EventType getType() {
        return type;
    }
*/

    public void setType(EventType type) {
        this.type = EventType.DIVIDEND;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DividendEntity that = (DividendEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(ticker, that.ticker) && Objects.equals(exDate, that.exDate) && Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, exDate, date, amount, type);
    }

    @Override
    public String toString() {
        return "DividendEntity{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", exDate=" + exDate +
                ", date=" + date +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }*/
}
