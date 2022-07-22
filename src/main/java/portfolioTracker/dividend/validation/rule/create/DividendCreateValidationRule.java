package portfolioTracker.dividend.validation.rule.create;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendCreateValidationRule {

    void validate(DividendDtoCreateRequest dto);
}
