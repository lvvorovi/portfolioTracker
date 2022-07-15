package portfolioTracker.dividend.validation;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendValidationService {


    void validate(DividendDtoUpdateRequest dto);

    void validate(DividendDtoCreateRequest dto);
}
