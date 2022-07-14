package com.portfolioTracker.domain.dividend.dto;

import com.portfolioTracker.domain.dto.eventType.EventType;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
public class DividendDtoUpdateRequest {

    @NotNull
    @NumberFormat
    private Long id;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String ticker;
    @NotNull
    @PastOrPresent
    private LocalDate exDate;
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotNull
    @NumberFormat
    @Positive
    private BigDecimal amount;
    @NotNull
    private EventType type;
    @NotNull
    @NumberFormat
    @Positive
    private Long portfolioId;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;
}
