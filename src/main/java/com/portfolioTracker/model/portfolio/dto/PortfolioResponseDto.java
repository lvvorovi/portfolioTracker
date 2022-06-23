package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.core.validation.annotation.Currency;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
public class PortfolioResponseDto extends RepresentationModel<PortfolioResponseDto> {

    @NotNull
    private Long id;
    private String name;
    @NotEmpty
    private String strategy;
    @Currency
    private String currency;
    @NotNull
    private List<TransactionResponseDto> transactionList;
    @NotNull
    private List<DividendResponseDto> dividendList;
}
