package com.portfolioTracker.externalApi.yahoo.validation;

import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.externalApi.yahoo.validation.rule.YahooValidationRule;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class YahooResponseValidationService implements ValidationService<HttpResponse<String>> {

    private final List<YahooValidationRule> yahooValidationRuleList;

    public YahooResponseValidationService(List<YahooValidationRule> yahooValidationRuleList) {
        this.yahooValidationRuleList = yahooValidationRuleList;
    }

    @Override
    public void validate(@NotNull HttpResponse<String> stringHttpResponse) {
        yahooValidationRuleList.forEach(rule -> rule.validate(stringHttpResponse));
    }
}
