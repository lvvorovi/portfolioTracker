package portfolioTracker.dto.currency.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import portfolioTracker.dto.currency.CurrencyRateDto;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CurrencyServiceYahooImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    @Override
    public Boolean isCurrencySupported(String currency) {
        return restTemplate.getForObject("http://localhost:9000/currency/supported/" +
                currency, Boolean.class);
    }

    @Override
    public CurrencyRateDto getRateForCurrencyPairOnDate(String currencyFrom, String currencyTo, LocalDate onDate) {
        return restTemplate.getForObject("http://localhost:9000/currency/rate/" +
                currencyFrom + "/" + currencyTo + "/" + onDate, CurrencyRateDto.class);
    }
}
