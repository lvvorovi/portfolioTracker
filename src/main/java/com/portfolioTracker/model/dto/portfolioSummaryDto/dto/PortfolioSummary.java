package com.portfolioTracker.model.dto.portfolioSummaryDto.dto;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.PositionSummary;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.summaryTotals.SummaryTotals;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.ModelName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
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

}
