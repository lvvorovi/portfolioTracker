package portfolioTracker.transaction.validation.exception;

public class TickerNotSupportedTransactionException extends TransactionException {

    public TickerNotSupportedTransactionException(String message) {
        super(message);
    }
}
