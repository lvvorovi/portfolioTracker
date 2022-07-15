package com.portfolioTracker.domain.portfolio.validation.rule;

import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.exception.PortfolioNameValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.Optional;

@Component
@AllArgsConstructor
@Priority(0)
public class PortfolioNameValidationRule implements PortfolioValidationRule {

    private final PortfolioRepository portfolioRepository;

    @Override
    public void validate(PortfolioDtoUpdateRequest requestDto) {
        Optional<PortfolioEntity> optionalEntity = portfolioRepository.findByName(requestDto.getName());
        if (optionalEntity.isPresent() && !optionalEntity.get().getId().equals(requestDto.getId())) {
            throw new PortfolioNameValidationException("Portfolio name " + requestDto.getName() +
                    " already exists");
        }
    }

    @Override
    public void validate(PortfolioDtoCreateRequest requestDto) {
        if (portfolioRepository.existsByName(requestDto.getName()))
            throw new PortfolioNameValidationException("Portfolio name " + requestDto.getName() +
                    " already exists");
    }
}

