package portfolioTracker.dto.ticker.service;

import portfolioTracker.dto.ticker.SplitEventDto;
import portfolioTracker.dto.ticker.exception.TickerServiceNullResponseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TickerServiceYahooImpl implements TickerService {

    private final RestTemplate restTemplate;

    @Override
    public Boolean isTickerSupported(String ticker) {
        Boolean result = null;
        try {
            result = restTemplate.getForObject("http://localhost:9000/tickers/supported/" +
                    ticker, Boolean.class);
        } catch (RestClientException ex) {
            log.warn(this.getClass() + ".isTickerSupported() caught exception while attempting connection to yahooMS");
        }
        validateResult(result);
        return result;
    }

    @Override
    public BigDecimal getTickerCurrentPrice(String ticker) {
        BigDecimal result = null;
        try {
            result = restTemplate.getForObject("http://localhost:9000/tickers/currentPrice/" +
                    ticker, BigDecimal.class);
        } catch (RestClientException ex) {
            log.warn(this.getClass() + ".isTickerSupported() caught exception while attempting connection to yahooMS");
        }
        validateResult(result);
        return result;
    }

    @Override
    public String getTickerCurrency(String ticker) {
        String result = null;
        try {
            result = restTemplate.getForObject("http://localhost:9000/tickers/currency/" +
                    ticker, String.class);
        } catch (RestClientException ex) {
            log.warn(this.getClass() + ".isTickerSupported() caught exception while attempting connection to yahooMS");
        }
        validateResult(result);
        return result;
    }

    @Override
    public List<SplitEventDto> getSplitEventList(String ticker) {
        SplitEventDto[] result = null;
        try {
            result = restTemplate.getForObject("http://localhost:9000/tickers/splits/" +
                    ticker, SplitEventDto[].class);
        } catch (RestClientException ex) {
            log.warn(this.getClass() + ".isTickerSupported() caught exception while attempting connection to yahooMS");
        }
        validateResult(result);
        return List.of(result);
    }

    private void validateResult(Object result) {
        if (result == null) {
            throw new TickerServiceNullResponseException(this.getClass() +
                    " returned null for method isTickerSupported()");
        }
    }
}
