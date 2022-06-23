package com.portfolioTracker.model.portfolio.service;

import com.portfolioTracker.core.contract.ApiCurrencyService;
import com.portfolioTracker.core.contract.ApiTickerService;
import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.portfolio.validation.PortfolioValidationService;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioNotFoundException;
import com.portfolioTracker.model.transaction.service.TransactionService;
import com.portfolioTracker.model.transaction.validation.exception.PortfolioNotFoundTransactionException;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class PortfolioService {

    private final PortfolioValidationService validationService;
    private final PortfolioRepository repository;
    private final ModelMapperContract<PortfolioEntity, PortfolioRequestDto,
            PortfolioResponseDto> mapper;
//    private final DividendService dividendService;
    private final TransactionService transactionService;
    private final ApiTickerService apiTickerService;
    private final ApiCurrencyService apiCurrencyService;

    public PortfolioService(
            PortfolioValidationService validationService,
            PortfolioRepository repository,
            ModelMapperContract<PortfolioEntity, PortfolioRequestDto, PortfolioResponseDto> mapper, /*DividendService dividendService, */ApiTickerService apiTickerService, TransactionService transactionService, ApiCurrencyService apiCurrencyService) {
        this.validationService = validationService;
        this.repository = repository;
        this.mapper = mapper;
//        this.dividendService = dividendService;
        this.apiTickerService = apiTickerService;
        this.transactionService = transactionService;
        this.apiCurrencyService = apiCurrencyService;
    }

    public PortfolioResponseDto save(@NotNull PortfolioRequestDto requestDto) {
        validationService.validate(requestDto);
        return mapper.toDto(repository.save(mapper.toEntity(requestDto)));
    }

    public List<PortfolioResponseDto> saveAll(@NotNull List<PortfolioRequestDto> requestDtoList) {
        requestDtoList.forEach(validationService::validate);
        List<PortfolioEntity> entityList = requestDtoList.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        List<PortfolioEntity> savedEntityList = repository.saveAll(entityList);
        return savedEntityList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PortfolioResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PortfolioResponseDto findById(@NumberFormat Long id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        return mapper.toDto(entity);
    }

    public void deleteById(@NumberFormat Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new PortfolioNotFoundTransactionException("Portfolio with id " + id + " was not found");
        }
    }

    public PortfolioResponseDto update(@NotNull PortfolioRequestDto requestDto) {
        validationService.validate(requestDto);
        PortfolioEntity entity = mapper.toEntity(requestDto);
        PortfolioEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    public Boolean existsByName(@NotBlank String name) {
        return repository.existsByName(name);
    }

    public PortfolioResponseDto findByName(@NotBlank String name) {
        PortfolioEntity foundEntity = repository.findByName(name).orElseThrow(() -> new PortfolioNotFoundException("Portfolio with " +
                "name " + name + " was not found"));
        return mapper.toDto(foundEntity);
    }

    @Async
    @Scheduled(fixedRateString = "PT1M")
    void loadTickersToContext() {
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

}
