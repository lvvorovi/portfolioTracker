package com.portfolioTracker.model.transaction.dto;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Quantity;
import com.portfolioTracker.validation.annotation.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Validated
public class TransactionResponseDto extends RepresentationModel<TransactionResponseDto> {

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
    @NotEmpty
    private EventType type;
    @NotNull
    private Long portfolioId;
    private BigDecimal bought;
    private BigDecimal sold;
}
