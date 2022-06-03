/*
package com.portfolioTracker.externalApi.alphaVantage.service;

import com.portfolioTracker.contract.ApiCurrencyService;
import com.portfolioTracker.contract.ApiTickerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@PropertySource("application.yml")
public class AlphaVantageApiService implements ApiTickerService, ApiCurrencyService {

    private final List<String> tickerList = new ArrayList<>();
    private final List<String> currencyList = new ArrayList<>();
    private Instant tickerListUpdatePeriod = Instant.now().minusSeconds(TimeUnit.MINUTES.toSeconds(10));
    private Instant lastCall = Instant.now().minusSeconds(13);

    @Value("${app.alphaVantage.apiKeyParam}")
    private String apiKeyParamAndKey;
    @Value("${app.alphaVantage.ticker.functionActiveTickerListURI}")
    private String functionActiveTickerListURI;
    @Value("${app.alphaVantage.currency.errorResponseMessage}")
    private String errorResponseMessage;
    @Value("${app.alphaVantage.currency.currencyFromParam}")
    private String currencyFromParam;
    @Value("${app.alphaVantage.currency.currencyToParam}")
    private String currencyToParam;
    @Value("${app.alphaVantage.currency.functionCurrencyPairURI}")
    private String functionCurrencyPairURI;

    @Override
    public Boolean isTickerSupported(String ticker) {
        return activeTickerListRequest().stream()
                .anyMatch(string -> string.equals(ticker));
    }

    @Override
    public String getTickerCurrency(String ticker) {
        return null;
    }

    @Override
    public BigDecimal getTickerCurrentPrice(String ticker) {
        return null;
    }

    @Override
    public Boolean isCurrencySupported(String currency) {

        if (!currencyList.isEmpty()) {
            if (currencyList.contains(currency)) return true;
        }

        HttpResponse<String> response;
        try {
            response = callApi(new URI(
                    functionCurrencyPairURI +
                            currencyFromParam + "USD" +
                            currencyToParam + currency +
                            apiKeyParamAndKey
            ));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        assert response != null;
        if (response.body().contains(currency.toUpperCase())) {
            currencyList.add(currency.toUpperCase());
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal getRateForCurrencyPairOnDate(String currencyFrom, String currencyTo, LocalDate onDate) {
        return null;
    }

*/
/*    private List<String> activeTickerListRequest() {

        if (!tickerList.isEmpty()
                &&
                (Instant.now().minusSeconds(60 * 60 * 24)
                        .compareTo(tickerListUpdatePeriod)) < 0
        ) {
            return tickerList;
        }

        HttpResponse<String> response;
        try {
            response = callApi(new URI(
                    functionActiveTickerListURI + apiKeyParamAndKey
            ));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        assert response != null;

        List<List<String>> listOfRows = getListOfListOfStrings(response);
        listOfRows.remove(0);
        listOfRows.forEach(row -> tickerList.add(row.get(0)));
        tickerListUpdatePeriod = Instant.now();
        return tickerList;

    }

    private List<List<String>> getListOfListOfStrings(HttpResponse<String> response) {
        List<List<String>> listOfRows = new ArrayList<>();
        try (Scanner scanner = new Scanner(response.body())) {
            while (scanner.hasNextLine()) {
                listOfRows.add(getCsvRows(scanner.nextLine()));
            }
        }
        return listOfRows;
    }

    private List<String> getCsvRows(String line) {
        List<String> listOfStrings = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                listOfStrings.add(rowScanner.next());
            }
        }
        return listOfStrings;
    }

    private HttpResponse<String> callApi(URI uri) {
        HttpRequest request;
        HttpResponse<String> response = null;
        try {
            if (lastCall.isAfter(Instant.now().minusSeconds(15))) {
                Thread.sleep(15000);
            }
            request = HttpRequest.newBuilder()
                    .uri(uri)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lastCall = Instant.now();
        }
        return response;

    }*//*

}
*/
