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
class DividendDtoUpdateRequestTest {

    private final DividendDtoUpdateRequest requestDto = new DividendDtoUpdateRequest();
    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstraintViolationSet() {
        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validate(requestDto);

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idNotBlankErrorMessage);
        assertThat(errorMessages).contains(tickerBlankErrorMessage);
        assertThat(errorMessages).contains(exDateNullErrorMessage);
        assertThat(errorMessages).contains(dateNullErrorMessage);
        assertThat(errorMessages).contains(amountNullErrorMessage);
        assertThat(errorMessages).contains(typeNullErrorMessage);
        assertThat(errorMessages).contains(portfolioIdNotBlankErrorMessage);
        assertThat(errorMessages).contains(usernameBlankErrorMessage);
    }

    @Test
    void whenIdIsBlank_thenConstraintViolationSet() {
        requestDto.setId(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idNotBlankErrorMessage);
    }

    @Test
    void whenIdIsMoreThan_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setId(exactly36CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idLengthErrorMessage);
    }

    @Test
    void whenIdIsLessThan_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setId(exactly36CharactersString.substring(0, 10));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idLengthErrorMessage);
    }

    @Test
    void whenIdIsExactly_36_charactersLong_theNoViolation() {
        requestDto.setId(exactly36CharactersString);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTickerIsBlank_thenConstraintViolationSet() {
        requestDto.setTicker(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(tickerBlankErrorMessage);
    }

    @Test
    void whenTickerIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setTicker(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(tickerLengthErrorMessage);
    }

    @Test
    void whenTickerIsExactly_50_charactersLong_thenNoViolation() {
        requestDto.setTicker(exactly50CharactersString);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "ticker");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsFuture_thenConstraintViolationSet() {
        requestDto.setDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(datePastOrPresentMessage);
    }

    @Test
    void whenDateIsPresent_thenNoViolation() {
        requestDto.setDate(LocalDate.now());

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPast_thenNoViolation() {
        requestDto.setDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsFuture_thenConstraintViolationSet() {
        requestDto.setExDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(exDatePastOrPresentMessage);
    }

    @Test
    void whenExDateIsPresent_thenNoViolation() {
        requestDto.setExDate(LocalDate.now());

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenExDateIsPast_thenNoViolation() {
        requestDto.setExDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "exDate");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenAmountIsZero_thenConstraintViolationSet() {
        requestDto.setAmount(new BigDecimal(0));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZero);
    }

    @Test
    void whenAmountIsNegative_thenConstraintViolationSet() {
        requestDto.setAmount(new BigDecimal(-1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(amountGreaterThenZero);
    }

    @Test
    void whenAmountIsPositive_thenNoViolation() {
        requestDto.setAmount(new BigDecimal(1));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "amount");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPortfolioIdIsBlank_thenConstraintViolationSet() {
        requestDto.setPortfolioId(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdNotBlankErrorMessage);
    }

    @Test
    void whenPortfolioIdIsMoreThan_36_characterLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioLengthErrorMessage);
    }

    @Test
    void whenPortfolioIdIsLessThan_36_characterLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.substring(1, 10));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioLengthErrorMessage);
    }

    @Test
    void whenPortfolioIdIsExactly_36_characterLong_thenNoViolation() {
        requestDto.setPortfolioId(exactly36CharactersString);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "portfolioId");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        requestDto.setUsername(Strings.EMPTY);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(usernameBlankErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThan_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setUsername(exactly50CharactersString.concat("a"));

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(usernameLengthErrorMessage);
    }

    @Test
    void whenUsernameIsExactly_50_charactersLong_thenNoViolation() {
        requestDto.setUsername(exactly50CharactersString);

        Set<ConstraintViolation<DividendDtoUpdateRequest>> violationSet = validator
                .validateProperty(requestDto, "username");

        assertThat(violationSet).isEmpty();
    }


}