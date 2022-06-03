package com.portfolioTracker.payload.portfolioSummaryDto.dto.summaryTotals;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioTracker.contract.AmountOfMoney;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class SummaryTotals {

    @AmountOfMoney
    private BigDecimal totalBought;
    @PositiveOrZero
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGains;
    @PositiveOrZero
    private BigDecimal dividends;
    @AmountOfMoney
    private BigDecimal commission;
    @NotNull
    private BigDecimal currencyGain;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    @JsonProperty("capitalReturn %")
    private BigDecimal capitalGainsPerc;
    @NotNull
    @JsonProperty("commissionReturn %")
    private BigDecimal commissionGainsPerc;
    @NotNull
    @JsonProperty("currencyReturn %")
    private BigDecimal currencyGainsPerc;
    @NotNull
    @JsonProperty("dividendReturn %")
    private BigDecimal DividendsPerc;
    @NotNull
    @JsonProperty("totalReturn %")
    private BigDecimal totalGainsPerc;

    public BigDecimal getTotalBought() {
        return totalBought;
    }

    public void setTotalBought(BigDecimal totalBought) {
        this.totalBought = totalBought;
    }

    public BigDecimal getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(BigDecimal totalShares) {
        this.totalShares = totalShares;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getCapitalGains() {
        return capitalGains;
    }

    public void setCapitalGains(BigDecimal capitalGains) {
        this.capitalGains = capitalGains;
    }

    public BigDecimal getDividends() {
        return dividends;
    }

    public void setDividends(BigDecimal dividends) {
        this.dividends = dividends;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
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

    public BigDecimal getCapitalGainsPerc() {
        return capitalGainsPerc;
    }

    public void setCapitalGainsPerc(BigDecimal capitalGainsPerc) {
        this.capitalGainsPerc = capitalGainsPerc;
    }

    public BigDecimal getCommissionGainsPerc() {
        return commissionGainsPerc;
    }

    public void setCommissionGainsPerc(BigDecimal commissionGainsPerc) {
        this.commissionGainsPerc = commissionGainsPerc;
    }

    public BigDecimal getCurrencyGainsPerc() {
        return currencyGainsPerc;
    }

    public void setCurrencyGainsPerc(BigDecimal currencyGainsPerc) {
        this.currencyGainsPerc = currencyGainsPerc;
    }

    public BigDecimal getDividendsPerc() {
        return DividendsPerc;
    }

    public void setDividendsPerc(BigDecimal dividendsPerc) {
        DividendsPerc = dividendsPerc;
    }

    public BigDecimal getTotalGainsPerc() {
        return totalGainsPerc;
    }

    public void setTotalGainsPerc(BigDecimal totalGainsPerc) {
        this.totalGainsPerc = totalGainsPerc;
    }
}
