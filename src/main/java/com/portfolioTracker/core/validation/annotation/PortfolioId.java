package com.portfolioTracker.core.validation.annotation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Positive
public @interface PortfolioId {
    String message() default "should be of type Long, positive";
}
