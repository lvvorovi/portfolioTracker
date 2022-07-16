package portfolioTracker.portfolio.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.validation.rule.PortfolioCurrencyValidationRule;
import portfolioTracker.portfolio.validation.rule.PortfolioNameValidationRule;
import portfolioTracker.portfolio.validation.rule.PortfolioValidationRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class PortfolioValidationServiceImplTest {

    @Mock
    PortfolioCurrencyValidationRule portfolioCurrencyValidationRule;
    @Mock
    PortfolioNameValidationRule portfolioNameValidationRule;

    private PortfolioValidationServiceImpl victim;
    private List<PortfolioValidationRule> ruleList;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(portfolioCurrencyValidationRule);
        ruleList.add(portfolioNameValidationRule);
        victim = new PortfolioValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenCreateRequest_thenDelegatesToEachRule() {
        PortfolioDtoCreateRequest createRequest = mock(PortfolioDtoCreateRequest.class);

        victim.validate(createRequest);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validate(createRequest));
    }

    @Test
    void validate_whenUpdateRequest_thenDelegatesToEachRule() {
        PortfolioDtoUpdateRequest updateRequest = mock(PortfolioDtoUpdateRequest.class);

        victim.validate(updateRequest);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validate(updateRequest));
    }
}