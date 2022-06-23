package com.portfolioTracker.model.dto.portfolioSummaryDto;

import com.portfolioTracker.model.dto.positionSummaryDto.PositionSummaryDto;
import com.portfolioTracker.model.dto.summaryTotalsDto.SummaryTotalsDto;
import com.portfolioTracker.core.validation.annotation.Currency;
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
public class PortfolioSummaryDto {

    @NotNull
    private Long portfolioId;
    private String portfolioName;
    @NotEmpty
    private String portfolioStrategy;
    @Currency
    private String portfolioCurrency;
    @NotNull
    private List<PositionSummaryDto> positionSummaryList;
    @NotNull
    private SummaryTotalsDto summaryTotals;

}
