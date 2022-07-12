package com.portfolioTracker.domain.dividend.validation.rule;

import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;

public interface DividendValidationRule {

    void validate(DividendDtoUpdateRequest dto);

    void validate(DividendDtoCreateRequest dto);
}
