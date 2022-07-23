package portfolioTracker.dto.currency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import portfolioTracker.util.CurrencyRateTestUtil;


import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static portfolioTracker.util.CurrencyRateTestUtil.extractMessagesFromViolationSet;
import static portfolioTracker.util.TestUtil.*;

@SpringBootTest
class CurrencyRateDtoTest {

    private final CurrencyRateDto dto = new CurrencyRateDto();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstrainViolationSet() {
        var violationSet = validator.validate(dto);

        String errorMessages = extractMessagesFromViolationSet(violationSet);
        assertThat(errorMessages).contains(portfolioCurrencyTypeTestErrorMessage);
        assertThat(errorMessages).contains(eventCurrencyTypeTestErrorMessage);
        assertThat(errorMessages).contains(dateNullTestErrorMessage);
//        assertThat(errorMessages).contains();
    }
}