package com.portfolioTracker.contract;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;

@DateTimeFormat(pattern = "yyyy-MM-dd")
@PastOrPresent
public @interface Date {
}
