package com.portfolioTracker.model.dividend.service;

import com.portfolioTracker.contract.ModelMapperContract;
import com.portfolioTracker.contract.ValidationService;
import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dividend.repository.DividendRepository;
import com.portfolioTracker.model.dividend.validation.exception.DividendNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
public class DividendService {

    private final ValidationService<DividendRequestDto> validationService;
    private final ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> mapper;
    private final DividendRepository repository;

    public DividendService(ValidationService<DividendRequestDto> validationService, ModelMapperContract<DividendEntity, DividendRequestDto, DividendResponseDto> mapper, DividendRepository repository) {
        this.validationService = validationService;
        this.mapper = mapper;
        this.repository = repository;
    }

    public DividendResponseDto save(@NotNull DividendRequestDto requestDto) {
        validationService.validate(requestDto);
        DividendEntity returnedEntity = repository.save(mapper.toEntity(requestDto));
        return mapper.toDto(returnedEntity);
    }

    public List<DividendResponseDto> saveAll(@NotNull List<DividendRequestDto> requestDtos) {
        requestDtos.forEach(validationService::validate);
        List<DividendEntity> entities = requestDtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        entities = repository.saveAll(entities);
        return entities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DividendResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DividendResponseDto> findAllByTickerList(@NotNull List<String> tickerList) {
        List<DividendEntity> dividendList = new ArrayList<>();
        tickerList.forEach(ticker -> {
            dividendList.addAll(repository.findAllByTicker(ticker));
        });
        return dividendList.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public DividendResponseDto findById(@NotNull Long id) {
        DividendEntity entity = repository.findById(id).orElseThrow(() -> new
                DividendNotFoundException("DividendEvent with id " + id + " not found"));
        return mapper.toDto(entity);
    }

    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public DividendResponseDto update(@NotNull DividendRequestDto requestDto) {
        return save(requestDto);
    }

    public Boolean existsById(@NotNull Long id) {
        return repository.existsById(id);
    }

    public List<DividendResponseDto> findAllByTicker(@NotNull String ticker) {
        return repository.findAllByTicker(ticker).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
