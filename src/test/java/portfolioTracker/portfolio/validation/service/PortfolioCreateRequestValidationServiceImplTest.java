package portfolioTracker.portfolio.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.validation.rule.PortfolioCurrencyValidationRule;
import portfolioTracker.portfolio.validation.rule.PortfolioNameValidationRule;
import portfolioTracker.portfolio.validation.rule.createRequestRule.PortfolioCreateValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioCreateRequestValidationServiceImplTest {

    @Mock
    PortfolioCurrencyValidationRule portfolioCurrencyValidationRule;
    @Mock
    PortfolioNameValidationRule portfolioNameValidationRule;

    List<PortfolioCreateValidationRule> ruleList;
    PortfolioCreateRequestValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(portfolioCurrencyValidationRule);
        ruleList.add(portfolioNameValidationRule);
        victim = new PortfolioCreateRequestValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenRequest_thenDelegateToEachRule() {
        PortfolioDtoCreateRequest requestDtoMock = mock(PortfolioDtoCreateRequest.class);
        doNothing().when(portfolioCurrencyValidationRule).validate(requestDtoMock);
        doNothing().when(portfolioNameValidationRule).validate(requestDtoMock);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(portfolioNameValidationRule, times(1)).validate(requestDtoMock);
        verify(portfolioCurrencyValidationRule, times(1)).validate(requestDtoMock);
        verifyNoMoreInteractions(portfolioCurrencyValidationRule, portfolioNameValidationRule);
        verifyNoInteractions(requestDtoMock);
    }

}