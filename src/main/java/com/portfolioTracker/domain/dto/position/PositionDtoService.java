package com.portfolioTracker.domain.dto.position;

import com.portfolioTracker.domain.currency.ApiCurrencyService;
import com.portfolioTracker.domain.ticker.ApiTickerService;
import com.portfolioTracker.domain.currency.CurrencyRateDto;
import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dto.event.EventDto;
import com.portfolioTracker.domain.dto.event.EventDtoService;
import com.portfolioTracker.domain.dto.event.eventType.EventType;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
@Validated
public class PositionDtoService {

    private final ApiTickerService apiTickerService;
    private final EventDtoService eventService;
    private final ApiCurrencyService apiCurrencyService;

    public PositionDtoService(ApiTickerService apiTickerService, EventDtoService eventService, ApiCurrencyService apiCurrencyService) {
        this.apiTickerService = apiTickerService;
        this.eventService = eventService;
        this.apiCurrencyService = apiCurrencyService;
    }

    public List<PositionDto> getPositionList(@NotNull PortfolioDtoResponse portfolio) {
        Map<String, List<EventDto>> eventListMap = eventService.getMapOfTickerAndEventLists(portfolio);
        return getPositionListFromEventListMap(eventListMap, portfolio.getCurrency());
    }

    private List<PositionDto> getPositionListFromEventListMap(
            @NotNull Map<String, List<EventDto>> eventsMap,
            @Currency String portfolioCurrency) {
        List<PositionDto> positionList = new ArrayList<>();
        eventsMap.forEach((ticker, eventList) -> {
            PositionDto position = new PositionDto();

            String tickerCurrency = apiTickerService.getTickerCurrency(ticker);
            CurrencyRateDto currencyRate = apiCurrencyService
                    .getRateForCurrencyPairOnDate(portfolioCurrency, tickerCurrency, LocalDate.now());
            BigDecimal currentExchangeRateClientSells = currencyRate.getRateClientSells();

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
                    getCapitalGainFromEventListAdjustedToCurrency(
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
        });
        positionList.sort(Comparator.comparing(position -> position.getEventList().get(0).getDate()));
        return positionList;
    }

    private BigDecimal getTotalSoldFromEventList(@NotNull List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(eventService::eventToTransaction)
                .map(TransactionDtoResponse::getSold)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalBoughtFromEventList(@NotNull List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.BUY))
                .map(eventService::eventToTransaction)
                .map(TransactionDtoResponse::getBought)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalSharesFromEventList(@NotNull List<EventDto> eventList) {
        BigDecimal sharesBough = eventList.stream()
                .filter(event -> event.getType().equals(EventType.BUY))
                .map(eventService::eventToTransaction)
                .map(TransactionDtoResponse::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        BigDecimal sharesSold = eventList.stream()
                .filter(event -> event.getType().equals(EventType.SELL))
                .map(eventService::eventToTransaction)
                .map(TransactionDtoResponse::getShares)
                .reduce(new BigDecimal(0), BigDecimal::add);

        return sharesBough.subtract(sharesSold);
    }

    private BigDecimal getTotalDividendAmountFromEventList(@NotNull List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToDividend)
                .map(DividendDtoResponse::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getTotalCommissionFromEventList(@NotNull List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> !event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToTransaction)
                .map(TransactionDtoResponse::getCommission)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal getCapitalGainFromEventListAdjustedToCurrency(
            @NotNull List<EventDto> eventList,
            @Currency String portfolioCurrency,
            BigDecimal currentExchangeRateClientSells) {

        List<TransactionDtoResponse> transactionList = getTransactionListFromEventList(eventList);

        String ticker = transactionList.get(0).getTicker();
        String transactionCurrency = apiTickerService.getTickerCurrency(ticker);
        BigDecimal currentPrice = apiTickerService.getTickerCurrentPrice(ticker);

        List<Share> shareListBought = getSharesBoughFromTransactionList(
                transactionList, portfolioCurrency, transactionCurrency);
        List<Share> shareListSold = getSharesSoldFromTransactionList(
                transactionList, portfolioCurrency, transactionCurrency);

        shareListBought.sort(Comparator.comparing(share -> share.date));
        shareListSold.sort(Comparator.comparing(share -> share.date));

        List<Share> liquidatedShares = getLiquidatedShares(shareListBought, shareListSold);

        List<Share> activeShares = new ArrayList<>(shareListBought);
        liquidatedShares.forEach(share -> activeShares.remove(0));

        BigDecimal capitalGainCurrencyAdjustedFromLiquidatedShares =
                getCapitalGainCurrencyAdjustedFromLiquidatedShares(liquidatedShares);

        BigDecimal capitalGainCurrencyAdjustedFromActiveShares = getCapitalGainCurrencyAdjustedFromActiveShares(
                activeShares, currentPrice, currentExchangeRateClientSells);

        return capitalGainCurrencyAdjustedFromLiquidatedShares
                .add(capitalGainCurrencyAdjustedFromActiveShares);
    }

    private BigDecimal getCapitalGainCurrencyAdjustedFromActiveShares(
            @NotNull List<Share> activeShares,
            BigDecimal currentPrice,
            BigDecimal currentExchangeRateClientSells) {
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

    private List<Share> getSharesSoldFromTransactionList(
            @NotNull List<TransactionDtoResponse> transactionList,
            @Currency String portfolioCurrency,
            @Currency String transactionCurrency) {
        return transactionList.stream()
                .map(transaction -> {
                    CurrencyRateDto currencyDto = apiCurrencyService
                            .getRateForCurrencyPairOnDate(portfolioCurrency, transactionCurrency, transaction.getDate());
                    return getSharesSoldFromTransaction(transaction, currencyDto);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Share> getSharesBoughFromTransactionList(
            @NotNull List<TransactionDtoResponse> transactionList,
            @Currency String portfolioCurrency,
            @Currency String transactionCurrency) {
        return transactionList.stream()
                .map(transaction -> {
                    CurrencyRateDto currencyDto = apiCurrencyService
                            .getRateForCurrencyPairOnDate(portfolioCurrency, transactionCurrency, transaction.getDate());
                    return getSharesBoughFromTransaction(transaction, currencyDto);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Share> getSharesSoldFromTransaction(
            @NotNull TransactionDtoResponse transaction,
            @Currency CurrencyRateDto currencyRate) {
        List<Share> shareListSold = new ArrayList<>();
        int transactionSize = transaction.getShares().intValue();
        for (int i = 0; i < transactionSize; i++) {
            if (transaction.getType().equals(EventType.SELL)) {
                Share share = new Share();
                share.date = transaction.getDate();
                share.priceSold = transaction.getPrice();
                share.rateSold = currencyRate.getRateClientSells();
                shareListSold.add(share);
            }
        }
        return shareListSold;
    }

    private List<Share> getSharesBoughFromTransaction(
            @NotNull TransactionDtoResponse transaction,
            @NotNull CurrencyRateDto currencyRate) {
        List<Share> shareListSold = new ArrayList<>();
        int transactionSize = transaction.getShares().intValue();
        for (int i = 0; i < transactionSize; i++) {
            if (transaction.getType().equals(EventType.BUY)) {
                Share share = new Share();
                share.date = transaction.getDate();
                share.priceBought = transaction.getPrice();
                share.rateBought = currencyRate.getRateClientBuys();
                shareListSold.add(share);
            }
        }
        return shareListSold;
    }

    private List<TransactionDtoResponse> getTransactionListFromEventList(@NotNull List<EventDto> eventList) {
        return eventList.stream()
                .filter(event -> !event.getType().equals(EventType.DIVIDEND))
                .map(eventService::eventToTransaction)
                .collect(Collectors.toList());
    }

    private BigDecimal getAsPercentOfTotalBough(BigDecimal input, BigDecimal totalBough) {
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
