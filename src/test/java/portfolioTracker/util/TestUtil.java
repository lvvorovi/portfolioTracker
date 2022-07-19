package portfolioTracker.util;

import static portfolioTracker.core.ExceptionErrors.*;

public class TestUtil {

    public static final String username = "user";
    public static final String id = "id";
    public static final String portfolioId = "portfolioId";

    public static final String exactly50CharactersString = "ThisStringIs_50_CharactersLongThisStringIs_50_Char";
    public static final String exactly36CharactersString = "ThisStringIs_36_CharactersLongString";

    public static final String amountNullTestErrorMessage = "amount: " + NOT_NULL_ERROR_MESSAGE;
    public static final String amountGreaterThenZeroTestErrorMessage = "amount: " + GREATER_THAN_ZERO_ERROR_MESSAGE;
    public static final String dateNullTestErrorMessage = "date: " + NOT_NULL_ERROR_MESSAGE;
    public static final String datePastOrPresentTestErrorMessage = "date: " + PAST_OR_PRESENT_ERROR_MESSAGE;
    public static final String exDateNullTestErrorMessage = "exDate: " + NOT_NULL_ERROR_MESSAGE;
    public static final String exDatePastOrPresentTestErrorMessage = "exDate: " + PAST_OR_PRESENT_ERROR_MESSAGE;
    public static final String idLengthTestErrorMessage = "id: " + ID_LENGTH_ERROR_MESSAGE;
    public static final String idBlankTestErrorMessage = "id: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String portfolioLengthTestErrorMessage = "portfolioId: " + idLengthTestErrorMessage;
    public static final String portfolioIdBlankTestErrorMessage = "portfolioId: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String tickerBlankTestErrorMessage = "ticker: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String typeNullTestErrorMessage = "type: " + NOT_NULL_ERROR_MESSAGE;
    public static final String tickerLengthTestErrorMessage = "ticker: " + TICKER_MAX_LENGTH_ERROR_MESSAGE;
    public static final String usernameBlankTestErrorMessage = "username: " + NOT_BLANK_ERROR_MESSAGE;
    public static final String usernameLengthTestErrorMessage = "username: " + USERNAME_MAX_LENGTH_ERROR_MESSAGE;


}
