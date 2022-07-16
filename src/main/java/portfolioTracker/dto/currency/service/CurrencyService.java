package portfolioTracker.dto.currency.service;

import portfolioTracker.dto.currency.CurrencyRateDto;
import portfolioTracker.core.validation.annotation.Currency;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Validated
public interface CurrencyService {

    Boolean isCurrencySupported(@Currency String currency);

    CurrencyRateDto getRateForCurrencyPairOnDate(@Currency String currencyFrom, @Currency String currencyTo, @PastOrPresent LocalDate onDate);

}