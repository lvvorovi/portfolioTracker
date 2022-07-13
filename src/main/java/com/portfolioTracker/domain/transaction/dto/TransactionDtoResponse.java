package com.portfolioTracker.domain.transaction.dto;

import com.portfolioTracker.summaryModule.event.eventType.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
public class TransactionDtoResponse extends RepresentationModel<TransactionDtoResponse> {

    @NotNull
    @NumberFormat
    @Positive
    private Long id;
    @NotBlank
    private String ticker;
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal price;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal shares;
    @NotNull
    @NumberFormat
    @PositiveOrZero
    private BigDecimal commission;
    @NotNull
    private EventType type;
    @NotNull
    @NumberFormat
    @Positive
    private Long portfolioId;
    @NotBlank
    private String username;
}
