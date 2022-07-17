package portfolioTracker.core.validation.validator;

import org.json.JSONException;
import org.json.JSONObject;
import portfolioTracker.core.validation.annotation.JsonString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonStringValidator implements ConstraintValidator<JsonString, String> {

    @Override
    public void initialize(JsonString constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

}
