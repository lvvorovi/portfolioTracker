package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioNameValidationException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class PortfolioNameValidationRule implements PortfolioValidationRule {

    private final PortfolioRepository repository;

    public PortfolioNameValidationRule(PortfolioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(@NotNull PortfolioRequestDto portfolioRequestDto) {

        if (portfolioRequestDto.getId() == null) {
            if (repository.existsByName(portfolioRequestDto.getName()))
                throw new PortfolioNameValidationException("Portfolio name " + portfolioRequestDto.getName() +
                        " already exists");
        } else {
            Optional<PortfolioEntity> entityOptional = repository.findByName(portfolioRequestDto.getName());
            if (entityOptional.isPresent()
                    && !entityOptional.get().getName().equals(portfolioRequestDto.getName())) {
                throw new PortfolioNameValidationException("Portfolio name " + portfolioRequestDto.getName() +
                        " already exists");
            }
        }

    }
}
