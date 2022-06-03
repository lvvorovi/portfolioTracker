package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NotNull
@Size(min = 3, max = 3)
public @interface Currency {
}
