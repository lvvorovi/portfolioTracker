package com.portfolioTracker.model.portfolio.service;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.contract.ModelMapperContract;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dividend.service.DividendService;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.portfolio.validation.PortfolioValidationService;
import com.portfolioTracker.model.portfolio.validation.exception.PortfolioNotFoundException;
import com.portfolioTracker.model.transaction.service.TransactionService;
import com.portfolioTracker.model.transaction.validation.exception.PortfolioNotFoundTransactionException;
import com.portfolioTracker.core.validation.annotation.ModelName;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
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

    public PortfolioService get() {
        return this;
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
        List<PortfolioResponseDto> savedResponseDtoList = savedEntityList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
//        savedResponseDtoList.forEach(portfolio -> portfolio.setDividendList(findAllDividendResponseDto(portfolio)));
        return savedResponseDtoList;
    }

    public List<PortfolioResponseDto> findAll() {
        List<PortfolioResponseDto> responseDtoList = repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

//        responseDtoList.forEach(portfolio ->
//                portfolio.setDividendList(findAllDividendResponseDto(portfolio)));
        return responseDtoList;
    }

    public PortfolioResponseDto findById(@NumberFormat Long id) {
        PortfolioEntity entity = repository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundTransactionException("Portfolio with id "
                        + id + " was not found"));
        PortfolioResponseDto portfolioResponseDto = mapper.toDto(entity);
//        portfolioResponseDto.setDividendList(findAllDividendResponseDto(portfolioResponseDto));
        return portfolioResponseDto;
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
        PortfolioResponseDto savedResponseDto = mapper.toDto(savedEntity);
//        savedResponseDto.setDividendList(findAllDividendResponseDto(savedResponseDto));
        return savedResponseDto;
    }

    public Boolean existsByName(@ModelName String name) {
        return repository.existsByName(name);
    }

    public PortfolioResponseDto findByName(@ModelName String name) {
        PortfolioEntity foundEntity = repository.findByName(name).orElseThrow(() -> new PortfolioNotFoundException("Portfolio with " +
                "name " + name + " was not found"));
        return mapper.toDto(foundEntity);
    }



//    private List<DividendResponseDto> findAllDividendResponseDto(@NotNull PortfolioResponseDto portfolioResponseDto) {
//        Set<String> tickerSetInPortfolio = new HashSet<>();
//        portfolioResponseDto.getTransactionList().forEach(transaction ->
//                tickerSetInPortfolio.add(transaction.getTicker()));
//        return dividendService.findAllByTickerList(List.copyOf(tickerSetInPortfolio));
//    }

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
