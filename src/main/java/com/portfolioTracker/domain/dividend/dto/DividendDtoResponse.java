package com.portfolioTracker.domain.dividend.dto;

import com.portfolioTracker.domain.dto.eventType.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Data
@EqualsAndHashCode(callSuper = false)
public class DividendDtoResponse extends RepresentationModel<DividendDtoResponse> {

    @NotNull
    @NumberFormat
    @Positive
    private Long id;
    @NotBlank
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
    private String username;

}
