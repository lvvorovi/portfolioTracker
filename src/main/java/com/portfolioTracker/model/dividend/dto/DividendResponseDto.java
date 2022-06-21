package com.portfolioTracker.model.dividend.dto;

import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.core.validation.annotation.AmountOfMoney;
import com.portfolioTracker.core.validation.annotation.Date;
import com.portfolioTracker.core.validation.annotation.Ticker;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class DividendResponseDto extends RepresentationModel<DividendResponseDto> {

    @NotNull
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
