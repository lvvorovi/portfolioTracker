package portfolioTracker.dividend.domain;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static portfolioTracker.util.DividendTestUtil.*;

@SpringBootTest
class DividendEntityTest {

    private final DividendEntity entity = new DividendEntity();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsAreNull_thenConstraintViolationSet() {
        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validate(entity);

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerBlankErrorMessage);
        assertThat(errorMessages).contains(exDateNullErrorMessage);
        assertThat(errorMessages).contains(dateNullErrorMessage);
        assertThat(errorMessages).contains(amountNullErrorMessage);
        assertThat(errorMessages).contains(typeNullErrorMessage);
        assertThat(errorMessages).contains(usernameBlankErrorMessage);
    }

    @Test
    void whenIdIsBlank_thenConstraintViolationSet() {
        entity.setId(Strings.EMPTY);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthErrorMessage);
    }

    @Test
    void whenIdIsLessThen_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.substring(1, 10));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthErrorMessage);
    }

    @Test
    void whenIdIsMoreThen_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.concat("a"));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthErrorMessage);
    }

    @Test
    void whenIdIsExactly_36_charactersLong_thenNoViolation() {
        entity.setId(exactly36CharactersString);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTickerIsBlank_thenConstraintViolationSet() {
        entity.setTicker(Strings.EMPTY);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "ticker");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerBlankErrorMessage);
    }

    @Test
    void whenTickerIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        entity.setTicker(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "ticker");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerLengthErrorMessage);
    }

    @Test
    void whenTickerIsExactly_50_charactersLong_thenNoViolation() {
        entity.setTicker(exactly50CharactersString);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "ticker");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsPast_thenNoViolation() {
        entity.setExDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsPresent_thenNoViolation() {
        entity.setExDate(LocalDate.now());

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsFuture_thenNoViolation() {
        entity.setExDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "exDate");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(exDatePastOrPresentMessage);
    }

    @Test
    void whenDateIsPast_thenNoViolation() {
        entity.setDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPresent_thenNoViolation() {
        entity.setDate(LocalDate.now());

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsFuture_thenNoViolation() {
        entity.setDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "date");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(datePastOrPresentMessage);
    }

    @Test
    void whenAmountIsNegative_thenConstraintViolationSet() {
        entity.setAmount(new BigDecimal(-1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "amount");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZero);
    }

    @Test
    void whenAmountIsZero_thenConstraintViolationSet() {
        entity.setAmount(new BigDecimal(0));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "amount");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZero);
    }

    @Test
    void whenAmountIsPositive_thenNoViolation() {
        entity.setAmount(new BigDecimal(1));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "amount");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        entity.setUsername(Strings.EMPTY);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameBlankErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThen_50_charactersLong_thenConstraintViolationSet() {
        entity.setUsername(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameLengthErrorMessage);
    }

    @Test
    void whenUsernameIsExactly_50_charactersLong_thenNoViolation() {
        entity.setUsername(exactly50CharactersString);

        Set<ConstraintViolation<DividendEntity>> violationSet = validator
                .validateProperty(entity, "username");

        assertThat(violationSet).isEmpty();
    }

}