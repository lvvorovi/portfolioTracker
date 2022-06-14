package com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event;

import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Validated
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Event {

    private Long id;
    @Ticker
    private String ticker;
    @NotEmpty
    private EventType type;
    private LocalDate exDate;
    @Date
    private LocalDate date;
    private BigDecimal priceAmount;
    private BigDecimal shares;
    private BigDecimal bought;
    private BigDecimal sold;
    private BigDecimal dividend;
    private BigDecimal commission;

}
