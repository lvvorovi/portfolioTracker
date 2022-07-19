package portfolioTracker.core;

public class ExceptionErrors {

    public static final String NOT_BLANK_ERROR_MESSAGE = "must not be blank";
    public static final String NOT_NULL_ERROR_MESSAGE = "must not be null";
    public static final String PAST_OR_PRESENT_ERROR_MESSAGE = "must be a date in the past or in the present";
    public static final String GREATER_THAN_ZERO_ERROR_MESSAGE = "must be greater than 0";
    public static final String POSITIVE_OR_ZERO_ERROR_MESSAGE = "must be greater than or equal to 0";
    public static final String CURRENCY_TYPE_ERROR_MESSAGE = "Currency should be of type 'XXX'";
    public static final String ID_LENGTH_ERROR_MESSAGE = "must be exactly 36 characters";
    public static final String TICKER_MAX_LENGTH_ERROR_MESSAGE = "max 50 characters";
    public static final String USERNAME_MAX_LENGTH_ERROR_MESSAGE = "max 50 characters";
    public static final String PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE = "max 50 characters";
    public static final String PORTFOLIO_STRATEGY_MAX_LENGTH_ERROR_MESSAGE = "max 150 characters";

    public static final String DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE = "Dividend not found for id: ";
    public static final String DIVIDEND_NOT_FOUND_EXCEPTION_MESSAGE = "Dividend not found for: ";
    public static final String DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE = "Dividend event already registered in requested portfolio";

    public static final String PORTFOLIO_NOT_FOUND_EXCEPTION_MESSAGE = "Portfolio not found for: ";
    public static final String PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE = "Portfolio not found for id: ";
    public static final String PORTFOLIO_NAME_EXISTS_EXCEPTION_MESSAGE = "Portfolio name already exists: ";

    public static final String TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE = "Transaction not found for id: ";
    public static final String TRANSACTION_NOT_FOUND_EXCEPTION_MESSAGE = "Transaction not found for: " ;

    public static final String TICKER_NOT_SUPPORTED_EXCEPTION_MESSAGE = "Ticker is not supported : ";

    public static final String JSON_TYPE_STRING_EXCEPTION_MESSAGE = "String should be of type Json";

    public static final String EVENTDTO_CONVERTER_FAIL_EXCEPTION_MESSAGE = "EventDtoTypeConverter failed to convert DB data to an EventType value: ";

    public static final String IS_TICKER_SUPPORTED_CONNECTION_FAILURE_EXCEPTION_MESSAGE = ".isTickerSupported() caught exception while attempting connection to yahooMS";
    public static final String IS_TICKER_SUPPORTED_NULL_RESPONSE_EXCEPTION_MESSAGE = " returned null for method isTickerSupported()";

    public static final String CURRENCY_NOT_SUPPORTED_EXCEPTION_MESSAGE = "Not supported currency: ";

}
