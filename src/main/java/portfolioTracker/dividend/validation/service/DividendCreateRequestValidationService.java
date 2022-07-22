package portfolioTracker.dividend.validation.service;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendCreateRequestValidationService {

    void validate(DividendDtoCreateRequest dto);

}
