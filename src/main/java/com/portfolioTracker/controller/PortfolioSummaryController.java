package com.portfolioTracker.controller;

import com.portfolioTracker.payload.portfolioSummaryDto.dto.PortfolioSummary;
import com.portfolioTracker.payload.portfolioSummaryDto.service.PortfolioSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/portfolio/summary")
public class PortfolioSummaryController {

    private final PortfolioSummaryService portfolioSummaryService;

    public PortfolioSummaryController(PortfolioSummaryService portfolioSummaryService) {
        this.portfolioSummaryService = portfolioSummaryService;
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioSummary> buildSummaryById(@NotNull @PathVariable Long portfolioId) {
        return ResponseEntity.ok(portfolioSummaryService.buildByPortfolioId(portfolioId));
    }


}
