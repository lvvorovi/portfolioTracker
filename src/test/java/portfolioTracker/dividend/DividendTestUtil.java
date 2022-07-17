package portfolioTracker.dividend;

import org.apache.logging.log4j.util.Strings;
import org.assertj.core.api.AbstractStringAssert;
import org.springframework.boot.test.system.CapturedOutput;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static portfolioTracker.dto.eventType.EventType.DIVIDEND;
import static portfolioTracker.portfolio.PortfolioTestUtil.newPortfolioEntitySkipEvents;

public class DividendTestUtil {

    private static final String errorMessage = "{\"errorMessage\":";
    private static final String portfolioMaxLengthErrorMessage = "portfolioId: max 60 characters";
    private static final String exDateNullErrorMessage = "exDate: must not be null";
    private static final String amountNullErrorMessage = "amount: must not be null";
    private static final String dateNullErrorMessage = "date: must not be null";
    private static final String usernameBlankErrorMessage = "username: must not be blank";
    private static final String tickerBlankErrorMessage = "ticker: must not be blank";
    private static final String typeNullErrorMessage = "type: must not be null";
    private static final String exDatePastOrPresentMessage = "exDate: must be a date in the past or in the present";
    private static final String portfolioIdNotBlankErrorMessage = "portfolioId: must not be blank";
    private static final String datePastOrPresentMessage = "date: must be a date in the past or in the present";
    private static final String amountGreaterThenZero = "amount: must be greater than 0";
    private static final String tickerMaxLengthErrorMessage = "ticker: max 50 characters";
    private static final String usernameMaxLengthErrorMessage = "username: max 50 characters";
    private static final String idMaxLengthErrorMessage = "id: max 60 characters";
    private static final String idNotBlankErrorMessage = "id: must not be blank";


    public static DividendEntity newDividendEntity() {
        DividendEntity entity = new DividendEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setUsername("john@email.com");
        entity.setPortfolio(newPortfolioEntitySkipEvents());
        entity.setAmount(new BigDecimal(100));
        entity.setDate(LocalDate.now());
        entity.setTicker("BRK-B");
        entity.setExDate(LocalDate.now().minusDays(2));
        entity.setType(DIVIDEND);
        return entity;
    }

    public static DividendDtoUpdateRequest newDividendDtoUpdateRequest(DividendEntity entity) {
        DividendDtoUpdateRequest dto = new DividendDtoUpdateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setId(entity.getId());
        dto.setTicker(entity.getTicker());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        return dto;
    }

    public static DividendDtoCreateRequest newDividendDtoCreateRequest(DividendEntity entity) {
        DividendDtoCreateRequest dto = new DividendDtoCreateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        dto.setTicker(entity.getTicker());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setType(entity.getType());
        return dto;
    }

    public static DividendDtoResponse newDividendResponseDto(DividendEntity entity) {
        DividendDtoResponse dto = new DividendDtoResponse();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setUsername(entity.getUsername());
        dto.setDate(entity.getDate());
        dto.setTicker(entity.getTicker());
        dto.setExDate(entity.getExDate());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        return dto;
    }

    public static List<DividendDtoResponse> newDividendDtoResponseList() {
        return List.of(
                newDividendResponseDto(newDividendEntity()),
                newDividendResponseDto(newDividendEntity()),
                newDividendResponseDto(newDividendEntity())
        );
    }

    public static List<DividendDtoCreateRequest> newDividendDtoCreateList() {
        return List.of(
                newDividendDtoCreateRequest(newDividendEntity()),
                newDividendDtoCreateRequest(newDividendEntity()),
                newDividendDtoCreateRequest(newDividendEntity())
        );
    }

    public static List<DividendEntity> newDividendEntityList() {
        return List.of(
                newDividendEntity(),
                newDividendEntity(),
                newDividendEntity()
        );
    }

    public static List<DividendEntity> newDividendEntityMockList () {
        return List.of(
                mock(DividendEntity.class),
                mock(DividendEntity.class),
                mock(DividendEntity.class)
        );
    }

    //DividendDtoCreateRequest validation input

    public static List<String> saveRequestAllFieldsNullErrorMessageList() {
        return List.of(/*errorMessage,*/
                portfolioIdNotBlankErrorMessage,
                exDateNullErrorMessage,
                amountNullErrorMessage,
                dateNullErrorMessage,
                usernameBlankErrorMessage,
                tickerBlankErrorMessage,
                typeNullErrorMessage);
    }

    public static List<String> saveTickerBlankRequestErrorMessageList() {
        return List.of(errorMessage,
                portfolioIdNotBlankErrorMessage,
                exDatePastOrPresentMessage,
                amountGreaterThenZero,
                datePastOrPresentMessage,
                usernameBlankErrorMessage,
                tickerBlankErrorMessage,
                typeNullErrorMessage);
    }

