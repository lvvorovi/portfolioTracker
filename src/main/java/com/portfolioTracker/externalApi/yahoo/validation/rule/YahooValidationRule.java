package com.portfolioTracker.externalApi.yahoo.validation.rule;

import com.portfolioTracker.contract.ValidationRule;

import java.net.http.HttpResponse;

public interface YahooValidationRule extends ValidationRule<HttpResponse<String>> {
}
