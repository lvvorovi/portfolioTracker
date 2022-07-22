package portfolioTracker.dividend.validation.rule.update;

import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;

public interface DividendUpdateValidationRule {

    void validate(DividendDtoUpdateRequest dto);

}
