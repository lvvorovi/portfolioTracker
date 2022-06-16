package com.portfolioTracker.yahooModule.validation.annotation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Size(min = 3, max = 3)
public @interface Currency {
    String message() default "String should be of type 'XXX'";
}
