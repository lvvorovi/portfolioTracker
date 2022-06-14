package com.portfolioTracker.validation.annotation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Positive
public @interface AmountOfMoney {
    String message() default "Value should be positive numeric";
}
