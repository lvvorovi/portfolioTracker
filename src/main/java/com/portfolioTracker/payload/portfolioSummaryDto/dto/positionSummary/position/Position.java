package com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portfolioTracker.contract.AmountOfMoney;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Position {

    @NotEmpty
    private String name;
    @NotNull
    private List<Event> eventList;
    @NotNull
    private BigDecimal netOriginalCosts;
    @PositiveOrZero
    private BigDecimal totalShares;
    @AmountOfMoney
    private BigDecimal currentSharePrice;
    @NotNull
    private BigDecimal currentValue;
    @NotNull
    private BigDecimal capitalGain;
    @NotNull
    private BigDecimal dividend;
    @NotNull
    private BigDecimal currencyGain;
    @PositiveOrZero
    private BigDecimal commission;
    @NotNull
    private BigDecimal totalGain;
    @NotNull
    @JsonProperty("capitalReturn_%")
    private BigDecimal capitalReturn;
    @NotNull
    @JsonProperty("dividendReturn_%")
    private BigDecimal dividendReturn;
    @NotNull
    @JsonProperty("currencyReturn_%")
    private BigDecimal currencyReturn;
    @NotNull
    @JsonProperty("commissionReturn_%")
    private BigDecimal commissionReturn;
    @NotNull
    @JsonProperty("totalReturn_%")
    private BigDecimal totalReturn;
    @NotNull
    @JsonIgnore
    private BigDecimal totalBought;
    @NotNull
    @JsonIgnore
    private BigDecimal totalSold;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public BigDecimal getNetOriginalCosts() {
        return netOriginalCosts;
    }

    public void setNetOriginalCosts(BigDecimal netOriginalCosts) {
        this.netOriginalCosts = netOriginalCosts;
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

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(BigDecimal totalGain) {
        this.totalGain = totalGain;
    }

    public BigDecimal getCapitalReturn() {
        return capitalReturn;
    }

    public void setCapitalReturn(BigDecimal capitalReturn) {
        this.capitalReturn = capitalReturn;
    }

    public BigDecimal getDividendReturn() {
        return dividendReturn;
    }

    public void setDividendReturn(BigDecimal dividendReturn) {
        this.dividendReturn = dividendReturn;
    }

    public BigDecimal getCurrencyReturn() {
        return currencyReturn;
    }

    public void setCurrencyReturn(BigDecimal currencyReturn) {
        this.currencyReturn = currencyReturn;
    }

    public BigDecimal getCommissionReturn() {
        return commissionReturn;
    }

    public void setCommissionReturn(BigDecimal commissionReturn) {
        this.commissionReturn = commissionReturn;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getTotalBought() {
        return totalBought;
    }

    public void setTotalBought(BigDecimal totalBought) {
        this.totalBought = totalBought;
    }

    public BigDecimal getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(BigDecimal totalSold) {
        this.totalSold = totalSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(name, position.name) && Objects.equals(eventList, position.eventList) && Objects.equals(netOriginalCosts, position.netOriginalCosts) && Objects.equals(totalShares, position.totalShares) && Objects.equals(currentSharePrice, position.currentSharePrice) && Objects.equals(currentValue, position.currentValue) && Objects.equals(capitalGain, position.capitalGain) && Objects.equals(dividend, position.dividend) && Objects.equals(currencyGain, position.currencyGain) && Objects.equals(commission, position.commission) && Objects.equals(totalGain, position.totalGain) && Objects.equals(capitalReturn, position.capitalReturn) && Objects.equals(dividendReturn, position.dividendReturn) && Objects.equals(currencyReturn, position.currencyReturn) && Objects.equals(commissionReturn, position.commissionReturn) && Objects.equals(totalReturn, position.totalReturn) && Objects.equals(totalBought, position.totalBought) && Objects.equals(totalSold, position.totalSold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventList, netOriginalCosts, totalShares, currentSharePrice, currentValue, capitalGain, dividend, currencyGain, commission, totalGain, capitalReturn, dividendReturn, currencyReturn, commissionReturn, totalReturn, totalBought, totalSold);
    }

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                ", eventList=" + eventList +
                ", netOriginalCosts=" + netOriginalCosts +
                ", totalShares=" + totalShares +
                ", currentSharePrice=" + currentSharePrice +
                ", currentValue=" + currentValue +
                ", capitalGain=" + capitalGain +
                ", dividend=" + dividend +
                ", currencyGain=" + currencyGain +
                ", commission=" + commission +
                ", totalGain=" + totalGain +
                ", capitalReturn=" + capitalReturn +
                ", dividendReturn=" + dividendReturn +
                ", currencyReturn=" + currencyReturn +
                ", commissionReturn=" + commissionReturn +
                ", totalReturn=" + totalReturn +
                ", totalBought=" + totalBought +
                ", totalSold=" + totalSold +
                '}';
    }
}
