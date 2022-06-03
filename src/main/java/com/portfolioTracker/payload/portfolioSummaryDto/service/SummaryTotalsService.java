package com.portfolioTracker.payload.portfolioSummaryDto.service;

import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.summaryTotals.SummaryTotals;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummaryTotalsService {

    public SummaryTotals getReturnSummaryTotals(@NotNull List<PositionSummary> positionSummaries) {
        SummaryTotals summaryTotals = new SummaryTotals();
        positionSummaries = positionSummaries.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .collect(Collectors.toList());

        summaryTotals.setTotalBought(positionSummaries.stream()
                .map(PositionSummary::getTotalBough)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setTotalShares(positionSummaries.stream()
                .map(PositionSummary::getTotalShares)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCurrentValue(positionSummaries.stream()
                .map(PositionSummary::getCurrentValue)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCapitalGains(positionSummaries.stream()
                .map(PositionSummary::getCapitalGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setDividends(positionSummaries.stream()
                .map(PositionSummary::getDividend)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCommission(positionSummaries.stream()
                .map(PositionSummary::getCommission)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCurrencyGain(positionSummaries.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .map(PositionSummary::getCurrencyGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setTotalGain(positionSummaries.stream()
                .filter(summary -> summary.getTotalShares().compareTo(new BigDecimal(0)) > 0)
                .map(PositionSummary::getTotalGain)
                .reduce(new BigDecimal(0), BigDecimal::add));

        summaryTotals.setCapitalGainsPerc(summaryTotals.getCapitalGains()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setDividendsPerc(summaryTotals.getDividends()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setCommissionGainsPerc(summaryTotals.getCommission()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setCurrencyGainsPerc(summaryTotals.getCurrencyGain()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        summaryTotals.setTotalGainsPerc(summaryTotals.getTotalGain()
                .divide(summaryTotals.getTotalBought(), 4, RoundingMode.HALF_DOWN));

        return summaryTotals;
    }

}
