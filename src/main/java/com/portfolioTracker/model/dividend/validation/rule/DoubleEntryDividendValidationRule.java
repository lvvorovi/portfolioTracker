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

@Validated
@Component
@Priority(0)
public class DoubleEntryDividendValidationRule implements DividendValidationRule {

    private final DividendRepository repository;

    public DoubleEntryDividendValidationRule(DividendRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized void validate(@NotNull DividendRequestDto dtoRequest) {
        Optional<DividendEntity> entityExists = repository
                .findByTickerAndExDate(dtoRequest.getTicker(), dtoRequest.getExDate());
        boolean anotherEntityExistsOnUpdateRequest = entityExists.isPresent()
                && !entityExists.get().getId().equals(dtoRequest.getId());
        boolean anotherEntityExistsOnSaveRequest = entityExists.isPresent() && dtoRequest.getId() == null;

        if (anotherEntityExistsOnSaveRequest || anotherEntityExistsOnUpdateRequest) throw new
                DividendAlreadyExists("Dividend event with ticker " + dtoRequest.getTicker() +
                "and exDate " + dtoRequest.getExDate() + " already exists");
    }

}
