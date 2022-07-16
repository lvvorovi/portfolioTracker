package portfolioTracker.dividend.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.DividendValidationRule;
import portfolioTracker.dividend.validation.rule.DoubleEntryDividendValidationRule;
import portfolioTracker.dividend.validation.rule.TickerDividendValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@SpringBootTest
class DividendValidationServiceImplTest {

    @Mock
    DoubleEntryDividendValidationRule doubleEntryDividendValidationRule;
    @Mock
    TickerDividendValidationRule tickerDividendValidationRule;

    DividendValidationServiceImpl victim;
    List<DividendValidationRule> ruleList;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(doubleEntryDividendValidationRule);
        ruleList.add(tickerDividendValidationRule);
        victim = new DividendValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenCreateRequest_thenDelegatesToAllRulles() {
        DividendDtoCreateRequest request = mock(DividendDtoCreateRequest.class);
        doNothing().when(doubleEntryDividendValidationRule).validate(request);
        doNothing().when(tickerDividendValidationRule).validate(request);

        victim.validate(request);

        ruleList.forEach(rule -> verify(rule, times(1)).validate(request));
        assertThatNoException().isThrownBy(() -> victim.validate(request));
    }

    @Test
    void validate_whenUpdateRequest_thenDelegateToAllRulles() {
        DividendDtoUpdateRequest request = mock(DividendDtoUpdateRequest.class);

        victim.validate(request);

        ruleList.forEach(rule -> verify(rule, times(1)).validate(request));
        assertThatNoException().isThrownBy(() -> victim.validate(request));
    }
}