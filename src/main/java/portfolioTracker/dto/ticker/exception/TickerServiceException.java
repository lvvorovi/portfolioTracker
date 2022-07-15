package portfolioTracker.dto.ticker.exception;

import portfolioTracker.core.validation.ValidationException;

public class TickerServiceException extends ValidationException {

    public TickerServiceException(String message) {
        super(message);
    }
}
