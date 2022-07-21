package portfolioTracker.portfolio.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;
import portfolioTracker.portfolio.repository.PortfolioRepository;
import portfolioTracker.portfolio.validation.exception.PortfolioNotFoundPortfolioException;
import portfolioTracker.portfolio.validation.service.PortfolioCreateRequestValidationService;
import portfolioTracker.portfolio.validation.service.PortfolioUpdateRequestValidationService;
import portfolioTracker.transaction.validation.exception.PortfolioNotFoundTransactionException;

import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static portfolioTracker.core.ExceptionErrors.PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioUpdateRequestValidationService validationServiceUpdateRequest;
    private final PortfolioCreateRequestValidationService validationServiceCreateRequest;
    private final PortfolioRepository repository;
    private final PortfolioRelationMappingService mappingService;

    @Override
    public PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto) {
        validationServiceCreateRequest.validate(requestDto);
        PortfolioEntity requestEntity = mappingService.createToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mappingService.toDto(savedEntity);
    }

    @Override
    public PortfolioDtoResponse findById(String id) {
        return repository.findById(id)
                .map(mappingService::toDto)
                .orElseThrow(() -> new PortfolioNotFoundPortfolioException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
    }

    @Override
    public PortfolioDtoResponse findByIdIncludeEvents(String id) {
        return repository.findByIdIncludeEvents(id)
                .map(mappingService::toDto)
                .orElseThrow(() -> new PortfolioNotFoundPortfolioException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
    }

    @Override
    public List<PortfolioDtoResponse> findAllByUsername(String username) {
        return repository.findAllByUsername(username).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public List<PortfolioDtoResponse> findAllByUsernameIncludeEvents(String username) {
        return repository.findAllByUsernameIncludeEvents(username).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public List<String> findAllPortfolioCurrencies() {
        return repository.findAllPortfolioCurrencies();
    }

    @Override
    public PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto) {
        validationServiceUpdateRequest.validate(requestDto);
        PortfolioEntity requestEntity = mappingService.updateToEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mappingService.toDto(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        // No need to check for existence. Done by Spring Security through this.isOwner()
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(String id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundPortfolioException(
                        PORTFOLIO_ID_NOT_FOUND_EXCEPTION_MESSAGE + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }
}
