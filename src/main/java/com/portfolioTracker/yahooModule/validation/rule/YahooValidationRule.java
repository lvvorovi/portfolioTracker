package com.portfolioTracker.yahooModule.validation.rule;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public interface YahooValidationRule {

    void validate(@NotBlank String responseString);

}
