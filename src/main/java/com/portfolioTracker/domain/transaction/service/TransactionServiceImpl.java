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
    public TransactionDtoResponse save(TransactionDtoCreateRequest dtoRequest) {
        validationService.validate(dtoRequest);
        TransactionEntity entity = mapper.createToEntity(dtoRequest);
        TransactionEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        return requestDtoList.stream()
                .map(mapper::createToEntity)
                .map(repository::save)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDtoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDtoResponse findById(Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException("no entity with id " + id + " found"));
        return mapper.toDto(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
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
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

}
