package portfolioTracker.dividend.dto;

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
class DividendDtoCreateRequestTest {

    private final DividendDtoCreateRequest requestDto = new DividendDtoCreateRequest();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsAreNull_thenConstraintViolationSet() {
        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator.validate(requestDto);

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdBlankTestErrorMessage);
        assertThat(errorMessages).contains(exDateNullTestErrorMessage);
        assertThat(errorMessages).contains(amountNullTestErrorMessage);
        assertThat(errorMessages).contains(dateNullTestErrorMessage);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
        assertThat(errorMessages).contains(typeNullTestErrorMessage);
    }

    @Test
    void whenTickerIsBlank_thenConstraintViolationSet() {
        requestDto.setTicker(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
    }

    @Test
    void whenTickerIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setTicker(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(tickerLengthTestErrorMessage);
    }

    @Test
    void whenTickerIsExactly_50_charactersLong_thenNoViolation() {
        requestDto.setTicker(exactly50CharactersString);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsFuture_thenConstraintViolationSet() {
        requestDto.setDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(datePastOrPresentTestErrorMessage);
    }

    @Test
    void whenDateIsPresent_thenNoViolation() {
        requestDto.setDate(LocalDate.now());

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPast_thenNoViolation() {
        requestDto.setDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsFuture_thenConstraintViolationSet() {
        requestDto.setExDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(exDatePastOrPresentTestErrorMessage);
    }

    @Test
    void whenExDateIsPresent_thenNoViolation() {
        requestDto.setExDate(LocalDate.now());

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsPast_thenNoViolation() {
        requestDto.setExDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenAmountIsZero_thenConstraintViolationSet() {
        requestDto.setAmount(new BigDecimal(0));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZeroTestErrorMessage);
    }

    @Test
    void whenAmountIsNegative_thenConstraintViolationSet() {
        requestDto.setAmount(new BigDecimal(-1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZeroTestErrorMessage);
    }

    @Test
    void whenAmountIsPositive_thenNoViolation() {
        requestDto.setAmount(new BigDecimal(1));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPortfolioIdIsBlank_thenConstraintViolationSet() {
        requestDto.setPortfolioId(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdBlankTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIsMoreThan_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdLengthTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIsLessThan_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.substring(0, 10));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdLengthTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIsExactly_36_charactersLong_thenNoViolation() {
        requestDto.setPortfolioId(exactly36CharactersString);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameBlank_thenConstraintViolationSet() {
        requestDto.setUsername(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThan_50_charactersLong_thenConstrainViolationSet() {
        requestDto.setUsername(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetCreateRequest(violationSet);
        assertThat(errorMessages).contains(usernameMaxLengthTestErrorMessage);
    }

    @Test
    void whenUsernameIsExactly_50_charactersLong_thenNoViolation() {
        requestDto.setUsername(exactly50CharactersString);

        Set<ConstraintViolation<DividendDtoCreateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        assertThat(violationSet).isEmpty();
    }

}