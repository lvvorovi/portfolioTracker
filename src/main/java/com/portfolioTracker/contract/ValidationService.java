package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;

public interface ValidationService<T> {

    void validate(@NotNull T t);

}
