package com.portfolioTracker.payload.currencyRateDto.dto;

import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.contract.Currency;
import com.portfolioTracker.contract.Date;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class CurrencyRateResponseDto {

    @Currency
    private String portfolioCurrency;
    @Currency
    private String eventCurrency;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal rateClientSells;
    @AmountOfMoney
    private BigDecimal rateClientBuys;

    public String getPortfolioCurrency() {
        return portfolioCurrency;
    }

    public void setPortfolioCurrency(String portfolioCurrency) {
        this.portfolioCurrency = portfolioCurrency;
    }

    public String getEventCurrency() {
        return eventCurrency;
    }

    public void setEventCurrency(String eventCurrency) {
        this.eventCurrency = eventCurrency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRateClientSells() {
        return rateClientSells;
    }

    public void setRateClientSells(BigDecimal rateClientSells) {
        this.rateClientSells = rateClientSells;
    }

    public BigDecimal getRateClientBuys() {
        return rateClientBuys;
    }

    public void setRateClientBuys(BigDecimal rateClientBuys) {
        this.rateClientBuys = rateClientBuys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRateResponseDto that = (CurrencyRateResponseDto) o;
        return Objects.equals(portfolioCurrency, that.portfolioCurrency) && Objects.equals(eventCurrency, that.eventCurrency) && Objects.equals(date, that.date) && Objects.equals(rateClientSells, that.rateClientSells) && Objects.equals(rateClientBuys, that.rateClientBuys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioCurrency, eventCurrency, date, rateClientSells, rateClientBuys);
    }

    @Override
    public String toString() {
        return "CurrencyRateResponseDto{" +
                "currencyFrom='" + portfolioCurrency + '\'' +
                ", currencyTo='" + eventCurrency + '\'' +
                ", date=" + date +
                ", rateClientSells=" + rateClientSells +
                ", rateClientBuys=" + rateClientBuys +
                '}';
    }
}
