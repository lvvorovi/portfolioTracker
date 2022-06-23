package com.portfolioTracker.model.dto.portfolioSummaryDto;

import com.portfolioTracker.model.dto.positionSummaryDto.PositionSummaryDto;
import com.portfolioTracker.model.dto.summaryTotalsDto.SummaryTotalsDto;
import com.portfolioTracker.core.validation.annotation.Currency;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@Data
public class PortfolioSummaryDto {

    @NotNull
    @NumberFormat
    @Positive
    private Long portfolioId;
    private String portfolioName;
    @NotBlank
    private String portfolioStrategy;
    @Currency
    private String portfolioCurrency;
    @NotNull
    private List<PositionSummaryDto> positionSummaryList;
    @NotNull
    private SummaryTotalsDto summaryTotals;

}
