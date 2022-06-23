package com.portfolioTracker.model.dto.event;

import com.portfolioTracker.model.dto.event.eventType.EventType;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
public class EventDto {

    private Long id;
    private String ticker;
    @NotNull
    private EventType type;
    private LocalDate exDate;
    private LocalDate date;
    private BigDecimal priceAmount;
    private BigDecimal shares;
    private BigDecimal bought;
    private BigDecimal sold;
    private BigDecimal dividend;
    private BigDecimal commission;

}
