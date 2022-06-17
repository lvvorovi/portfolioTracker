package com.portfolioTracker.model.dto.portfolioSummaryDto;

import com.portfolioTracker.model.dto.positionSummaryDto.PositionSummaryDto;
import com.portfolioTracker.model.dto.position.PositionDto;
import com.portfolioTracker.model.dto.summaryTotalsDto.SummaryTotalsDto;
import com.portfolioTracker.model.dto.position.PositionDtoService;
import com.portfolioTracker.model.dto.positionSummaryDto.PositionSummaryDtoService;
import com.portfolioTracker.model.dto.summaryTotalsDto.SummaryTotalsDtoService;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
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
        PortfolioResponseDto portfolio = portfolioService.findById(portfolioId);
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
