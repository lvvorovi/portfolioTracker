package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.core.validation.annotation.ModelName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Validated
public class PortfolioResponseDto extends RepresentationModel<PortfolioResponseDto> {

    @NotNull
    private Long id;
    @ModelName
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
