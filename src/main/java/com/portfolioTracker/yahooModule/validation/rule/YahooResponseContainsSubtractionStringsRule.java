package com.portfolioTracker.yahooModule.validation.rule;

import com.portfolioTracker.yahooModule.config.YahooModuleConfig;
import com.portfolioTracker.yahooModule.validation.exception.YahooServiceLogicException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Component
public class YahooResponseContainsSubtractionStringsRule implements YahooValidationRule {

    private final YahooModuleConfig yahooConfig;

    public YahooResponseContainsSubtractionStringsRule(YahooModuleConfig yahooConfig) {
        this.yahooConfig = yahooConfig;
    }

    @Override
    public void validate(@NotBlank String responseString) {
        if (!responseString.contains(yahooConfig.getJsonExtractionStart())
                || !responseString.contains(yahooConfig.getJsonExtractionEnd())) {
            throw new YahooServiceLogicException("YahooService Logic does not work, extracting Json by key Strings "
                    + "'" + yahooConfig.getJsonExtractionStart() + "'" + " and/or " + "'" + yahooConfig.getJsonExtractionEnd() + "'");
        }


    }
}
