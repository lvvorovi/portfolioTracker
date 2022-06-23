package com.portfolioTracker.yahooModule.validation.rule;

import com.portfolioTracker.yahooModule.validation.exception.YahooServiceLogicException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Component
public class YahooResponseContainsSubtractionStringsRule implements YahooValidationRule {

    private static final String TICKER_ROOT_APP_MAIN_JSON_START = "root.App.main = ";
    private static final String TICKER_ROOT_APP_MAIN_JSON_END = "}(this));";

    public void validate(@NotEmpty String responseString) {
        if (!responseString.contains(TICKER_ROOT_APP_MAIN_JSON_START)
                || !responseString.contains(TICKER_ROOT_APP_MAIN_JSON_END)) {
            throw new YahooServiceLogicException("YahooService Logic does not work, extracting Json by key Strings "
                    + "'" + TICKER_ROOT_APP_MAIN_JSON_START + "'" + " and/or " + "'" + TICKER_ROOT_APP_MAIN_JSON_END + "'");
        }


    }
}
