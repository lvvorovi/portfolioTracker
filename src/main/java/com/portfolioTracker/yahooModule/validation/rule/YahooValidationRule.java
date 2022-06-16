package com.portfolioTracker.yahooModule.validation.rule;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public interface YahooValidationRule {

    void validate(@NotEmpty String responseString);

}
