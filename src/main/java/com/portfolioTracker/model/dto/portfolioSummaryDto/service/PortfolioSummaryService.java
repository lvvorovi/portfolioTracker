package com.portfolioTracker.model.dto.portfolioSummaryDto.service;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.PortfolioSummary;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.Position;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.summaryTotals.SummaryTotals;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class PortfolioSummaryService {


    private final PortfolioService portfolioService;
    private final PositionSummaryService positionSummaryService;
    private final PositionService positionService;
    private final SummaryTotalsService summaryTotalsService;

    public PortfolioSummaryService(PortfolioService portfolioService, PositionSummaryService positionSummaryService, PositionService positionService, SummaryTotalsService summaryTotalsService) {
        this.portfolioService = portfolioService;
        this.positionSummaryService = positionSummaryService;
        this.positionService = positionService;
        this.summaryTotalsService = summaryTotalsService;
    }

    public PortfolioSummary buildByPortfolioId(@NumberFormat Long portfolioId) {
        PortfolioResponseDto portfolio = portfolioService.findById(portfolioId);
        List<Position> positionList = positionService.getPositions(portfolio);
        List<PositionSummary> positionSummaryList = positionSummaryService.getSummaryList(positionList);
        SummaryTotals summaryTotals = summaryTotalsService.getReturnSummaryTotals(positionSummaryList);

        PortfolioSummary summaryDto = new PortfolioSummary();

        summaryDto.setPortfolioId(portfolio.getId());
        summaryDto.setPortfolioName(portfolio.getName());
        summaryDto.setPortfolioCurrency(portfolio.getCurrency());
        summaryDto.setPortfolioStrategy(portfolio.getStrategy());
        summaryDto.setPositionSummaryList(positionSummaryList);
        summaryDto.setSummaryTotals(summaryTotals);

        return summaryDto;
    }

}
