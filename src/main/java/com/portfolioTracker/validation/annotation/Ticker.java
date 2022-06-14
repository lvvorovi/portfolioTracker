package com.portfolioTracker.validation.annotation;

import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
public @interface Ticker {
    String message() default "shall not be blank";
}
