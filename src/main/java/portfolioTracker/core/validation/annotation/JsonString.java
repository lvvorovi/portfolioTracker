package portfolioTracker.core.validation.annotation;

import portfolioTracker.core.validation.validator.JsonStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static portfolioTracker.core.ExceptionErrors.JSON_TYPE_STRING_EXCEPTION_MESSAGE;

@Documented
@Constraint(validatedBy = JsonStringValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonString {
    String message() default JSON_TYPE_STRING_EXCEPTION_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
