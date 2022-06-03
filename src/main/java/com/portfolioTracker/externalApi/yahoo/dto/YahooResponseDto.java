package com.portfolioTracker.externalApi.yahoo.dto;

import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.contract.Currency;
import com.portfolioTracker.contract.Ticker;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class YahooResponseDto {

    @Ticker
    private String ticker;
    @Currency
    private String currency;
    @AmountOfMoney
    private BigDecimal currentMarketPrice;
    @NotNull
    private List<Price> priceList;
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

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<YahooEvent> getEventDataList() {
        return yahooEventList;
    }

    public void setEventDataList(List<YahooEvent> yahooEventList) {
        this.yahooEventList = yahooEventList;
    }
}
