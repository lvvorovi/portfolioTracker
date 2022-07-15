package portfolioTracker.dividend.validation.rule;

import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.exception.TickerNotSupportedDividendException;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dto.ticker.service.TickerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@AllArgsConstructor
@Priority(1)
public class TickerDividendValidationRule implements DividendValidationRule {

    private final TickerService tickerService;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        validateTicker(dto.getTicker());
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        validateTicker(dto.getTicker());
    }

    private void validateTicker(String ticker) {
        if (tickerService.isTickerSupported(ticker)) {
            return;
        }
        throw new TickerNotSupportedDividendException("Not supported ticker: " + ticker);
    }

}
