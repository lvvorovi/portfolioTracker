package com.portfolioTracker.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ValidationRule<T> {

    void validate(@NotNull T t);

}
