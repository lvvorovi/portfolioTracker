package portfolioTracker.transaction.validation.exception;

import portfolioTracker.core.validation.ValidationException;

public class TransactionException extends ValidationException {

    public TransactionException(String message) {
        super(message);
    }
}
