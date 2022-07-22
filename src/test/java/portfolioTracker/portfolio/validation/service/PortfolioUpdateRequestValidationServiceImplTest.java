package portfolioTracker.portfolio.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.rule.PortfolioCurrencyValidationRule;
import portfolioTracker.portfolio.validation.rule.PortfolioNameValidationRule;
import portfolioTracker.portfolio.validation.rule.updateRequestRule.PortfolioExistsValidationRule;
import portfolioTracker.portfolio.validation.rule.updateRequestRule.PortfolioUpdateValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioUpdateRequestValidationServiceImplTest {

    @Mock
    PortfolioExistsValidationRule portfolioExistsValidationRule;
    @Mock
    PortfolioCurrencyValidationRule portfolioCurrencyValidationRule;
    @Mock
    PortfolioNameValidationRule portfolioNameValidationRule;

    List<PortfolioUpdateValidationRule> ruleList;
    PortfolioUpdateRequestValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(portfolioCurrencyValidationRule);
        ruleList.add(portfolioExistsValidationRule);
        ruleList.add(portfolioNameValidationRule);
        victim = new PortfolioUpdateRequestValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenRequest_thenDelegateToEachRule() {
        PortfolioDtoUpdateRequest requestDtoMock = mock(PortfolioDtoUpdateRequest.class);
        doNothing().when(portfolioCurrencyValidationRule).validate(requestDtoMock);
        doNothing().when(portfolioExistsValidationRule).validate(requestDtoMock);
        doNothing().when(portfolioNameValidationRule).validate(requestDtoMock);

        assertThatNoException().isThrownBy(() -> victim.validate(requestDtoMock));

        verify(portfolioCurrencyValidationRule, times(1)).validate(requestDtoMock);
        verify(portfolioExistsValidationRule, times(1)).validate(requestDtoMock);
        verify(portfolioNameValidationRule, times(1)).validate(requestDtoMock);
        verifyNoMoreInteractions(portfolioCurrencyValidationRule, portfolioExistsValidationRule, portfolioNameValidationRule);
        verifyNoMoreInteractions(requestDtoMock);
    }

}