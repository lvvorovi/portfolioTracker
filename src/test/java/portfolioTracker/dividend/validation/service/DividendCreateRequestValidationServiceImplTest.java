package portfolioTracker.dividend.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.create.DividendCreateValidationRule;
import portfolioTracker.dividend.validation.rule.DoubleEntryDividendValidationRule;
import portfolioTracker.dividend.validation.rule.TickerDividendValidationRule;
import portfolioTracker.dividend.validation.service.DividendCreateRequestValidationServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DividendCreateRequestValidationServiceImplTest {

    @Mock
    DoubleEntryDividendValidationRule doubleEntryDividendValidationRule;
    @Mock
    TickerDividendValidationRule tickerDividendValidationRule;

    List<DividendCreateValidationRule> ruleList;
    DividendCreateRequestValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(doubleEntryDividendValidationRule);
        ruleList.add(tickerDividendValidationRule);
        victim = new DividendCreateRequestValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenCreateRequest_thenDelegatesToAllRules() {
        DividendDtoCreateRequest requestDtoMock = mock(DividendDtoCreateRequest.class);
        ruleList.forEach(rule -> doNothing().when(rule).validate(requestDtoMock));

        victim.validate(requestDtoMock);

        ruleList.forEach(rule -> verify(rule, times(1)).validate(requestDtoMock));
    }

}