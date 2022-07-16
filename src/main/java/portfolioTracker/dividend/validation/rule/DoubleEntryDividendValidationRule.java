package portfolioTracker.dividend.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.repository.DividendRepository;
import portfolioTracker.dividend.validation.exception.DividendAlreadyExists;

import javax.annotation.Priority;
import java.util.Optional;

@Component
@AllArgsConstructor
@Priority(0)
public class DoubleEntryDividendValidationRule implements DividendValidationRule {

    private final DividendRepository repository;

    @Override
    public void validate(DividendDtoUpdateRequest dto) {
        Optional<DividendEntity> optionalEntity = repository
                .findByTickerAndExDateAndPortfolioId(dto.getTicker(), dto.getExDate(), dto.getPortfolioId());
        boolean anotherEntityExists = optionalEntity.isPresent()
                && !optionalEntity.get().getId().equals(dto.getId());
        if (anotherEntityExists) throw new DividendAlreadyExists(
                "Following Dividend event already registered in requested portfolio" + dto);
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        boolean anotherEntityExists = repository
                .existsByTickerAndExDateAndPortfolioId(
                        dto.getTicker(), dto.getExDate(), dto.getPortfolioId());
        if (anotherEntityExists) throw new DividendAlreadyExists(
                "Following Dividend event already registered in requested portfolio" + dto);
    }

}
