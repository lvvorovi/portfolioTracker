package com.portfolioTracker.externalApi.yahoo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Quantity;

import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties({"data", "type", "numerator", "denominator", "splitRatio", "amount"})
public class YahooPrice {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YahooPrice yahooPrice = (YahooPrice) o;
        return Objects.equals(date, yahooPrice.date) && Objects.equals(open, yahooPrice.open) && Objects.equals(high, yahooPrice.high) && Objects.equals(low, yahooPrice.low) && Objects.equals(close, yahooPrice.close) && Objects.equals(volume, yahooPrice.volume) && Objects.equals(adjclose, yahooPrice.adjclose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, open, high, low, close, volume, adjclose);
    }

    @Override
    public String toString() {
        return "YahooPrice{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", adjclose=" + adjclose +
                '}';
    }
}
