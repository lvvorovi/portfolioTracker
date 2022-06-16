package com.portfolioTracker.yahooModule.validation.annotation;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DateTimeFormat(pattern = "yyyy-MM-dd")
@PastOrPresent
public @interface Date {
    String message() default "Date should be in yyy-MM-dd format and be PastorPresent";
}
