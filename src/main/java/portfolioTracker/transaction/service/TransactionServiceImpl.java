package portfolioTracker.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portfolioTracker.portfolio.mapper.relationMapper.PortfolioRelationMappingService;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.repository.TransactionRepository;
import portfolioTracker.transaction.validation.exception.TransactionNotFoundTransactionException;
import portfolioTracker.transaction.validation.service.TransactionCreateValidationService;
import portfolioTracker.transaction.validation.service.TransactionUpdateValidationService;
import portfolioTracker.transaction.validation.exception.TransactionException;

import java.util.List;
import java.util.UUID;

import static portfolioTracker.core.ExceptionErrors.TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionUpdateValidationService updateRequestValidationService;
    private final TransactionCreateValidationService createRequestValidationService;
    private final TransactionRepository repository;
    private final PortfolioRelationMappingService mappingService;

    @Override
    public TransactionDtoResponse save(TransactionDtoCreateRequest requestDto) {
        createRequestValidationService.validate(requestDto);
        TransactionEntity requestEntity = mappingService.createToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        TransactionEntity savedEntity = repository.save(requestEntity);
        return mappingService.toDto(savedEntity);
    }

    @Override
    public List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList) {
        requestDtoList.forEach(createRequestValidationService::validate);

        return requestDtoList.parallelStream()
                .map(mappingService::createToEntity)
                .peek(entity -> entity.setId(UUID.randomUUID().toString()))
                .map(repository::save)
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public TransactionDtoResponse findById(String id) {
        return repository.findById(id)
                .map(mappingService::toDto)
                .orElseThrow(() -> new TransactionNotFoundTransactionException(
                        TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id));
    }

    @Override
    public List<TransactionDtoResponse> findAllByUsername(String username) {
        return repository.findAllByUsername(username).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public List<TransactionDtoResponse> findAllByPortfolioId(String id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mappingService::toDto)
                .toList();
    }

    @Override
    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

    @Override
    public TransactionDtoResponse update(TransactionDtoUpdateRequest requestDto) {
        updateRequestValidationService.validate(requestDto);
        TransactionEntity entity = mappingService.updateToEntity(requestDto);
        TransactionEntity savedEntity = repository.save(entity);
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
                .orElseThrow(() -> new TransactionNotFoundTransactionException(
                        TRANSACTION_ID_NOT_FOUND_EXCEPTION_MESSAGE + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
