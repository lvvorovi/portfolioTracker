package com.portfolioTracker.model.transaction.dto;

import com.portfolioTracker.validation.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
public class TransactionRequestDto {

    private Long id;
    @Ticker
    private String ticker;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal price;
    @Quantity
    private BigDecimal shares;
    @AmountOfMoney
    private BigDecimal commission;
    @NotNull
    private String type;
    @PortfolioId
    private Long portfolioId;

}
