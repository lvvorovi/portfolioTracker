package portfolioTracker.dividend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;

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
    @Size(max = 36, min = 36, message = "must be exactly 36 characters")
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
    @Positive
    private BigDecimal amount;
    @NotNull
    private EventType type;
    @NotBlank
    @Size(max = 36, min = 36, message = "must be exactly 36 characters")
    private String portfolioId;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;
}
