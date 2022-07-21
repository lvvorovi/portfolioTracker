package portfolioTracker.portfolio.domain;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static portfolioTracker.util.PortfolioTestUtil.*;

@SpringBootTest
class PortfolioEntityTest {

    PortfolioEntity entity = new PortfolioEntity();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstraintViolationSet() {
        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validate(entity);

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(nameBlankTestErrorMessage);
        assertThat(errorMessages).contains(strategyBlankTestErrorMessage);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenIdIsNull_thenNoViolation() {
        entity.setId(null);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenIdIsBlank_thenNoViolation() {
        entity.setId(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsMoreThan_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsLessThan_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.substring(0, 10));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIs_36_charactersLong_thenNoViolation() {
        entity.setId(exactly36CharactersString);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenNameIsBlank_thenConstraintViolationSet() {
        entity.setName(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "name");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(nameBlankTestErrorMessage);
    }

    @Test
    void whenNameIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        entity.setName(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "name");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(nameMaxLengthTestErrorMessage);
    }

    @Test
    void whenNameIs_50_charactersLong_thenNoViolation() {
        entity.setName(exactly50CharactersString);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "name");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenStrategyIsBlank_thenConstraintViolationSet() {
        entity.setStrategy(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "strategy");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(strategyBlankTestErrorMessage);
    }

    @Test
    void whenStrategyIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        entity.setStrategy(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "strategy");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(strategyMaxLengthTestErrorMessage);
    }

    @Test
    void whenStrategyIs_50_charactersLong_thenNoViolation() {
        entity.setStrategy(exactly50CharactersString);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "strategy");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCurrencyIsBlank_thenConstraintViolationSet() {
        entity.setCurrency(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "currency");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIsMoreThan_3_charactersLong_thenConstraintViolationSet() {
        entity.setCurrency(exactly3CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "currency");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIsLessThan_3_charactersLong_thenConstraintViolationSet() {
        entity.setCurrency(exactly3CharactersString.substring(0, exactly3CharactersString.length() - 1));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "currency");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIs_3_charactersLong_thenNoViolation() {
        entity.setCurrency(exactly3CharactersString);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "currency");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        entity.setUsername(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        entity.setUsername(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameMaxLengthTestErrorMessage);
    }

    @Test
    void whenUsernameIs_50_charactersLong_thenNoViolation() {
        entity.setUsername(exactly50CharactersString);

        Set<ConstraintViolation<PortfolioEntity>> violationSet = validator
                .validateProperty(entity, "username");

        assertThat(violationSet).isEmpty();
    }


}