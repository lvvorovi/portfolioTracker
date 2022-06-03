package com.portfolioTracker.externalApi.yahoo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.portfolioTracker.externalApi.yahoo.dto.Price;
import com.portfolioTracker.externalApi.yahoo.dto.YahooEvent;
import com.portfolioTracker.externalApi.yahoo.dto.YahooResponseDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class YahooDtoDeserializer extends StdDeserializer<YahooResponseDto> {

    public YahooDtoDeserializer() {
        this(null);
    }

    public YahooDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public YahooResponseDto deserialize(JsonParser parser, DeserializationContext context) throws JsonProcessingException {

        YahooResponseDto yahooDto = new YahooResponseDto();
        ObjectCodec codec = parser.getCodec();
        JsonNode node;
        try {
            node = codec.readTree(parser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode contextNode = node.get("context");
        JsonNode dispatcherNode = contextNode.get("dispatcher");
        JsonNode storesNode = dispatcherNode.get("stores");
        JsonNode quoteSummaryStoreNode = storesNode.get("QuoteSummaryStore");
        JsonNode tickerNode = quoteSummaryStoreNode.get("symbol");
        JsonNode priceNode = quoteSummaryStoreNode.get("price");
        JsonNode currencyNode = priceNode.get("currency");
        JsonNode regularMarketPriceNode = priceNode.get("regularMarketPrice");
        JsonNode currentMarketPriceNode = regularMarketPriceNode.get("raw");
        JsonNode historicalPriceStore = storesNode.get("HistoricalPriceStore");
        JsonNode priceListNode = historicalPriceStore.get("prices");
        JsonNode eventListNode = historicalPriceStore.get("eventsData");

        yahooDto.setTicker(tickerNode.asText());
        yahooDto.setCurrency(currencyNode.asText());
        yahooDto.setCurrentMarketPrice(BigDecimal.valueOf(Double.parseDouble(currentMarketPriceNode.asText())));

        ObjectMapper objectMapper = new ObjectMapper();

        List<Price> priceList = objectMapper.readValue(priceListNode.toString(), new TypeReference<List<Price>>() {
        });
        List<YahooEvent> yahooEventList = objectMapper.readValue(eventListNode.toString(), new TypeReference<>() {
        });


        yahooDto.setPriceList(priceList);
        yahooDto.setEventDataList(yahooEventList);

        return yahooDto;

    }
}
