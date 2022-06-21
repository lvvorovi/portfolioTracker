package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioNameValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
@Priority(0)
@Validated
public class PortfolioNameValidationRule implements PortfolioValidationRule {

    private final PortfolioRepository portfolioRepository;

    public PortfolioNameValidationRule(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public void validate(@NotNull PortfolioRequestDto requestDto) {
        if (requestToSave(requestDto)) {
            if (portfolioRepository.existsByName(requestDto.getName()))
                throw new PortfolioNameValidationException("Portfolio name " + requestDto.getName() +
                        " already exists");
        }
        if (requestToUpdate(requestDto)) {
            Optional<PortfolioEntity> optionalEntity = portfolioRepository.findByName(requestDto.getName());
            if (optionalEntity.isPresent() && !optionalEntity.get().getId().equals(requestDto.getId())) {
                throw new PortfolioNameValidationException("Portfolio name " + requestDto.getName() +
                        " already exists");
            }
        }
    }

    private boolean requestToSave(PortfolioRequestDto requestDto) {
        return requestDto.getId() == null;
    }

    private boolean requestToUpdate(PortfolioRequestDto requestDto) {
        return requestDto.getId() != null;
    }
}
