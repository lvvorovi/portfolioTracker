package com.portfolioTracker.model.dividend.validation.rule;

import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.repository.DividendRepository;
import com.portfolioTracker.model.dividend.validation.exception.DividendAlreadyExists;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
@Priority(0)
@Validated
public class DoubleEntryDividendValidationRule implements DividendValidationRule {

    private final DividendRepository repository;

    public DoubleEntryDividendValidationRule(DividendRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized void validate(@NotNull DividendRequestDto dtoRequest) {
        Optional<DividendEntity> optionalEntity = repository
                .findByTickerAndExDate(dtoRequest.getTicker(), dtoRequest.getExDate());//TODO andPortfolioId
        boolean anotherEntityExistsOnUpdateRequest = optionalEntity.isPresent()
                && !optionalEntity.get().getId().equals(dtoRequest.getId());
        boolean anotherEntityExistsOnSaveRequest = optionalEntity.isPresent() && dtoRequest.getId() == null;

        if (anotherEntityExistsOnSaveRequest || anotherEntityExistsOnUpdateRequest) throw new
                DividendAlreadyExists("Dividend event with ticker " + dtoRequest.getTicker() +
                "and exDate " + dtoRequest.getExDate() + " already exists");
    }

}
