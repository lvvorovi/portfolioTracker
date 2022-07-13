package com.portfolioTracker.summaryModule.event;

import com.portfolioTracker.summaryModule.event.eventType.EventType;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
public class EventDtoSplitAndDividend {

    private Long id;
    @NotBlank
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
