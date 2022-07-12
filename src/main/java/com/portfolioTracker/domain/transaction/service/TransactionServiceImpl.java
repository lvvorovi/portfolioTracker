package com.portfolioTracker.domain.transaction.service;

import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.mapper.TransactionMapper;
import com.portfolioTracker.domain.transaction.repository.TransactionRepository;
import com.portfolioTracker.domain.transaction.validation.TransactionValidationService;
import com.portfolioTracker.domain.transaction.validation.exception.TransactionException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidationService validationService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public TransactionDtoResponse findById(Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException("Transaction not found with id :" + id));
        return mapper.toDto(entity);
    }

    @Override
    @PreAuthorize("#portfolioId == null or @portfolioServiceImpl.isPrincipalOwnerOfResource(#portfolioId)")
    public List<TransactionDtoResponse> findAll(Long portfolioId) {
        if (portfolioId == null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return repository.findAllByUsername(username).stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findAllByPortfolioId(portfolioId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("#dtoRequest.username == authentication.name " +
            " and " +
            "@portfolioServiceImpl.isPrincipalOwnerOfResource(#dtoRequest.portfolioId)")
    public TransactionDtoResponse save(TransactionDtoCreateRequest dtoRequest) {
        validationService.validate(dtoRequest);
        TransactionEntity entity = mapper.createToEntity(dtoRequest);
        TransactionEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    @PreFilter("filterObject.username == authentication.name" +
            " and " +
            "@portfolioServiceImpl.isPrincipalOwnerOfResource(filterObject.portfolioId)")
    public List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        return requestDtoList.stream()
                .map(mapper::createToEntity)
                .map(repository::save)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("#dtoRequest.username == authentication.name" +
            " and " +
            "@portfolioServiceImpl.isPrincipalOwnerOfResource(#dtoRequest.portfolioId)" +
            " and " +
            "@transactionServiceImpl.isPrincipalOwnerOfResource(#dtoRequest.id)")
    public TransactionDtoResponse update(TransactionDtoUpdateRequest dtoRequest) {
        validationService.validate(dtoRequest);
        if (dtoRequest.getId() == null)
            throw new TransactionException("id cannot not be null for method update()");

        if (existsById(dtoRequest.getId())) {
            TransactionEntity entity = mapper.updateToEntity(dtoRequest);
            TransactionEntity savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);

        } else {
            throw new TransactionException("EquityTransaction with id " +
                    dtoRequest.getId() + " was not found");
        }
    }

    @Override
    @PreAuthorize("@transactionServiceImpl.isPrincipalOwnerOfResource(#id)")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

    @Override
    public boolean isPrincipalOwnerOfResource(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new TransactionException("Transaction not found with id :" + id))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
