package com.portfolioTracker.model.transaction.service;

import com.portfolioTracker.contract.ModelMapperContract;
import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.model.transaction.repository.TransactionRepository;
import com.portfolioTracker.model.transaction.validation.exception.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
public class TransactionService {

    private final ValidationService<TransactionRequestDto> validationService;
    private final TransactionRepository repository;
    private final ModelMapperContract<TransactionEntity, TransactionRequestDto,
            TransactionResponseDto> mapper;

    public TransactionService(
            ValidationService<TransactionRequestDto> validationService,
            TransactionRepository repository,
            ModelMapperContract<TransactionEntity,
                    TransactionRequestDto, TransactionResponseDto> mapper) {
        this.validationService = validationService;
        this.repository = repository;
        this.mapper = mapper;
    }

    public TransactionResponseDto save(@NotNull TransactionRequestDto dtoRequest) {
        validationService.validate(dtoRequest);
        var entity = mapper.toEntity(dtoRequest);
        return mapper.toDto(repository.save(entity));
    }

    public List<TransactionResponseDto> saveAll(@NotNull List<TransactionRequestDto> requestDtos) {
        requestDtos.forEach(validationService::validate);
        List<TransactionEntity> entities = requestDtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        entities = repository.saveAll(entities);
        return entities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto findById(@NotNull Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException("no entity with id " + id + " found"));
        return mapper.toDto(entity);
    }

    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public TransactionResponseDto update(@NotNull TransactionRequestDto dtoRequest) {
        validationService.validate(dtoRequest);
        if (dtoRequest.getId() == null)
            throw new TransactionException("id shall not be null for method update()");

        if (existsById(dtoRequest.getId())) {
            var entity = mapper.toEntity(dtoRequest);
            return mapper.toDto(repository.save(entity));

        } else {
            throw new TransactionException("EquityTransaction with id " +
                    dtoRequest.getId() + " was not found");
        }
    }

    public Boolean existsById(@NotNull Long id) {
        return repository.existsById(id);
    }

    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

}
