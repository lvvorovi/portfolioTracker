package com.portfolioTracker.model.transaction.dto;

import com.portfolioTracker.contract.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Validated
public class TransactionRequestDto {

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
    @NotBlank
    private String type;
    @PortfolioId
    private Long portfolioId;

    public TransactionRequestDto() {
    }

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
        this.ticker = ticker.toUpperCase();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRequestDto that = (TransactionRequestDto) o;
        return Objects.equals(id, that.id) && Objects.equals(ticker, that.ticker) && Objects.equals(date, that.date) && Objects.equals(shares, that.shares) && Objects.equals(price, that.price) && Objects.equals(commission, that.commission) && Objects.equals(type, that.type) && Objects.equals(portfolioId, that.portfolioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, date, shares, price, commission, type, portfolioId);
    }

    @Override
    public String toString() {
        return "TransactionRequestDto{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", date=" + date +
                ", totalShares=" + shares +
                ", price=" + price +
                ", commission=" + commission +
                ", type='" + type + '\'' +
                ", portfolioId=" + portfolioId +
                '}';
    }
}
