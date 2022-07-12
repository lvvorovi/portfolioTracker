package com.portfolioTracker.domain.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
public class PortfolioDtoResponse extends RepresentationModel<PortfolioDtoResponse> {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String strategy;
    @Currency
    private String currency;
    @NotBlank
    private String username;
    @JsonIgnore
    private List<TransactionDtoResponse> transactionList;
    @JsonIgnore
    private List<DividendDtoResponse> dividendList;
}
