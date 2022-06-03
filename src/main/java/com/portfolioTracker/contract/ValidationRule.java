package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;

public interface ValidationRule<T> {

    void validate(@NotNull T t);

}
