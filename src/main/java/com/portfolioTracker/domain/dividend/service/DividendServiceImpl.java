package com.portfolioTracker.domain.dividend.service;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.mapper.DividendMapper;
import com.portfolioTracker.domain.dividend.repository.DividendRepository;
import com.portfolioTracker.domain.dividend.validation.DividendValidationService;
import com.portfolioTracker.domain.dividend.validation.exception.DividendNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DividendServiceImpl implements DividendService {

    private final DividendValidationService validationService;
    private final DividendMapper mapper;
    private final DividendRepository repository;


    @Override
    public DividendDtoResponse save(DividendDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.createToEntity(requestDto);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        return requestDtoList.stream()
                .map(mapper::createToEntity)
                .map(repository::save)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DividendDtoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DividendDtoResponse> findAllByTickerList(List<String> tickerList) {
        List<DividendEntity> entityList = new ArrayList<>();
        tickerList.forEach(ticker -> entityList.addAll(repository.findAllByTicker(ticker)));
        return entityList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DividendDtoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new
                DividendNotFoundException("DividendEvent with id " + id + " not found"));
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
    public DividendDtoResponse update(DividendDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.updateToEntity(requestDto);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<DividendDtoResponse> findAllByTicker(String ticker) {
        return repository.findAllByTicker(ticker).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
