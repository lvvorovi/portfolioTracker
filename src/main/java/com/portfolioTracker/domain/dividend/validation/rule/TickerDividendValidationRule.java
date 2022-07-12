package com.portfolioTracker.domain.dividend.validation.rule;

import com.portfolioTracker.core.contract.ApiTickerService;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.validation.exception.TickerNotSupportedDividendException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@AllArgsConstructor
@Priority(1)
public class TickerDividendValidationRule implements DividendValidationRule {

    private final ApiTickerService apiTickerService;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        validateTicker(dto.getTicker());
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        validateTicker(dto.getTicker());
    }

    private void validateTicker(String ticker) {
        if (apiTickerService.isTickerSupported(ticker)) {
            return;
        }
        throw new TickerNotSupportedDividendException("Not supported ticker: " + ticker);
    }

}