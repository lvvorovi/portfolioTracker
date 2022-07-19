package portfolioTracker.util;

import static portfolioTracker.core.ExceptionErrors.*;

public class TestUtil {

    public static final String username = "user";
    public static final String id = "id";
    public static final String portfolioId = "portfolioId";

    public static final String exactly3CharactersString = "123";
    public static final String exactly36CharactersString = "ThisStringIs_36_CharactersLongString";
    public static final String exactly50CharactersString = "ThisStringIs_50_CharactersLongThisStringIs_50_____";
    public static final String exactly150CharactersString = exactly50CharactersString +
            exactly50CharactersString + exactly50CharactersString;

    public static final String amountNullTestErrorMessage = "amount: " + NOT_NULL_ERROR_MESSAGE;
    public static final String amountGreaterThenZeroTestErrorMessage = "amount: " + GREATER_THAN_ZERO_ERROR_MESSAGE;

    public static final String currencyTypeTestErrorMessage = "currency: " + CURRENCY_TYPE_ERROR_MESSAGE;

    public static final String dateNullTestErrorMessage = "date: " + NOT_NULL_ERROR_MESSAGE;
    public static final String datePastOrPresentTestErrorMessage = "date: " + PAST_OR_PRESENT_ERROR_MESSAGE;

    public static final String exDateNullTestErrorMessage = "exDate: " + NOT_NULL_ERROR_MESSAGE;
    public static final String exDatePastOrPresentTestErrorMessage = "exDate: " + PAST_OR_PRESENT_ERROR_MESSAGE;

    public static final String idLengthTestErrorMessage = "id: " + ID_LENGTH_ERROR_MESSAGE;
    public static final String idBlankTestErrorMessage = "id: " + NOT_BLANK_ERROR_MESSAGE;

    public static final String nameBlankTestErrorMessage = "name: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String nameMaxLengthTestErrorMessage = "name: " + PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE;

    public static final String portfolioIdLengthTestErrorMessage = "portfolioId: " + ID_LENGTH_ERROR_MESSAGE;
    public static final String portfolioIdBlankTestErrorMessage = "portfolioId: " + NOT_BLANK_ERROR_MESSAGE;

    public static final String strategyBlankTestErrorMessage = "strategy: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String strategyMaxLengthTestErrorMessage = "strategy: " + PORTFOLIO_STRATEGY_MAX_LENGTH_ERROR_MESSAGE;

    public static final String tickerBlankTestErrorMessage = "ticker: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String tickerLengthTestErrorMessage = "ticker: " + TICKER_MAX_LENGTH_ERROR_MESSAGE;

    public static final String typeNullTestErrorMessage = "type: " + NOT_NULL_ERROR_MESSAGE;

    public static final String usernameBlankTestErrorMessage = "username: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String usernameMaxLengthTestErrorMessage = "username: " + USERNAME_MAX_LENGTH_ERROR_MESSAGE;


}
