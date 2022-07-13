/*
package com.portfolioTracker.summaryModule.portfolioSummaryDto;

import com.portfolioTracker.summaryModule.position.PositionDto;
import com.portfolioTracker.summaryModule.position.PositionDtoService;
import com.portfolioTracker.summaryModule.positionSummaryDto.PositionSummaryDto;
import com.portfolioTracker.summaryModule.positionSummaryDto.PositionSummaryDtoService;
import com.portfolioTracker.summaryModule.summaryTotalsDto.SummaryTotalsDto;
import com.portfolioTracker.summaryModule.summaryTotalsDto.SummaryTotalsDtoService;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.service.PortfolioService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class PortfolioSummaryDtoService {


    private final PortfolioService portfolioService;
    private final PositionSummaryDtoService positionSummarService;
    private final PositionDtoService positionService;
    private final SummaryTotalsDtoService summaryTotalsService;

    public PortfolioSummaryDtoService(PortfolioService portfolioService, PositionSummaryDtoService positionSummarService, PositionDtoService positionService, SummaryTotalsDtoService summaryTotalsService) {
        this.portfolioService = portfolioService;
        this.positionSummarService = positionSummarService;
        this.positionService = positionService;
        this.summaryTotalsService = summaryTotalsService;
    }

    public PortfolioSummaryDto getSummaryByPortfolioId(@NumberFormat Long portfolioId) {
        PortfolioDtoResponse portfolio = portfolioService.findById(portfolioId);
        List<PositionDto> positionList = positionService.getPositionList(portfolio);
        List<PositionSummaryDto> positionSummaryList =
                positionSummarService.getPositionSummaryList(positionList);
        SummaryTotalsDto summaryTotals = summaryTotalsService.getSummaryTotals(positionSummaryList);

        PortfolioSummaryDto portfolioSummary = new PortfolioSummaryDto();

        portfolioSummary.setPortfolioId(portfolio.getId());
        portfolioSummary.setPortfolioName(portfolio.getName());
        portfolioSummary.setPortfolioCurrency(portfolio.getCurrency());
        portfolioSummary.setPortfolioStrategy(portfolio.getStrategy());
        portfolioSummary.setPositionSummaryList(positionSummaryList);
        portfolioSummary.setSummaryTotals(summaryTotals);

        return portfolioSummary;
    }

}
*/
