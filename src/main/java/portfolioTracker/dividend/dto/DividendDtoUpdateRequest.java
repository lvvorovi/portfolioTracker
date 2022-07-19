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

import static portfolioTracker.core.ExceptionErrors.*;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendDtoUpdateRequest {

    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 36, min = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = TICKER_MAX_LENGTH_ERROR_MESSAGE)
    private String ticker;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    private LocalDate exDate;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    private LocalDate date;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    private BigDecimal amount;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    private EventType type;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 36, min = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String portfolioId;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    private String username;
}
