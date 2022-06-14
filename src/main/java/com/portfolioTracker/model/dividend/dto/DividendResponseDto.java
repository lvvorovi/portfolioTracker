package com.portfolioTracker.model.dividend.dto;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class DividendResponseDto extends RepresentationModel<DividendResponseDto> {

    private Long id;
    @Ticker
    private String ticker;
    @Date
    private LocalDate exDate;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal amount;
    @NotEmpty
    private EventType type;

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

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DividendResponseDto that = (DividendResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(ticker, that.ticker) && Objects.equals(exDate, that.exDate) && Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, ticker, exDate, date, amount, type);
    }

    @Override
    public String toString() {
        return "DividendResponseDto{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", exDate=" + exDate +
                ", date=" + date +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}
