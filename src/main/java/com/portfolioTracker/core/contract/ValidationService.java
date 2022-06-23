package com.portfolioTracker.core.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ValidationService<T> {

    void validate(@NotNull T t);

}
