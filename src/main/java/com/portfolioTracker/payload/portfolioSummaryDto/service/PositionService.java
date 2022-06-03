package com.portfolioTracker.payload.portfolioSummaryDto.service;

import com.portfolioTracker.contract.ApiTickerService;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.payload.currencyRateDto.dto.CurrencyRateResponseDto;
import com.portfolioTracker.payload.currencyRateDto.service.CurrencyRateService;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.Position;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import com.portfolioTracker.payload.portfolioSummaryDto.exception.PositionServiceException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final ApiTickerService apiTickerService;
    private final EventService eventService;
    private final CurrencyRateService currencyRateService;

    public PositionService(ApiTickerService apiTickerService, EventService eventService, CurrencyRateService currencyRateService) {
        this.apiTickerService = apiTickerService;
        this.eventService = eventService;
        this.currencyRateService = currencyRateService;
    }

    public List<Position> getPositions(@NotNull PortfolioResponseDto portfolio) {
        Map<String, List<Event>> mapOfEvents = eventService.getMapOfTickerAndEventLists(portfolio);
        return getPositionsFromMapOfEvents(mapOfEvents, portfolio.getCurrency());
    }

    private List<Position> getPositionsFromMapOfEvents(@NotNull Map<String, List<Event>> eventsMap, @NotNull String portfolioCurrency) {
        List<Position> positionList = new ArrayList<>();
        eventsMap.forEach((ticker, eventList) -> {
            Position position = new Position();

            String tickerCurrency = apiTickerService.getTickerCurrency(ticker);
            CurrencyRateResponseDto currencyDto = currencyRateService.getRateForPairOnDate(portfolioCurrency, tickerCurrency, LocalDate.now());
            BigDecimal currentExchangeRateClientSells = currencyDto.getRateClientSells();

            position.setName(ticker + " converted from " + tickerCurrency + " to " + portfolioCurrency);
            position.setEventList(eventList);
            position.setTotalBought(getTotalBoughtFromEventList(eventList));
            position.setTotalSold(getTotalSoldFromEventList(eventList));
            position.setNetOriginalCosts(position.getTotalBought().subtract(position.getTotalSold()));
            position.setTotalShares(getTotalSharesFromEventList(eventList));
            position.setCurrentSharePrice(apiTickerService.getTickerCurrentPrice(ticker));

            position.setCurrentValue(
                    position.getTotalShares().multiply(position.getCurrentSharePrice())
                            .divide(currentExchangeRateClientSells, 2, RoundingMode.HALF_DOWN));

            position.setDividend(getTotalDividendAmountFromEventList(eventList));

            position.setCommission(getTotalCommissionFromEventList(eventList));

            position.setCapitalGain(
                    getCapitalGainFromEventsAdjustedToCurrency(
                            eventList, portfolioCurrency, currentExchangeRateClientSells));

            position.setCurrencyGain(position.getCurrentValue()
                    .subtract(position.getNetOriginalCosts())
                    .subtract(position.getCapitalGain()));

            position.setTotalGain(position.getCapitalGain()
                    .add(position.getDividend())
                    .add(position.getCurrencyGain())
                    .subtract(position.getCommission()));

            position.setCurrencyReturn(getAsPercentOfTotalBough(position.getCurrencyGain(), position.getTotalBought()));
            position.setDividendReturn(getAsPercentOfTotalBough(position.getDividend(), position.getTotalBought()));
            position.setCapitalReturn(getAsPercentOfTotalBough(position.getCapitalGain(), position.getTotalBought()));
            position.setCommissionReturn(getAsPercentOfTotalBough(position.getCommission(), position.getTotalBought()));
            position.setTotalReturn(getAsPercentOfTotalBough(position.getTotalGain(), position.getTotalBought()));

            positionList.add(position);
            positionList.sort(Comparator.comparing(pos -> pos.getEventList().get(0).getDate()));
        });
        return positionList;
    }

    private BigDecimal getTotalSoldFromEventList(@NotNull List<Event> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(eventService::eventToTransaction)
                .map(TransactionResponseDto::getSold)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalBoughtFromEventList(@NotNull List<Event> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.BUY))
                .map(eventService::eventToTransaction)
                .map(TransactionResponseDto::getBought)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalSharesFromEventList(@NotNull List<Event> eventList) {
        BigDecimal sharesBough = eventList.stream()
                .filter(event -> event.getType().equals(EventType.BUY))
                .map(eventService::eventToTransaction)
                .map(TransactionResponseDto::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        BigDecimal sharesSold = eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(eventService::eventToTransaction)
                .map(TransactionResponseDto::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        return sharesBough.subtract(sharesSold);
    }

    private BigDecimal getTotalDividendAmountFromEventList(@NotNull List<Event> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToDividend)
                .map(DividendResponseDto::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalCommissionFromEventList(@NotNull List<Event> eventList) {
        return eventList.stream()
                .filter(event -> !event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToTransaction)
                .map(TransactionResponseDto::getCommission)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getCapitalGainFromEventsAdjustedToCurrency(
            @NotNull List<Event> eventList, String portfolioCurrency, @NotNull BigDecimal currentExchangeRateClientSells) {

        List<TransactionResponseDto> transactionList = getTransactionListFromEventList(eventList);

        String ticker = transactionList.get(0).getTicker();
        String transactionCurrency = apiTickerService.getTickerCurrency(ticker);
        BigDecimal currentPrice = apiTickerService.getTickerCurrentPrice(ticker);

        List<Share> shareListBought = getSharesBoughFromTransactionList(transactionList, portfolioCurrency, transactionCurrency);
        List<Share> shareListSold = getSharesSoldFromTransactionList(transactionList, portfolioCurrency, transactionCurrency);

        shareListBought.sort(Comparator.comparing(share -> share.date));
        shareListSold.sort(Comparator.comparing(share -> share.date));

        if (shareListSold.size() > shareListBought.size()) {
            throw new PositionServiceException("Shares Sold List is greater than Shares Bought List for eventList "
                    + eventList);
        }

        List<Share> liquidatedShares = getLiquidatedShares(shareListBought, shareListSold);

        List<Share> activeShares = new ArrayList<>(shareListBought);
        liquidatedShares.forEach(share -> activeShares.remove(0));

        BigDecimal capitalGainCurrencyAdjustedFromLiquidatedShares =
                getCapitalGainCurrencyAdjustedFromLiquidatedShares(liquidatedShares);

        BigDecimal capitalGainCurrencyAdjustedFromActiveShares =
                getCapitalGainCurrencyAdjustedFromActiveShares(activeShares, currentPrice, currentExchangeRateClientSells);

        return capitalGainCurrencyAdjustedFromLiquidatedShares
                .add(capitalGainCurrencyAdjustedFromActiveShares);
    }

    private BigDecimal getCapitalGainCurrencyAdjustedFromActiveShares(
            @NotNull List<Share> activeShares, @NotNull BigDecimal currentPrice, @NotNull BigDecimal currentExchangeRateClientSells) {
        return activeShares.stream()
                .map(share -> (currentPrice.subtract(share.priceBought))
                        .divide(currentExchangeRateClientSells, 2, RoundingMode.HALF_DOWN))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getCapitalGainCurrencyAdjustedFromLiquidatedShares(@NotNull List<Share> liquidatedShares) {
        return liquidatedShares.stream()
                .map(share -> (share.priceSold.subtract(share.priceBought))
                        .divide(share.rateSold, 2, RoundingMode.HALF_DOWN))
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private List<Share> getLiquidatedShares(@NotNull List<Share> shareListBought, @NotNull List<Share> shareListSold) {
        List<Share> liquidatedShareList = new ArrayList<>();
        for (int i = 0; i < shareListSold.size(); i++) {
            shareListBought.get(i).priceSold = shareListSold.get(i).priceSold;
            shareListBought.get(i).rateSold = shareListSold.get(i).rateSold;
            liquidatedShareList.add(shareListBought.get(i));
        }
        return liquidatedShareList;
    }

    private List<Share> getSharesSoldFromTransactionList(@NotNull List<TransactionResponseDto> transactionList, @NotNull String portfolioCurrency, @NotNull String transactionCurrency) {
        return transactionList.stream()
                .map(transaction -> {
                    CurrencyRateResponseDto currencyDto = currencyRateService
                            .getRateForPairOnDate(portfolioCurrency, transactionCurrency, transaction.getDate());
                    return getSharesSoldFromTransaction(transaction, currencyDto);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Share> getSharesBoughFromTransactionList(@NotNull List<TransactionResponseDto> transactionList, @NotNull String portfolioCurrency, @NotNull String transactionCurrency) {
        return transactionList.stream()
                .map(transaction -> {
                    CurrencyRateResponseDto currencyDto = currencyRateService
                            .getRateForPairOnDate(portfolioCurrency, transactionCurrency, transaction.getDate());
                    return getSharesBoughFromTransaction(transaction, currencyDto);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Share> getSharesSoldFromTransaction(@NotNull TransactionResponseDto transaction, @NotNull CurrencyRateResponseDto currencyDto) {
        List<Share> shareListSold = new ArrayList<>();
        int transactionSize = transaction.getShares().intValue();
        for (int i = 0; i < transactionSize; i++) {
            if (transaction.getType().equals(EventType.SELL)) {
                Share share = new Share();
                share.date = transaction.getDate();
                share.priceSold = transaction.getPrice();
                share.rateSold = currencyDto.getRateClientSells();
                shareListSold.add(share);
            }
        }
        return shareListSold;
    }

    private List<Share> getSharesBoughFromTransaction(@NotNull TransactionResponseDto transaction, @NotNull CurrencyRateResponseDto currencyDto) {
        List<Share> shareListSold = new ArrayList<>();
        int transactionSize = transaction.getShares().intValue();
        for (int i = 0; i < transactionSize; i++) {
            if (transaction.getType().equals(EventType.BUY)) {
                Share share = new Share();
                share.date = transaction.getDate();
                share.priceBought = transaction.getPrice();
                share.rateBought = currencyDto.getRateClientBuys();
                shareListSold.add(share);
            }
        }
        return shareListSold;
    }

    private List<TransactionResponseDto> getTransactionListFromEventList(@NotNull List<Event> eventList) {
        return eventList.stream()
                .filter(event -> !event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToTransaction)
                .collect(Collectors.toList());
    }

    private BigDecimal getAsPercentOfTotalBough(@NotNull BigDecimal input, @NotNull BigDecimal totalBough) {
        return input
                .divide(totalBough, 4, RoundingMode.HALF_DOWN);
    }
}

class Share {
    LocalDate date;
    BigDecimal priceBought;
    BigDecimal priceSold;
    BigDecimal rateBought;
    BigDecimal rateSold;
}
