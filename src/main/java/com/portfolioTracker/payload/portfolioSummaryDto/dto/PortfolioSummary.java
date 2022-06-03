package com.portfolioTracker.payload.portfolioSummaryDto.dto;

import com.portfolioTracker.contract.Currency;
import com.portfolioTracker.contract.ModelName;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.summaryTotals.SummaryTotals;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PortfolioSummary {

    @NotNull
    private Long portfolioId;
    @ModelName
    private String portfolioName;
    @NotEmpty
    private String portfolioStrategy;
    @Currency
    private String portfolioCurrency;
    @NotNull
    private List<PositionSummary> positionSummaryList;
    @NotNull
    private SummaryTotals summaryTotals;

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public String getPortfolioStrategy() {
        return portfolioStrategy;
    }

    public void setPortfolioStrategy(String portfolioStrategy) {
        this.portfolioStrategy = portfolioStrategy;
    }

    public String getPortfolioCurrency() {
        return portfolioCurrency;
    }

    public void setPortfolioCurrency(String portfolioCurrency) {
        this.portfolioCurrency = portfolioCurrency;
    }

    public List<PositionSummary> getPositionSummaryList() {
        return positionSummaryList;
    }

    public void setPositionSummaryList(List<PositionSummary> positionSummaryList) {
        this.positionSummaryList = positionSummaryList;
    }

    public SummaryTotals getSummaryTotals() {
        return summaryTotals;
    }

    public void setSummaryTotals(SummaryTotals summaryTotals) {
        this.summaryTotals = summaryTotals;
    }
}
