package portfolioTracker.core.validation.annotation;


import portfolioTracker.core.validation.validator.CurrencyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Currency {
    String message() default "currencyTypeErrorMessage";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
