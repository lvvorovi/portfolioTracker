package com.portfolioTracker.model.dto.event;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.contract.CurrencyRateDto;
import com.portfolioTracker.contract.EventMapperContract;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.core.validation.annotation.AmountOfMoney;
import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.yahooModule.dto.YahooSplitEventDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@Service
public class EventDtoService {

    private final EventMapperContract eventMapper;
    private final ApiCurrencyService apiCurrencyRateService;
    private final ApiTickerService apiTickerService;

    public EventDtoService(EventMapperContract eventMapper, ApiCurrencyService apiCurrencyRateService, ApiTickerService apiTickerService) {
        this.eventMapper = eventMapper;
        this.apiCurrencyRateService = apiCurrencyRateService;
        this.apiTickerService = apiTickerService;
    }

    public Map<String, List<EventDto>> getMapOfTickerAndEventLists(@NotNull PortfolioResponseDto portfolio) {
        List<EventDto> eventList = new ArrayList<>();

        List<TransactionResponseDto> transactionList = portfolio.getTransactionList();
        Map<String, List<YahooSplitEventDto>> splitEventMap = getSplitEventMapForTransactionList(transactionList);
        transactionList = adjustTransactionListToSplits(transactionList, splitEventMap);
        transactionList = adjustTransactionListToCurrencyRate(transactionList, portfolio.getCurrency());

        List<DividendResponseDto> dividendList = portfolio.getDividendList();
        adjustDividendListToCurrencyRate(dividendList, portfolio.getCurrency());

        eventList.addAll(transactionList.stream()
                .map(eventMapper::transactionToEvent)
                .collect(Collectors.toList()));

        eventList.addAll(dividendList.stream()
                .map(eventMapper::dividendToEvent)
                .collect(Collectors.toList()));

        Map<String, List<EventDto>> eventMap = sortEventListToMapByTickers(eventList);
        eventMap.forEach((ticker, listOfEvents) -> listOfEvents.sort(Comparator.comparing(EventDto::getDate)));
        return eventMap;
    }

    public TransactionResponseDto eventToTransaction(@NotNull EventDto event) {
        return eventMapper.eventToTransaction(event);
    }

    public DividendResponseDto eventToDividend(@NotNull EventDto event) {
        return eventMapper.eventToDividend(event);
    }

    private Map<String, List<EventDto>> sortEventListToMapByTickers(@NotNull List<EventDto> eventList) {
        Map<String, List<EventDto>> eventMapByTicker = new TreeMap<>();

        eventList.forEach(event -> {
            if (eventMapByTicker.containsKey(event.getTicker())) {
                eventMapByTicker.get(event.getTicker()).add(event);
            } else {
                List<EventDto> newEventListDto = new ArrayList<>();
                newEventListDto.add(event);
                eventMapByTicker.put(event.getTicker(), newEventListDto);
            }
        });

        return eventMapByTicker;
    }

    private List<TransactionResponseDto> adjustTransactionListToCurrencyRate(
            @NotNull List<TransactionResponseDto> transactionList,
            @Currency String portfolioCurrency) {
        transactionList = transactionList.stream().peek(transaction -> {
                    String transactionCurrency = apiTickerService.getTickerCurrency(transaction.getTicker());

                    CurrencyRateDto currencyRate = apiCurrencyRateService.getRateForCurrencyPairOnDate(
                                    portfolioCurrency, transactionCurrency, transaction.getDate());

                    BigDecimal transactionCurrencyRateClientBuys = currencyRate.getRateClientBuys();
                    BigDecimal transactionCurrencyRateClientSells = currencyRate.getRateClientSells();

                    if (transaction.getType().equals(EventType.BUY)) {
                        transaction.setBought(
                                adjustToCurrencyRate(transaction.getBought(), transactionCurrencyRateClientBuys));
                        transaction.setCommission(
                                adjustToCurrencyRate(transaction.getCommission(), transactionCurrencyRateClientBuys));
                    }

                    if (transaction.getType().equals(EventType.SELL)) {
                        transaction.setSold(
                                adjustToCurrencyRate(transaction.getSold(), transactionCurrencyRateClientSells));
                        transaction.setCommission(
                                adjustToCurrencyRate(transaction.getCommission(), transactionCurrencyRateClientSells));
                    }
                })
                .collect(Collectors.toList());
        return transactionList;
    }

    private void adjustDividendListToCurrencyRate(
            @NotNull List<DividendResponseDto> dividendList,
            @Currency String portfolioCurrency) {
        dividendList.forEach(dividend -> {
            String dividendCurrency = apiTickerService.getTickerCurrency(dividend.getTicker());
            CurrencyRateDto currencyRate = apiCurrencyRateService
                    .getRateForCurrencyPairOnDate(portfolioCurrency, dividendCurrency, dividend.getDate());
            BigDecimal dividendCurrencyRate = currencyRate.getRateClientSells();
            dividend.setAmount(adjustToCurrencyRate(dividend.getAmount(), dividendCurrencyRate));
        });
    }

    private BigDecimal adjustToCurrencyRate(
            @AmountOfMoney BigDecimal input,
            @AmountOfMoney BigDecimal currencyRate) {
        return input.divide(currencyRate, 2, RoundingMode.HALF_DOWN);
    }

    private Map<String, List<YahooSplitEventDto>> getSplitEventMapForTransactionList(
            @NotNull List<TransactionResponseDto> transactionList) {
        Map<String, List<YahooSplitEventDto>> splitEventMap = new HashMap<>();
        transactionList.forEach(transaction ->
                splitEventMap.put(
                        transaction.getTicker(),
                        apiTickerService.getSplitEventList(transaction.getTicker())));
        return splitEventMap;
    }

    private List<TransactionResponseDto> adjustTransactionListToSplits(
            @NotNull List<TransactionResponseDto> transactionList,
            @NotNull Map<String, List<YahooSplitEventDto>> splitEventMap) {
        return transactionList.stream()
                .peek(transaction -> splitEventMap.forEach((ticker, eventList) -> {
                    if (transaction.getTicker().equals(ticker)) {
                        eventList.forEach(event -> {
                            if (transaction.getDate().isBefore(event.getDate())) {
                                transaction.setShares(transaction.getShares().multiply(event.getNumerator())
                                        .divide(event.getDenominator(), 0, RoundingMode.HALF_UP));
                                transaction.setPrice(transaction.getPrice().multiply(event.getDenominator())
                                        .divide(event.getNumerator(), 2, RoundingMode.HALF_UP));
                            }
                        });
                    }
                }))
                .collect(Collectors.toList());
    }

}
