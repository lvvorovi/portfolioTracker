package com.portfolioTracker.externalApi.yahoo.validation.rule;

import com.portfolioTracker.externalApi.yahoo.validation.exception.YahooServiceLogicException;

import javax.validation.constraints.NotNull;
import java.net.http.HttpResponse;

public class YahooResponseContainsLogicKeyJsonSubstractionStrings implements YahooValidationRule{

    private static final String TICKER_ROOT_APP_MAIN_JSON_START = "root.App.main = ";
    private static final String TICKER_ROOT_APP_MAIN_JSON_END = "}(this));";

    @Override
    public void validate(@NotNull HttpResponse<String> stringHttpResponse) {
        if (!stringHttpResponse.body().contains(TICKER_ROOT_APP_MAIN_JSON_START)
        || !stringHttpResponse.body().contains(TICKER_ROOT_APP_MAIN_JSON_END)) {
            throw new YahooServiceLogicException("YahooService Logic does not work, exctacting Json by key Strings "
            + "'" + TICKER_ROOT_APP_MAIN_JSON_START + "'" + " and " + "'" + TICKER_ROOT_APP_MAIN_JSON_END + "'");
        }


    }
}
