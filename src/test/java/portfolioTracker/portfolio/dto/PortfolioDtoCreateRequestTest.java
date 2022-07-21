package portfolioTracker.portfolio.dto;

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
class PortfolioDtoCreateRequestTest {

    private final PortfolioDtoCreateRequest requestDto = new PortfolioDtoCreateRequest();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstraintViolationSet() {
        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validate(requestDto);

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(nameBlankTestErrorMessage);
        assertThat(errorMessages).contains(strategyBlankTestErrorMessage);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenNameIsBlank_thenConstraintViolationSet() {
        requestDto.setName(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "name");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(nameBlankTestErrorMessage);
    }

    @Test
    void whenNameIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setName(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "name");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(nameMaxLengthTestErrorMessage);
    }

    @Test
    void whenNameIs_50_charactersLong_thenNoViolation() {
        requestDto.setName(exactly50CharactersString);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "name");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenStrategyIsBlank_thenConstraintViolationSet() {
        requestDto.setStrategy(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "strategy");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(strategyBlankTestErrorMessage);
    }

    @Test
    void whenStrategyIsMoreThan_150_charactersLong_thenConstraintViolationSet() {
        requestDto.setStrategy(exactly150CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "strategy");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(strategyMaxLengthTestErrorMessage);
    }

    @Test
    void whenStrategyIs_150_charactersLong_thenNoViolation() {
        requestDto.setStrategy(exactly150CharactersString);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "strategy");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCurrencyIsBlank_thenConstraintViolationSet() {
        requestDto.setCurrency(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "currency");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIsMoreThan_3_charactersLong_thenConstraintViolationSet() {
        requestDto.setCurrency(exactly3CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "currency");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIsLessThan_3_charactersLong_thenConstraintViolationSet() {
        requestDto.setCurrency(exactly3CharactersString.substring(0, exactly3CharactersString.length() - 1));

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "currency");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(currencyTypeTestErrorMessage);
    }

    @Test
    void whenCurrencyIs_3_charactersLong_thenNoViolation() {
        requestDto.setCurrency(exactly3CharactersString);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "currency");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        requestDto.setUsername(Strings.EMPTY);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setUsername(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(usernameMaxLengthTestErrorMessage);
    }

    @Test
    void whenUsernameIs_50_charactersLong_thenNoViolation() {
        requestDto.setUsername(exactly50CharactersString);

        Set<ConstraintViolation<PortfolioDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        assertThat(violationSet).isEmpty();
    }

}