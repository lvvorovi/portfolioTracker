package portfolioTracker.dividend.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.DoubleEntryDividendValidationRule;
import portfolioTracker.dividend.validation.rule.TickerDividendValidationRule;
import portfolioTracker.dividend.validation.rule.update.DividendUpdateValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DividendUpdateRequestValidationServiceImplTest {

    @Mock
    DoubleEntryDividendValidationRule doubleEntryDividendValidationRule;
    @Mock
    TickerDividendValidationRule tickerDividendValidationRule;

    List<DividendUpdateValidationRule> ruleList;
    DividendUpdateRequestValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(doubleEntryDividendValidationRule);
        ruleList.add(tickerDividendValidationRule);
        victim = new DividendUpdateRequestValidationServiceImpl(ruleList);
    }


    @Test
    void validate_whenUpdateRequest_thenDelegateToAllRules() {
        DividendDtoUpdateRequest requestDto = mock(DividendDtoUpdateRequest.class);
        ruleList.forEach(rule -> doNothing().when(rule).validate(requestDto));

        victim.validate(requestDto);

        ruleList.forEach(rule -> verify(rule, times(1)).validate(requestDto));
    }

}