package com.portfolioTracker.domain.dividend.service;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.mapper.DividendMapper;
import com.portfolioTracker.domain.dividend.repository.DividendRepository;
import com.portfolioTracker.domain.dividend.validation.DividendValidationService;
import com.portfolioTracker.domain.dividend.validation.exception.DividendNotFoundException;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.exception.PortfolioNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DividendServiceImpl implements DividendService {

    private final DividendValidationService validationService;
    private final DividendMapper mapper;
    private final DividendRepository repository;
    private final PortfolioRepository portfolioRepository;


    @Override
    public DividendDtoResponse save(DividendDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.createToEntity(requestDto);
        requestEntity.setPortfolio(portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException(
                        "Portfolio with requested id not found. id: " + requestDto.getPortfolioId())));
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        List<DividendEntity> requestEntityList = requestDtoList.parallelStream()
                .map(requestDto -> {
                    DividendEntity requestEntity = mapper.createToEntity(requestDto);
                    requestEntity.setPortfolio(portfolioRepository.findById(requestDto.getPortfolioId())
                            .orElseThrow(() -> new PortfolioNotFoundException(
                                    "Portfolio with requested id not found. id: " + requestDto.getPortfolioId())));
                    return requestEntity;
                })
                .collect(Collectors.toList());

        return requestEntityList.parallelStream()
                .map(repository::save)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DividendDtoResponse> findAll() {
        return repository.findAll().parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DividendDtoResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new
                DividendNotFoundException("DividendEvent with id " + id + " not found"));
    }

    @Override
    public List<DividendDtoResponse> findAllByPortfolioId(Long id) {
        return repository.findAllByPortfolioId(id).parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DividendDtoResponse update(DividendDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        DividendEntity requestEntity = mapper.updateToEntity(requestDto);
        PortfolioEntity portfolioEntity = portfolioRepository.findById(requestDto.getPortfolioId())
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with id: "
                        + requestDto.getPortfolioId()));
        requestEntity.setPortfolio(portfolioEntity);
        DividendEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new DividendNotFoundException("DividendEntity with id " + id + " not found"))
                .getUsername();
        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

}
