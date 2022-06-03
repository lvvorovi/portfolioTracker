package com.portfolioTracker.externalApi.yahoo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.contract.Date;
import com.portfolioTracker.contract.Quantity;

import java.math.BigDecimal;

@JsonIgnoreProperties({"data", "type", "numerator", "denominator", "splitRatio", "amount"})
public class Price {

    @Date
    private String date;
    @AmountOfMoney
    private BigDecimal open;
    @AmountOfMoney
    private BigDecimal high;
    @AmountOfMoney
    private BigDecimal low;
    @AmountOfMoney
    private BigDecimal close;
    @Quantity
    private BigDecimal volume;
    @AmountOfMoney
    private BigDecimal adjclose;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getAdjclose() {
        return adjclose;
    }

    public void setAdjclose(BigDecimal adjclose) {
        this.adjclose = adjclose;
    }
}
