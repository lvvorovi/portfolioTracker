package com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event;

import com.portfolioTracker.contract.Date;
import com.portfolioTracker.contract.Ticker;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Event {

    private Long id;
    @Ticker
    private String ticker;
    @NotEmpty
    private EventType type;
    private LocalDate exDate;
    @Date
    private LocalDate date;
    private BigDecimal priceAmount;
    private BigDecimal shares;
    private BigDecimal bought;
    private BigDecimal sold;
    private BigDecimal dividend;
    private BigDecimal commission;

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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
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

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public BigDecimal getBought() {
        return bought;
    }

    public void setBought(BigDecimal bought) {
        this.bought = bought;
    }

    public BigDecimal getSold() {
        return sold;
    }

    public void setSold(BigDecimal sold) {
        this.sold = sold;
    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public void setDividend(BigDecimal dividend) {
        this.dividend = dividend;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id)
                && Objects.equals(ticker, event.ticker)
                && type == event.type
                && Objects.equals(exDate, event.exDate)
                && Objects.equals(date, event.date)
                && Objects.equals(priceAmount, event.priceAmount)
                && Objects.equals(shares, event.shares)
                && Objects.equals(bought, event.bought)
                && Objects.equals(sold, event.sold)
                && Objects.equals(dividend, event.dividend)
                && Objects.equals(commission, event.commission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, type, exDate, date, priceAmount, shares, bought, sold, dividend, commission);
    }

    @Override
    public String toString() {
        return "YahooEvent{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", type=" + type +
                ", exDate=" + exDate +
                ", date=" + date +
                ", priceAmount=" + priceAmount +
                ", shares=" + shares +
                ", bought=" + bought +
                ", sold=" + sold +
                ", dividend=" + dividend +
                ", commission=" + commission +
                '}';
    }
}
