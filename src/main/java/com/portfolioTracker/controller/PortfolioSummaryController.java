package com.portfolioTracker.controller;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.PortfolioSummary;
import com.portfolioTracker.model.dto.portfolioSummaryDto.service.PortfolioSummaryService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/portfolio/summary")
public class PortfolioSummaryController {

    private final PortfolioSummaryService portfolioSummaryService;

    public PortfolioSummaryController(PortfolioSummaryService portfolioSummaryService) {
        this.portfolioSummaryService = portfolioSummaryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioSummary> getSummaryByPortfolioId(@NumberFormat @PathVariable Long id) {
        return ResponseEntity.ok(portfolioSummaryService.buildByPortfolioId(id));
    }


}
