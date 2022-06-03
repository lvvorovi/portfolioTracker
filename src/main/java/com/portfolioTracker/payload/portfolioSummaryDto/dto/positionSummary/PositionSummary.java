package com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary;

import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.contract.Quantity;
import com.portfolioTracker.contract.Ticker;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PositionSummary {

    @Ticker
    private String ticker;
    @AmountOfMoney
    private BigDecimal totalBough;
    @Quantity
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentSharePrice;
    @AmountOfMoney
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @NotNull
    private BigDecimal dividend;
    @NotNull
    private BigDecimal commission;
    @NotNull
    private BigDecimal currencyGain;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    private BigDecimal totalReturnPerc;

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getTotalBough() {
        return totalBough;
    }

    public void setTotalBough(BigDecimal totalBough) {
        this.totalBough = totalBough;
    }

    public BigDecimal getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(BigDecimal totalShares) {
        this.totalShares = totalShares;
    }

    public BigDecimal getCurrentSharePrice() {
        return currentSharePrice;
    }

    public void setCurrentSharePrice(BigDecimal currentSharePrice) {
        this.currentSharePrice = currentSharePrice;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getCapitalGain() {
        return capitalGain;
    }

    public void setCapitalGain(BigDecimal capitalGain) {
        this.capitalGain = capitalGain;
    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public void setDividend(BigDecimal dividend) {
        this.dividend = dividend;
    }

    public BigDecimal getCurrencyGain() {
        return currencyGain;
    }

    public void setCurrencyGain(BigDecimal currencyGain) {
        this.currencyGain = currencyGain;
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(BigDecimal totalGain) {
        this.totalGain = totalGain;
    }

    public BigDecimal getTotalReturnPerc() {
        return totalReturnPerc;
    }

    public void setTotalReturn(BigDecimal totalReturnPerc) {
        this.totalReturnPerc = totalReturnPerc;
    }

}
