package com.portfolioTracker.domain.portfolio.service;

import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.dividend.repository.DividendRepository;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.mapper.PortfolioMapper;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.PortfolioValidationService;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.repository.TransactionRepository;
import com.portfolioTracker.domain.transaction.validation.exception.PortfolioNotFoundTransactionException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioValidationService validationService;
    private final PortfolioRepository repository;
    private final PortfolioMapper mapper;
    private final TransactionRepository transactionRepository;
    private final DividendRepository dividendRepository;

    @Override
    public PortfolioDtoResponse findByIdSkipEvents(Long id) {
        PortfolioEntity entity = repository.findByIdSkipEvents(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        return mapper.toDto(entity);
    }

    @Override
    public PortfolioDtoResponse findByIdWithEvents(Long id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        return mapper.toDto(entity);
    }

    @Override
    public ArrayList<PortfolioDtoResponse> findAllWithEvents() {
        return repository.findAll().parallelStream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<PortfolioDtoResponse> findAllSkipEvents() {
       return repository.findAllSKipEvents().parallelStream()
               .map(mapper::toDto)
               .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity requestEntity = mapper.createToEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity requestEntity = mapper.updateToEntity(requestDto);
        List<TransactionEntity> transactionEntityList = transactionRepository.findAllByPortfolioId(requestDto.getId());
        List<DividendEntity> dividendEntityList = dividendRepository.findAllByPortfolioId(requestDto.getId());
        requestEntity.setTransactionEntityList(transactionEntityList);
        requestEntity.setDividendEntityList(dividendEntityList);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        // No need to check for existence. Done by Spring Security through this.isOwner()
        repository.deleteById(id);
    }

    @Override
    public boolean isOwner(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        "Portfolio with id " + id + " was not found"))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

    @Override
    public List<String> findAllPortfolioCurrencies() {
        return repository.findAllPortfolioCurrencies();
    }
}
