package com.portfolioTracker.model.dto.portfolioSummaryDto.service;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.contract.CurrencyRateResponse;
import com.portfolioTracker.contract.EventMapperContract;
import com.portfolioTracker.yahooModule.dto.YahooSplitEventDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Currency;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@Service
public class EventService {

    private final EventMapperContract eventMapper;
    private final ApiCurrencyService currencyRateService;
    private final ApiTickerService tickerService;

    public EventService(EventMapperContract eventMapper, ApiCurrencyService currencyRateService, ApiTickerService tickerService) {
        this.eventMapper = eventMapper;
        this.currencyRateService = currencyRateService;
        this.tickerService = tickerService;
    }

    public Map<String, List<Event>> getMapOfTickerAndEventLists(@NotNull PortfolioResponseDto portfolioResponseDto) {
        List<Event> eventList = new ArrayList<>();

        List<TransactionResponseDto> transactionList = portfolioResponseDto.getTransactionList();
        Map<String, List<YahooSplitEventDto>> splitEventMap = getSplitEventMapForTransactionList(transactionList);
        transactionList = adjustTransactionListToSplitEvents(transactionList, splitEventMap);
        transactionList = adjustTransactionListToCurrencyRate(transactionList, portfolioResponseDto.getCurrency());

        List<DividendResponseDto> dividendList = portfolioResponseDto.getDividendList();
        adjustDividendListToCurrencyRate(dividendList, portfolioResponseDto.getCurrency());

        eventList.addAll(transactionList.stream()
                .map(eventMapper::transactionToEvent)
                .collect(Collectors.toList()));

        eventList.addAll(dividendList.stream()
                .map(eventMapper::dividendToEvent)
                .collect(Collectors.toList()));

        Map<String, List<Event>> eventListMap = sortEventListToMapByTickers(eventList);
        eventListMap.forEach((ticker, listOfEvents) -> listOfEvents.sort(Comparator.comparing(Event::getDate)));
        return eventListMap;
    }

    public TransactionResponseDto eventToTransaction(@NotNull Event event) {
        return eventMapper.eventToTransaction(event);
    }

    public DividendResponseDto eventToDividend(@NotNull Event event) {
        return eventMapper.eventToDividend(event);
    }

    private Map<String, List<Event>> sortEventListToMapByTickers(@NotNull List<Event> eventList) {
        Map<String, List<Event>> eventMapByTicker = new TreeMap<>();

        eventList.forEach(event -> {
            if (eventMapByTicker.containsKey(event.getTicker())) {
                eventMapByTicker.get(event.getTicker()).add(event);
            } else {
                List<Event> newEventList = new ArrayList<>();
                newEventList.add(event);
                eventMapByTicker.put(event.getTicker(), newEventList);
            }
        });

        return eventMapByTicker;
    }

    private List<TransactionResponseDto> adjustTransactionListToCurrencyRate(@NotNull List<TransactionResponseDto> transactionList, @Currency String portfolioCurrency) {
        transactionList = transactionList.stream().peek(transaction -> {
                    String transactionCurrency = tickerService.getTickerCurrency(transaction.getTicker());
                    CurrencyRateResponse currencyDto = currencyRateService
                            .getRateForCurrencyPairOnDate(portfolioCurrency, transactionCurrency, transaction.getDate());
                    BigDecimal transactionCurrencyRateClientBuys = currencyDto.getRateClientBuys();
                    BigDecimal transactionCurrencyRateClientSells = currencyDto.getRateClientSells();
                    if (transaction.getType().equals(EventType.BUY)) {
                        transaction.setBought(adjustToCurrencyRate(transaction.getBought(), transactionCurrencyRateClientBuys));
                        transaction.setCommission(adjustToCurrencyRate(transaction.getCommission(), transactionCurrencyRateClientBuys));
                    }
                    if (transaction.getType().equals(EventType.SELL)) {
                        transaction.setSold(adjustToCurrencyRate(transaction.getSold(), transactionCurrencyRateClientSells));
                        transaction.setCommission(adjustToCurrencyRate(transaction.getCommission(), transactionCurrencyRateClientSells));
                    }
                })
                .collect(Collectors.toList());
        return transactionList;
    }

    private void adjustDividendListToCurrencyRate(@NotNull List<DividendResponseDto> dividends, @Currency String portfolioCurrency) {
        dividends.forEach(dividend -> {
            String dividendCurrency = tickerService.getTickerCurrency(dividend.getTicker());
            CurrencyRateResponse currencyDto = currencyRateService
                    .getRateForCurrencyPairOnDate(portfolioCurrency, dividendCurrency, dividend.getDate());
            BigDecimal dividendCurrencyRate = currencyDto.getRateClientSells();
            dividend.setAmount(adjustToCurrencyRate(dividend.getAmount(), dividendCurrencyRate));
        });
    }

    private BigDecimal adjustToCurrencyRate(@AmountOfMoney BigDecimal input, @AmountOfMoney BigDecimal currencyRate) {
        return input.divide(currencyRate, 2, RoundingMode.HALF_DOWN);
    }

    private Map<String, List<YahooSplitEventDto>> getSplitEventMapForTransactionList(@NotNull List<TransactionResponseDto> transactionList) {
        Map<String, List<YahooSplitEventDto>> splitEventMap = new HashMap<>();
        transactionList.forEach(transaction -> splitEventMap
                .put(transaction.getTicker(), tickerService.getSplitEventList(transaction.getTicker())));
        return splitEventMap;
    }

    private List<TransactionResponseDto> adjustTransactionListToSplitEvents(@NotNull List<TransactionResponseDto> transactionList, @NotNull Map<String, List<YahooSplitEventDto>> splitEventMap) {

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
