package portfolioTracker.dividend.validation.service;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendUpdateRequestValidationService {

    void validate(DividendDtoUpdateRequest dto);

}
