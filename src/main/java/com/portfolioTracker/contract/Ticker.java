package com.portfolioTracker.contract;

import javax.validation.constraints.NotBlank;

@NotBlank(message = "must be present")
public @interface Ticker {
}
