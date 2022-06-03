package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NotNull
@Positive(message = "value must be positive")
public @interface Quantity {
}
