package com.portfolioTracker.domain.dto.event;

import com.portfolioTracker.core.contract.*;
import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dto.event.eventType.EventType;
import com.portfolioTracker.domain.dto.event.mapper.EventDtoMapper;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventDtoService {

    private final EventDtoMapper eventMapper;
    private final ApiCurrencyService apiCurrencyRateService;
    private final ApiTickerService apiTickerService;

    public Map<String, List<EventDto>> getMapOfTickerAndEventLists(@NotNull PortfolioDtoResponse portfolio) {
        List<EventDto> eventList = new ArrayList<>();

        List<TransactionDtoResponse> transactionList = portfolio.getTransactionList();
        Map<String, List<SplitEventDto>> splitEventMap = getSplitEventMapForTransactionList(transactionList);
        transactionList = adjustTransactionListToSplits(transactionList, splitEventMap);
        transactionList = adjustTransactionListToCurrencyRate(transactionList, portfolio.getCurrency());

        List<DividendDtoResponse> dividendList = portfolio.getDividendList();
        adjustDividendListToCurrencyRate(dividendList, portfolio.getCurrency());

        eventList.addAll(transactionList.stream()
                .map(eventMapper::toEvent)
                .collect(Collectors.toList()));

        eventList.addAll(dividendList.stream()
                .map(eventMapper::toEvent)
                .collect(Collectors.toList()));

        Map<String, List<EventDto>> eventMap = sortEventListToMapByTickers(eventList);
        eventMap.forEach((ticker, listOfEvents) -> listOfEvents.sort(Comparator.comparing(EventDto::getDate)));
        return eventMap;
    }

    public TransactionDtoResponse eventToTransaction(@NotNull EventDto event) {
        return eventMapper.toTransaction(event);
    }

    public DividendDtoResponse eventToDividend(@NotNull EventDto event) {
        return eventMapper.toDividend(event);
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

    private List<TransactionDtoResponse> adjustTransactionListToCurrencyRate(
            @NotNull List<TransactionDtoResponse> transactionList,
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
            @NotNull List<DividendDtoResponse> dividendList,
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
            BigDecimal input,
            BigDecimal currencyRate) {
        return input.divide(currencyRate, 2, RoundingMode.HALF_DOWN);
    }

    private Map<String, List<SplitEventDto>> getSplitEventMapForTransactionList(
            @NotNull List<TransactionDtoResponse> transactionList) {
        Map<String, List<SplitEventDto>> splitEventMap = new HashMap<>();
        transactionList.forEach(transaction ->
                splitEventMap.put(
                        transaction.getTicker(),
                        apiTickerService.getSplitEventList(transaction.getTicker())));
        return splitEventMap;
    }

    private List<TransactionDtoResponse> adjustTransactionListToSplits(
            @NotNull List<TransactionDtoResponse> transactionList,
            @NotNull Map<String, List<SplitEventDto>> splitEventMap) {
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
