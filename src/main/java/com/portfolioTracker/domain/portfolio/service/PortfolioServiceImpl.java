package com.portfolioTracker.domain.portfolio.service;

import com.portfolioTracker.domain.currency.ApiCurrencyService;
import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.mapper.PortfolioMapper;
import com.portfolioTracker.domain.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.domain.portfolio.validation.PortfolioValidationService;
import com.portfolioTracker.domain.ticker.ApiTickerService;
import com.portfolioTracker.domain.transaction.service.TransactionService;
import com.portfolioTracker.domain.transaction.validation.exception.PortfolioNotFoundTransactionException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public PortfolioDtoResponse findById(Long id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        return mapper.toDto(entity);
    }

    @Override
    public List<PortfolioDtoResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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
        PortfolioEntity entity = mapper.updateToEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

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

    @Override
    public boolean isPrincipalOwnerOfResource(Long id) {
        String principalUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String resourceUsername = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException(
                        "Portfolio with id " + id + " was not found"))
                .getUsername();

        return principalUsername.equalsIgnoreCase(resourceUsername);
    }

//    @Override
//    public boolean isPrincipalOwnerOfResourceList(List<String> id) {
//        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//    }
}
