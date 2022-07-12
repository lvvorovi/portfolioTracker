package com.portfolioTracker.domain.dividend.validation.rule;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.repository.DividendRepository;
import com.portfolioTracker.domain.dividend.validation.exception.DividendAlreadyExists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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
        if (anotherEntityExists) throw new
                DividendAlreadyExists("Dividend event with ticker " + dto.getTicker() +
                "and exDate " + dto.getExDate() + " already exists");
    }

    @Override
    public void validate(DividendDtoCreateRequest dto) {
        boolean anotherEntityExists = repository
                .existsByTickerAndExDateAndPortfolioId(dto.getTicker(), dto.getExDate(), dto.getPortfolioId());
        if (anotherEntityExists) throw new
                DividendAlreadyExists("Dividend event with ticker " + dto.getTicker() +
                "and exDate " + dto.getExDate() + " already exists");
    }

}
