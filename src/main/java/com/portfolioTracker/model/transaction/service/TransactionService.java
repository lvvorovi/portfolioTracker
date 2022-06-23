package com.portfolioTracker.model.transaction.service;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.core.contract.ValidationService;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.model.transaction.repository.TransactionRepository;
import com.portfolioTracker.model.transaction.validation.exception.TransactionException;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
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
        TransactionEntity entity = mapper.toEntity(dtoRequest);
        TransactionEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public List<TransactionResponseDto> saveAll(@NotNull List<TransactionRequestDto> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        List<TransactionEntity> entityList = requestDtoList.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        List<TransactionEntity> savedEntityList = repository.saveAll(entityList);
        return savedEntityList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto findById(@NumberFormat Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionException("no entity with id " + id + " found"));
        return mapper.toDto(entity);
    }

    public void deleteById(@NumberFormat Long id) {
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
            TransactionEntity entity = mapper.toEntity(dtoRequest);
            TransactionEntity savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);

        } else {
            throw new TransactionException("EquityTransaction with id " +
                    dtoRequest.getId() + " was not found");
        }
    }

    public Boolean existsById(@NumberFormat Long id) {
        return repository.existsById(id);
    }

    public List<String> findAllUniqueTickers() {
        return repository.findAllUniqueTickers();
    }

}
