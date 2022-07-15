package com.portfolioTracker.domain.dividend.validation;

import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;

public interface DividendValidationService {


    void validate(DividendDtoUpdateRequest dto);

    void validate(DividendDtoCreateRequest dto);
}
