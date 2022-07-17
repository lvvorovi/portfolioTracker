package portfolioTracker.portfolio.validation.rule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNameValidationException;

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

