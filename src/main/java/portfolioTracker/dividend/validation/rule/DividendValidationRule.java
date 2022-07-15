package portfolioTracker.dividend.validation.rule;

import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;

public interface DividendValidationRule {

    void validate(DividendDtoUpdateRequest dto);

    void validate(DividendDtoCreateRequest dto);
}
