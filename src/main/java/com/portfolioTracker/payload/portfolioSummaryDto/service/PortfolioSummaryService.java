package com.portfolioTracker.payload.portfolioSummaryDto.service;

import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.PortfolioSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.Position;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.summaryTotals.SummaryTotals;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
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

    public PortfolioSummary buildByPortfolioId(@NotNull Long portfolioId) {
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
