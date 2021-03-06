package portfolioTracker.core.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import portfolioTracker.dto.currency.service.CurrencyService;
import portfolioTracker.dto.ticker.service.TickerService;
import portfolioTracker.portfolio.service.PortfolioService;
import portfolioTracker.transaction.service.TransactionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class ScheduledTaskConfig {

    private final TransactionService transactionService;
    private final PortfolioService portfolioService;
    private final TickerService tickerService;
    private final CurrencyService currencyService;

    @Async
    @Scheduled(fixedRateString = "PT1M")
    public void loadTickersToContext() {
        List<String> tickerListFromDB = transactionService.findAllUniqueTickers();

        Set<String> transactionCurrencyList = tickerListFromDB.parallelStream()
                .peek(ticker -> log.info("TickerService calling for currency for " + ticker))
                .map(tickerService::getTickerCurrency)
                .collect(Collectors.toSet());

        Set<String> portfolioCurrencyList = Set.copyOf(portfolioService.findAllPortfolioCurrencies());
        portfolioCurrencyList.parallelStream()
                .forEach(portfolioCurrency -> transactionCurrencyList.parallelStream()
                        .forEach(transactionCurrency -> {
                            log.info("CurrencyService calling for pair rate: " +
                                    portfolioCurrency + "-" + transactionCurrency);
                            currencyService.getRateForCurrencyPairOnDate(
                                    portfolioCurrency,
                                    transactionCurrency,
                                    LocalDate.now());
                        }));
    }

}
