package portfolioTracker.transaction.dto;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static portfolioTracker.dto.eventType.EventType.SELL;
import static portfolioTracker.util.TestUtil.*;
import static portfolioTracker.util.TestUtil.exactly50CharactersString;
import static portfolioTracker.util.TransactionTestUtil.extractMessagesFromViolationSetUpdateRequest;

@SpringBootTest
class TransactionDtoUpdateRequestTest {


    private final TransactionDtoUpdateRequest requestDto = new TransactionDtoUpdateRequest();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstraintViolationSet() {
        var violationSet = validator.validate(requestDto);

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
        assertThat(errorMessages).contains(dateNullTestErrorMessage);
        assertThat(errorMessages).contains(priceNullTestErrorMessage);
        assertThat(errorMessages).contains(sharesNullTestErrorMessage);
        assertThat(errorMessages).contains(commissionNullTestErrorMessage);
        assertThat(errorMessages).contains(typeNullTestErrorMessage);
        assertThat(errorMessages).contains(portfolioIdBlankTestErrorMessage);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenIdIsBlank_thenConstraintViolationSet() {
        requestDto.setId(Strings.EMPTY);

        var violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsLessThen_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setId(exactly36CharactersString.substring(1, exactly36CharactersString.length() - 1));

        var violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsMoreThen_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setId(exactly36CharactersString.concat("a"));

        var violationSet = validator
                .validateProperty(requestDto, "id");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIs_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setId(exactly36CharactersString);

        var violationSet = validator
                .validateProperty(requestDto, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTickerIsBlank_thenConstraintViolationException() {
        requestDto.setTicker(Strings.EMPTY);

        var violationSet = validator.validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
    }

    @Test
    void whenTickerIsMoreThan_50_charactersLong_thenConstraintViolationException() {
        requestDto.setTicker(exactly50CharactersString.concat("a"));

        var violationSet = validator.validateProperty(requestDto, "ticker");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(tickerLengthTestErrorMessage);
    }

    @Test
    void whenTickerIs_50_charactersLong_thenNoViolation() {
        requestDto.setTicker(exactly50CharactersString);

        var violationSet = validator.validateProperty(requestDto, "ticker");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPast_thenNoViolation() {
        requestDto.setDate(LocalDate.now().minusDays(1));

        var violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPresent_thenNoViolation() {
        requestDto.setDate(LocalDate.now());

        var violationSet = validator
                .validateProperty(requestDto, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsFuture_thenConstraintViolationSet() {
        requestDto.setDate(LocalDate.now().plusDays(1));

        var violationSet = validator
                .validateProperty(requestDto, "date");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(datePastOrPresentTestErrorMessage);
    }

    @Test
    void whenSharesAreNegative_thenConstraintViolationSet() {
        requestDto.setShares(new BigDecimal(-1));

        var violationSet = validator
                .validateProperty(requestDto, "shares");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(sharesPositiveTestErrorMessage);
    }

    @Test
    void whenSharesAreZero_thenConstraintViolationSet() {
        requestDto.setShares(new BigDecimal(0));

        var violationSet = validator
                .validateProperty(requestDto, "shares");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(sharesPositiveTestErrorMessage);
    }

    @Test
    void whenSharesArePositive_thenNoViolation() {
        requestDto.setShares(new BigDecimal(1));

        var violationSet = validator.validateProperty(requestDto, "shares");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPriceIsNegative_thenConstraintViolationSet() {
        requestDto.setPrice(new BigDecimal(-1));

        var violationSet = validator.validateProperty(requestDto, "price");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(pricePositiveTestErrorMessage);
    }

    @Test
    void whenPriceIsZero_thenConstraintViolationSet() {
        requestDto.setPrice(new BigDecimal(0));

        var violationSet = validator.validateProperty(requestDto, "price");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(pricePositiveTestErrorMessage);
    }

    @Test
    void whenPriceIsPositive_thenNoViolation() {
        requestDto.setPrice(new BigDecimal(1));

        var violationSet = validator.validateProperty(requestDto, "price");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCommissionIsNegative_thenConstraintViolationSet() {
        requestDto.setCommission(new BigDecimal(-1));

        var violationSet = validator.validateProperty(requestDto, "commission");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(commissionPositiveOrZeroTestErrorMessage);
    }

    @Test
    void whenCommissionIsZero_thenNoViolation() {
        requestDto.setCommission(new BigDecimal(0));

        var violationSet = validator.validateProperty(requestDto, "commission");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCommissionIsPositive_thenNoViolation() {
        requestDto.setCommission(new BigDecimal(1));

        var violationSet = validator.validateProperty(requestDto, "commission");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTypeIsNotNull_thenNoViolation() {
        requestDto.setType(SELL);

        var violationSet = validator.validateProperty(requestDto, "type");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPortfolioIdIsBlank_thenConstraintViolationSet() {
        requestDto.setPortfolioId(Strings.EMPTY);

        var violationSet = validator.validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdBlankTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIsLessThen_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.substring(0, exactly36CharactersString.length() - 1));

        var violationSet = validator.validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdLengthTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIsMoreThen_36_charactersLong_thenConstraintViolationSet() {
        requestDto.setPortfolioId(exactly36CharactersString.concat("a"));

        var violationSet = validator.validateProperty(requestDto, "portfolioId");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(portfolioIdLengthTestErrorMessage);
    }

    @Test
    void whenPortfolioIdIs_36_charactersLong_thenNoViolation() {
        requestDto.setPortfolioId(exactly36CharactersString);

        var violationSet = validator.validateProperty(requestDto, "portfolioId");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        requestDto.setUsername(Strings.EMPTY);

        var violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThen_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setUsername(exactly50CharactersString.concat("a"));

        var violationSet = validator
                .validateProperty(requestDto, "username");

        String errorMessages = extractMessagesFromViolationSetUpdateRequest(violationSet);
        assertThat(errorMessages).contains(usernameMaxLengthTestErrorMessage);
    }

    @Test
    void whenUsernameIs_50_charactersLong_thenConstraintViolationSet() {
        requestDto.setUsername(exactly50CharactersString);

        var violationSet = validator
                .validateProperty(requestDto, "username");

        assertThat(violationSet).isEmpty();
    }



}