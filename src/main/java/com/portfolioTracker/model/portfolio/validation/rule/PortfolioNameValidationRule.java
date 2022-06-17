package com.portfolioTracker.model.portfolio.validation.rule;

import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import com.portfolioTracker.model.portfolio.service.PortfolioServiceFactory;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioNameValidationException;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;

@Component
@Priority(0)
public class PortfolioNameValidationRule implements PortfolioValidationRule {

    @Override
    public void validate(@NotNull PortfolioRequestDto requestDto) {
        PortfolioService portfolioService = PortfolioServiceFactory.getPortfolioService();
        if (requestToSave(requestDto)) {
            if (portfolioService.existsByName(requestDto.getName()))
                throw new PortfolioNameValidationException("Portfolio name " + requestDto.getName() +
                        " already exists");
        }
        if (requestToUpdate(requestDto)) {
            PortfolioResponseDto responseDto = portfolioService.findByName(requestDto.getName());
            if (!responseDto.getId().equals(requestDto.getId())) {
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
