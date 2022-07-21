package portfolioTracker.transaction.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dto.ticker.service.TickerService;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.validation.exception.TransactionException;
import portfolioTracker.transaction.validation.rule.createRequest.TransactionCreateValidationRule;
import portfolioTracker.transaction.validation.rule.updateRequest.TransactionUpdateValidationRule;

import javax.annotation.Priority;

import static portfolioTracker.core.ExceptionErrors.TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
@Priority(100)
public class TickerTransactionValidationRule implements TransactionUpdateValidationRule, TransactionCreateValidationRule {

    private final TickerService tickerService;

    @Override
    public void validate(TransactionDtoCreateRequest dtoRequest) {
        validateTicker(dtoRequest.getTicker());
    }

    @Override
    public void validate(TransactionDtoUpdateRequest dtoRequest) {
        validateTicker(dtoRequest.getTicker());
    }

    private void validateTicker(String ticker) {
        if (!tickerService.isTickerSupported(ticker))
            throw new TransactionException(TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE + ticker);

    }
}
