package com.portfolioTracker.yahooModule.validation;

import com.portfolioTracker.yahooModule.validation.rule.YahooValidationRule;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Service
@Validated
public class YahooResponseValidationService {

    private final List<YahooValidationRule> yahooValidationRuleList;

    public YahooResponseValidationService(List<YahooValidationRule> yahooValidationRuleList) {
        this.yahooValidationRuleList = yahooValidationRuleList;
    }

    public void validate(@NotEmpty String responseString) {
        yahooValidationRuleList.forEach(rule -> rule.validate(responseString));
    }
}
