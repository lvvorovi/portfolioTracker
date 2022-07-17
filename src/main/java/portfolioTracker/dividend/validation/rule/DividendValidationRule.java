package portfolioTracker.dividend.validation.rule;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendValidationRule {

    void validate(DividendDtoUpdateRequest dto);

    void validate(DividendDtoCreateRequest dto);
}
