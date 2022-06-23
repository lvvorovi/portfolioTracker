package com.portfolioTracker.controller;

import com.portfolioTracker.model.dto.portfolioSummaryDto.PortfolioSummaryDto;
import com.portfolioTracker.model.dto.portfolioSummaryDto.PortfolioSummaryDtoService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/portfolios/summary")
public class PortfolioSummaryController {

    private final PortfolioSummaryDtoService portfolioSummaryDtoService;

    public PortfolioSummaryController(PortfolioSummaryDtoService portfolioSummaryDtoService) {
        this.portfolioSummaryDtoService = portfolioSummaryDtoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioSummaryDto> getSummaryByPortfolioId(@NumberFormat @PathVariable Long id) {
        return ResponseEntity.ok(portfolioSummaryDtoService.getSummaryByPortfolioId(id));
    }


}
