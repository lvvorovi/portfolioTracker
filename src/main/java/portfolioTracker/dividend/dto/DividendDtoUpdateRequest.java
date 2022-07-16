package portfolioTracker.dividend.dto;

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

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendDtoUpdateRequest {

    @NotBlank
    @Size(max = 60, message = "max 60 characters")
    private String id;
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
    @NotBlank
    @Size(max = 60, message = "max 60 characters")
    private String portfolioId;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;
}
