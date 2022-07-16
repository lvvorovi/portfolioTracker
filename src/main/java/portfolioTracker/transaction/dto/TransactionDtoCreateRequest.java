package portfolioTracker.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import portfolioTracker.dto.eventType.EventType;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @NotBlank
    private String portfolioId;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;

}
