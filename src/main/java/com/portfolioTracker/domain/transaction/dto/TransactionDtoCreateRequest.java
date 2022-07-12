package com.portfolioTracker.domain.transaction.dto;

import com.portfolioTracker.domain.dto.event.eventType.EventType;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Validated
public class TransactionDtoCreateRequest {

    @NotBlank
    @Size(max = 50, message = "max 50 characters")
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
    private Long portfolioId;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;

}
