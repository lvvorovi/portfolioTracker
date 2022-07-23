package portfolioTracker.util;

import portfolioTracker.dto.currency.CurrencyRateDto;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyRateTestUtil extends TestUtil {

    public static String extractMessagesFromViolationSet(Set<ConstraintViolation<CurrencyRateDto>> violationSet) {
        return violationSet.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }


}
