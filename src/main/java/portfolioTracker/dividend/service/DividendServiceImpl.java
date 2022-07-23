package portfolioTracker.dividend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolioTracker.dividend.repository.service.DividendRepositoryService;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.validation.service.DividendCreateRequestValidationService;
import portfolioTracker.dividend.validation.exception.DividendNotFoundDividendException;
import portfolioTracker.dividend.validation.service.DividendUpdateRequestValidationService;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;

import java.util.List;
import java.util.UUID;

import static portfolioTracker.core.ExceptionErrors.DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@AllArgsConstructor
public class DividendServiceImpl implements DividendService {

    private final DividendCreateRequestValidationService createRequestValidationService;
    private final DividendUpdateRequestValidationService updateRequestValidationService;
    private final PortfolioRelationMappingService mappingService;
    private final DividendRepositoryService repository;


    @Override
    public DividendDtoResponse save(DividendDtoCreateRequest requestDto) {
        createRequestValidationService.validate(requestDto);
        DividendEntity requestEntity = mappingService.createToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        DividendEntity savedEntity = repository.save(requestEntity);
        return mappingService.toDto(savedEntity);
    }

    @Override
    @Transactional
    public List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList) {
        return requestDtoList.parallelStream()
                .peek(createRequestValidationService::validate)
                .map(mappingService::createToEntity)
                .peek(entity -> entity.setId(UUID.randomUUID().toString()))
                .map(repository::save)
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public DividendDtoResponse findById(String id) {
        return repository.findById(id)
                .map(mappingService::toDto)
                .orElseThrow(() ->
                        new DividendNotFoundDividendException(DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
    }

    @Override
    public List<DividendDtoResponse> findAllByUsername(String username) {
        return repository.findAllByUsername(username).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public List<DividendDtoResponse> findAllByPortfolioId(String id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public DividendDtoResponse update(DividendDtoUpdateRequest requestDto) {
        updateRequestValidationService.validate(requestDto);
        DividendEntity requestEntity = mappingService.updateToEntity(requestDto);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mappingService.toDto(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        // No need to verify for existence. Will be verified by this.IsOwner(),
        // called by Spring Security through @PreAuthorize().
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(String id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new DividendNotFoundDividendException(
                        DIVIDEND_ID_NOT_FOUND_EXCEPTION_MESSAGE + id))
                .getUsername();
        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
