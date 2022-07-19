package portfolioTracker.core.validation.validator;

import portfolioTracker.core.validation.annotation.Currency;

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
        if (value.isBlank()) return false;
        return value.length() == 3;
    }
}
