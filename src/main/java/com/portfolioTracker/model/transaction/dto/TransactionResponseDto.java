package com.portfolioTracker.model.transaction.dto;

import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.contract.Date;
import com.portfolioTracker.contract.Quantity;
import com.portfolioTracker.contract.Ticker;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class TransactionResponseDto extends RepresentationModel<TransactionResponseDto> {

    private Long id;
    @Ticker
    private String ticker;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal price;
    @Quantity
    private BigDecimal shares;
    @AmountOfMoney
    private BigDecimal commission;
    @NotEmpty
    private EventType type;
    @NotNull
    private Long portfolioId;
    private BigDecimal bought;
    private BigDecimal sold;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransactionResponseDto that = (TransactionResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(ticker, that.ticker) && Objects.equals(date, that.date) && Objects.equals(price, that.price) && Objects.equals(shares, that.shares) && Objects.equals(commission, that.commission) && type == that.type && Objects.equals(portfolioId, that.portfolioId) && Objects.equals(bought, that.bought) && Objects.equals(sold, that.sold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, ticker, date, price, shares, commission, type, portfolioId, bought, sold);
    }

    @Override
    public String toString() {
        return "TransactionResponseDto{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", shares=" + shares +
                ", commission=" + commission +
                ", type=" + type +
                ", portfolioId=" + portfolioId +
                ", bought=" + bought +
                ", sold=" + sold +
                '}';
    }
}