    public static List<String> saveTickerTooLongRequestErrorMessageList() {
        return List.of(errorMessage,
                tickerMaxLengthErrorMessage,
                usernameMaxLengthErrorMessage,
                dateNullErrorMessage,
                portfolioMaxLengthErrorMessage,
                typeNullErrorMessage,
                amountGreaterThenZero,
                exDateNullErrorMessage);
    }

    public static DividendDtoCreateRequest newAllFieldsNullCreateRequest() {
        return DividendDtoCreateRequest.builder()
                .ticker(null)
                .exDate(null)
                .date(null)
                .amount(null)
                .portfolioId(null)
                .type(null)
                .username(null)
                .build();
    }

    public static DividendDtoCreateRequest newTickerBlankCreateRequest() {
        return DividendDtoCreateRequest.builder()
                .ticker("")
                .exDate(LocalDate.now().plusDays(10))
                .date(LocalDate.now().plusDays(10))
                .amount(new BigDecimal(-10))
                .portfolioId("")
                .type(null)
                .username(Strings.EMPTY)
                .build();
    }

    public static DividendDtoCreateRequest newTickerTooLongCreateRequest() {
        return DividendDtoCreateRequest.builder()
                .ticker("thisStringLengthIsMoreThenFiftyCharactersThisStringLengthIsMoreThenFiftyCharacters")
                .exDate(null)
                .date(null)
                .amount(new BigDecimal(0))
                .portfolioId("thisStringLengthIsMoreThenFiftyCharactersThisStringLengthIsMoreThenFiftyCharacters")
                .type(null)
                .username("thisStringLengthIsMoreThenFiftyCharactersThisStringLengthIsMoreThenFiftyCharacters")
                .build();
    }

    //DividendUpdateDtoRequest validation input

    public static List<String> allFieldsNullUpdateRequestErrorMessageList() {
        return List.of(errorMessage,
                tickerBlankErrorMessage,
                typeNullErrorMessage,
                usernameBlankErrorMessage,
                amountNullErrorMessage,
                dateNullErrorMessage,
                portfolioIdNotBlankErrorMessage,
                exDateNullErrorMessage,

                idNotBlankErrorMessage);
    }

    public static List<String> fieldsBlankUpdateRequestErrorMessageList() {
        return List.of(errorMessage,
                portfolioIdNotBlankErrorMessage,
                exDatePastOrPresentMessage,
                amountGreaterThenZero,
                datePastOrPresentMessage,
                usernameBlankErrorMessage,
                tickerBlankErrorMessage,
                typeNullErrorMessage,
                idNotBlankErrorMessage);
    }

    public static List<String> tooLongFieldsUpdateRequestErrorMessageList() {
        return List.of(errorMessage,
                typeNullErrorMessage,
                tickerMaxLengthErrorMessage,
                amountGreaterThenZero,
                usernameMaxLengthErrorMessage,
                dateNullErrorMessage,
                exDateNullErrorMessage,
                portfolioMaxLengthErrorMessage,
                idMaxLengthErrorMessage);
    }

    public static DividendDtoUpdateRequest newAllFieldsNullUpdateRequest() {
        return DividendDtoUpdateRequest.builder()
                .ticker(null)
                .exDate(null)
                .date(null)
                .amount(null)
                .portfolioId(null)
                .type(null)
                .username(null)
                .id(null)
                .build();
    }

    public static DividendDtoUpdateRequest newFieldsBlankUpdateRequest() {
        return DividendDtoUpdateRequest.builder()
                .ticker("")
                .exDate(LocalDate.now().plusDays(10))
                .date(LocalDate.now().plusDays(10))
                .amount(new BigDecimal(-10))
                .portfolioId("")
                .type(null)
                .username(Strings.EMPTY)
                .id("")
                .build();
    }

    public static DividendDtoUpdateRequest newTooLongFieldsUpdateRequest() {
        return DividendDtoUpdateRequest.builder()
                .ticker("ThisStringLengthIsMoreThenFiftyCharacters_ThisStringLengthIsMoreThenFiftyCharacters")
                .exDate(null)
                .date(null)
                .amount(new BigDecimal(0))
                .portfolioId("ThisStringLengthIsMoreThenSixtyCharacters_ThisStringLengthIsMoreThenSixtyCharacters")
                .type(null)
                .username("ThisStringLengthIsMoreThenFiftyCharacters_ThisStringLengthIsMoreThenFiftyCharacters")
                .id("ThisStringLengthIsMoreThenSixtyCharacters_ThisStringLengthIsMoreThenSixtyCharacters")
                .build();
    }

    public static AbstractStringAssert<?> assertOutputContainsExpected(CapturedOutput output, String method, URI uri) {
        return assertThat(output.toString())
                .contains("Request id", "requested url", "with method " + method.toUpperCase(), uri.toString());
    }


}
