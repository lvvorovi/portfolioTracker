package portfolioTracker.dividend.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.exception.DividendExistsDividendException;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;
import portfolioTracker.dividend.validation.rule.create.DividendCreateValidationRule;
import portfolioTracker.dividend.validation.rule.update.DividendUpdateValidationRule;

import javax.annotation.Priority;
import java.util.Optional;

import static portfolioTracker.core.ExceptionErrors.DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE;
import static portfolioTracker.core.ExceptionErrors.DIVIDEND_NOT_FOUND_EXCEPTION_MESSAGE;

@Component
@AllArgsConstructor
@Priority(0)
public class DoubleEntryDividendValidationRule implements DividendCreateValidationRule, DividendUpdateValidationRule {

    private final DividendRepository repository;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        if (!repository.existsById(dto.getId())) throw new DividendNotFoundDividendException(
                DIVIDEND_NOT_FOUND_EXCEPTION_MESSAGE + dto);
        Optional<DividendEntity> optionalEntity = repository
                .findByTickerAndExDateAndPortfolioId(dto.getTicker(), dto.getExDate(), dto.getPortfolioId());
        boolean anotherEntityExists = optionalEntity.isPresent()
                && !optionalEntity.get().getId().equals(dto.getId());
        if (anotherEntityExists) throw new DividendExistsDividendException(
                DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE + dto);
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        boolean anotherEntityExists = repository
                .existsByTickerAndExDateAndPortfolioId(
                        dto.getTicker(), dto.getExDate(), dto.getPortfolioId());
        if (anotherEntityExists) throw new DividendExistsDividendException(
                DIVIDEND_EXISTS_IN_PORTFOLIO_EXCEPTION_MESSAGE + dto);
    }

}
