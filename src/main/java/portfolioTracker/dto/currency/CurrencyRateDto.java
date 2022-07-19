package portfolioTracker.dto.currency;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.validation.annotation.Currency;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

import static portfolioTracker.core.ExceptionErrors.*;

@Validated
@Data
public class CurrencyRateDto {

    @Currency(message = CURRENCY_TYPE_ERROR_MESSAGE)
    String portfolioCurrency;
    @Currency(message = CURRENCY_TYPE_ERROR_MESSAGE)
    String eventCurrency;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @PastOrPresent(message = PAST_OR_PRESENT_ERROR_MESSAGE)
    LocalDate date;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    BigDecimal rateClientSells;
    @NotNull(message = NOT_NULL_ERROR_MESSAGE)
    @Positive(message = GREATER_THAN_ZERO_ERROR_MESSAGE)
    BigDecimal rateClientBuys;

}
