package portfolioTracker.transaction.domain;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import portfolioTracker.dto.eventType.EventType;
import portfolioTracker.util.TransactionTestUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;
import static org.junit.jupiter.api.Assertions.*;
import static portfolioTracker.dto.eventType.EventType.SELL;
import static portfolioTracker.util.PortfolioTestUtil.newPortfolioEntity;
import static portfolioTracker.util.TestUtil.*;
import static portfolioTracker.util.TransactionTestUtil.*;

@SpringBootTest
class TransactionEntityTest {

    TransactionEntity entity = new TransactionEntity();

    @Autowired
    Validator validator;

    @Test
    void whenAllFieldsNull_thenConstraintViolationSet() {
        var violationSet = validator
                .validate(entity);

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
        assertThat(errorMessages).contains(dateNullTestErrorMessage);
        assertThat(errorMessages).contains(sharesNullTestErrorMessage);
        assertThat(errorMessages).contains(priceNullTestErrorMessage);
        assertThat(errorMessages).contains(commissionNullTestErrorMessage);
        assertThat(errorMessages).contains(typeNullTestErrorMessage);
        assertThat(errorMessages).contains(portfolioNullTestErrorMessage);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenIdIsBlank_thenConstraintViolationSet() {
        entity.setId(Strings.EMPTY);

        var violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsLessThen_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.substring(1, exactly36CharactersString.length() - 1));

        var violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIsMoreThen_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString.concat("a"));

        var violationSet = validator
                .validateProperty(entity, "id");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(idLengthTestErrorMessage);
    }

    @Test
    void whenIdIs_36_charactersLong_thenConstraintViolationSet() {
        entity.setId(exactly36CharactersString);

        var violationSet = validator
                .validateProperty(entity, "id");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTickerIsBlank_thenConstraintViolationSet() {
        entity.setTicker(Strings.EMPTY);

        var violationSet = validator
                .validateProperty(entity, "ticker");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerBlankTestErrorMessage);
    }

    @Test
    void whenTickerIsMoreThan_50_charactersLength_thenConstraintViolationSet() {
        entity.setTicker(exactly50CharactersString.concat("a"));

        var violationSet = validator
                .validateProperty(entity, "ticker");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(tickerLengthTestErrorMessage);
    }

    @Test
    void whenTickerIs_50_charactersLong_thenNoViolation() {
        entity.setTicker(exactly50CharactersString);

        var violationSet = validator
                .validateProperty(entity, "ticker");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPast_thenNoViolation() {
        entity.setDate(LocalDate.now().minusDays(1));

        var violationSet = validator
                .validateProperty(entity, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsPresent_thenNoViolation() {
        entity.setDate(LocalDate.now());

        var violationSet = validator
                .validateProperty(entity, "date");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenDateIsFuture_thenConstraintViolationSet() {
        entity.setDate(LocalDate.now().plusDays(1));

        var violationSet = validator
                .validateProperty(entity, "date");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(datePastOrPresentTestErrorMessage);
    }

    @Test
    void whenSharesAreNegative_thenConstraintViolationSet() {
        entity.setShares(new BigDecimal(-1));

        var violationSet = validator
                .validateProperty(entity, "shares");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(sharesPositiveTestErrorMessage);
    }

    @Test
    void whenSharesAreZero_thenConstraintViolationSet() {
        entity.setShares(new BigDecimal(0));

        var violationSet = validator
                .validateProperty(entity, "shares");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(sharesPositiveTestErrorMessage);
    }

    @Test
    void whenSharesArePositive_thenNoViolation() {
        entity.setShares(new BigDecimal(1));

        var violationSet = validator.validateProperty(entity, "shares");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPriceIsNegative_thenConstraintViolationSet() {
        entity.setPrice(new BigDecimal(-1));

        var violationSet = validator.validateProperty(entity, "price");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(pricePositiveTestErrorMessage);
    }

    @Test
    void whenPriceIsZero_thenConstraintViolationSet() {
        entity.setPrice(new BigDecimal(0));

        var violationSet = validator.validateProperty(entity, "price");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(pricePositiveTestErrorMessage);
    }

    @Test
    void whenPriceIsPositive_thenNoViolation() {
        entity.setPrice(new BigDecimal(1));

        var violationSet = validator.validateProperty(entity, "price");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCommissionIsNegative_thenConstraintViolationSet() {
        entity.setCommission(new BigDecimal(-1));

        var violationSet = validator.validateProperty(entity, "commission");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(commissionPositiveOrZeroTestErrorMessage);
    }

    @Test
    void whenCommissionIsZero_thenNoViolation() {
        entity.setCommission(new BigDecimal(0));

        var violationSet = validator.validateProperty(entity, "commission");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenCommissionIsPositive_thenNoViolation() {
        entity.setCommission(new BigDecimal(1));

        var violationSet = validator.validateProperty(entity, "commission");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenTypeIsNotNull_thenNoViolation() {
        entity.setType(SELL);

        var violationSet = validator.validateProperty(entity, "type");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenPortfolioIsNotNull_thenNoViolation() {
        entity.setPortfolio(newPortfolioEntity());

        var violationSet = validator.validateProperty(entity, "portfolio");

        assertThat(violationSet).isEmpty();
    }

    @Test
    void whenUsernameIsBlank_thenConstraintViolationSet() {
        entity.setUsername(Strings.EMPTY);

        var violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameBlankTestErrorMessage);
    }

    @Test
    void whenUsernameIsMoreThen_50_charactersLong_thenConstraintViolationSet() {
        entity.setUsername(exactly50CharactersString.concat("a"));

        var violationSet = validator
                .validateProperty(entity, "username");

        String errorMessages = extractMessagesFromViolationSetEntity(violationSet);
        assertThat(errorMessages).contains(usernameMaxLengthTestErrorMessage);
    }

    @Test
    void whenUsernameIs_50_charactersLong_thenConstraintViolationSet() {
        entity.setUsername(exactly50CharactersString);

        var violationSet = validator
                .validateProperty(entity, "username");

        assertThat(violationSet).isEmpty();
    }






}