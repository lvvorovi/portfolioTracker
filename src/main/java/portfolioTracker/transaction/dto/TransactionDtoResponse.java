package portfolioTracker.transaction.dto;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoResponse extends RepresentationModel<TransactionDtoResponse> {

    @NotBlank
    private String id;
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
    @NotBlank
    private String portfolioId;
    @NotBlank
    private String username;
}
