package com.portfolioTracker.model.dividend.dto;

import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DividendRequestDto {

    private Long id;
    @Ticker
    private String ticker;
    @Date
    private LocalDate exDate;
    @Date
    private LocalDate date;
    @AmountOfMoney
    private BigDecimal amount;
    @NotNull
    private EventType type;
}
