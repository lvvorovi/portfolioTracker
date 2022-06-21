package com.portfolioTracker.core.validation.validator;

import com.portfolioTracker.core.validation.annotation.Currency;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {

    @Override
    public void initialize(Currency constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        if (value.isEmpty()) return false;
        if (value.length() != 3) return false;
        return true;
    }
}
