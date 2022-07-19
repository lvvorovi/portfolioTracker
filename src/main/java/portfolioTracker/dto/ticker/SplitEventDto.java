package portfolioTracker.dto.ticker;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

import static portfolioTracker.core.ExceptionErrors.*;

@Validated
@Data
public class SplitEventDto {

    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    private BigDecimal amount;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    private LocalDate date;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    private String type;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    private String data;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    private BigDecimal numerator;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    private BigDecimal denominator;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    private String splitRatio;

}
