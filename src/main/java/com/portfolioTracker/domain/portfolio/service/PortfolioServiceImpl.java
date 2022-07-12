package com.portfolioTracker.domain.portfolio.service;

import com.portfolioTracker.core.contract.ApiCurrencyService;
import com.portfolioTracker.core.contract.ApiTickerService;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.mapper.PortfolioMapper;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.PortfolioValidationService;
import com.portfolioTracker.domain.transaction.service.TransactionService;
import com.portfolioTracker.domain.transaction.validation.exception.PortfolioNotFoundTransactionException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioValidationService validationService;
    private final PortfolioRepository repository;
    private final PortfolioMapper mapper;
    private final TransactionService transactionService;
    private final ApiTickerService apiTickerService;
    private final ApiCurrencyService apiCurrencyService;

    @Override
    @PreAuthorize("#dto.username == authentication.name")
    public PortfolioDtoResponse save(PortfolioDtoCreateRequest dto) {
        validationService.validate(dto);
        PortfolioEntity requestEntity = mapper.createToEntity(dto);
        PortfolioEntity savedEntity = repository.save(requestEntity);
        return mapper.toDto(savedEntity);
    }

    @Override
    @PostFilter("filterObject.username == authentication.name")
    public List<PortfolioDtoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @PostFilter("filterObject.username == authentication.name")
    public PortfolioDtoResponse findById(Long id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("@portfolioServiceImpl.isPrincipalOwnerOfResource(#id)")
    public void deleteById(Long id) {
        PortfolioEntity entity = repository.findById(id).orElseThrow(() ->
                new PortfolioNotFoundTransactionException("Portfolio with id " + id + " was not found"));
        repository.deleteById(id);
    }

    @Override
    public PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity entity = mapper.updateToEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

/*    @Override
    public Boolean existsByName(String name) {
        return repository.existsByName(name);
    }*/

/*    @Override
    public PortfolioDtoResponse findByName(String name) {
        PortfolioEntity foundEntity = repository.findByName(name).orElseThrow(() -> new PortfolioNotFoundException("Portfolio with " +
                "name " + name + " was not found"));
        return mapper.toDto(foundEntity);
    }*/

    @Async
    @Scheduled(fixedRateString = "PT1M")
    @Override
    public void loadTickersToContext() {
        System.out.println(Thread.currentThread());
        List<String> tickerListFromDB = transactionService.findAllUniqueTickers();

        Set<String> transactionCurrencyList = tickerListFromDB.parallelStream()
                .map(apiTickerService::getTickerCurrency)
                .collect(Collectors.toSet());

        Set<String> portfolioCurrencyList = Set.copyOf(repository.findAllPortfolioCurrencies());
        portfolioCurrencyList.parallelStream()
                .forEach(portfolioCurrency -> transactionCurrencyList.parallelStream()
                        .forEach(transactionCurrency -> apiCurrencyService
                                .getRateForCurrencyPairOnDate(portfolioCurrency, transactionCurrency, LocalDate.now())));
    }

    public boolean isPrincipalOwnerOfResource(Long id) {
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceName = repository.findById(id).orElseThrow(() ->
                new PortfolioNotFoundTransactionException("Portfolio with id " + id + " was not found"))
                .getUsername();

        return principalName.equalsIgnoreCase(resourceName);
    }

}
