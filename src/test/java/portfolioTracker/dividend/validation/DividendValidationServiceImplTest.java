package portfolioTracker.dividend.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.rule.DividendValidationRule;
import portfolioTracker.dividend.validation.rule.DoubleEntryDividendValidationRule;
import portfolioTracker.dividend.validation.rule.TickerDividendValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DividendValidationServiceImplTest {

    @Mock
    DoubleEntryDividendValidationRule doubleEntryDividendValidationRule;
    @Mock
    TickerDividendValidationRule tickerDividendValidationRule;

    List<DividendValidationRule> ruleList;
    DividendValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(doubleEntryDividendValidationRule);
        ruleList.add(tickerDividendValidationRule);
        victim = new DividendValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenCreateRequest_thenDelegatesToAllRules() {
        DividendDtoCreateRequest requestDtoMock = mock(DividendDtoCreateRequest.class);
        doNothing().when(doubleEntryDividendValidationRule).validate(requestDtoMock);
        doNothing().when(tickerDividendValidationRule).validate(requestDtoMock);

        victim.validate(requestDtoMock);

        verify(doubleEntryDividendValidationRule, times(1)).validate(requestDtoMock);
        verify(tickerDividendValidationRule, times(1)).validate(requestDtoMock);
        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));
    }

    @Test
    void validate_whenUpdateRequest_thenDelegateToAllRules() {
        DividendDtoUpdateRequest request = mock(DividendDtoUpdateRequest.class);

        victim.validate(request);

        ruleList.forEach(rule -> verify(rule, times(1)).validate(request));
        assertThatNoException().isThrownBy(() -> victim.validate(request));
    }
}