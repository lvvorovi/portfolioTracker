package com.portfolioTracker.externalApi.yahoo.dto;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.Ticker;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class YahooResponseDto {

    @Ticker
    private String ticker;
    @Currency
    private String currency;
    @AmountOfMoney
    private BigDecimal currentMarketPrice;
    @NotNull
    private List<YahooPrice> yahooPriceList;
    @NotNull
    private List<YahooEvent> yahooEventList;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getCurrentMarketPrice() {
        return currentMarketPrice;
    }

    public void setCurrentMarketPrice(BigDecimal currentMarketPrice) {
        this.currentMarketPrice = currentMarketPrice;
    }

    public List<YahooPrice> getPriceList() {
        return yahooPriceList;
    }

    public void setPriceList(List<YahooPrice> yahooPriceList) {
        this.yahooPriceList = yahooPriceList;
    }

    public List<YahooEvent> getEventDataList() {
        return yahooEventList;
    }

    public void setEventDataList(List<YahooEvent> yahooEventList) {
        this.yahooEventList = yahooEventList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YahooResponseDto that = (YahooResponseDto) o;
        return Objects.equals(ticker, that.ticker) && Objects.equals(currency, that.currency) && Objects.equals(currentMarketPrice, that.currentMarketPrice) && Objects.equals(yahooPriceList, that.yahooPriceList) && Objects.equals(yahooEventList, that.yahooEventList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, currency, currentMarketPrice, yahooPriceList, yahooEventList);
    }

    @Override
    public String toString() {
        return "YahooResponseDto{" +
                "ticker='" + ticker + '\'' +
                ", currency='" + currency + '\'' +
                ", currentMarketPrice=" + currentMarketPrice +
                ", yahooPriceList=" + yahooPriceList +
                ", yahooEventList=" + yahooEventList +
                '}';
    }
}
