package com.portfolioTracker.yahooModule;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.portfolioTracker.yahooModule.dto.YahooPriceDto;
import com.portfolioTracker.yahooModule.dto.YahooResponseDto;
import com.portfolioTracker.yahooModule.dto.YahooSplitEventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
@Validated
class YahooDtoDeserializer extends StdDeserializer<YahooResponseDto> {

    public YahooDtoDeserializer() {
        this(null);
    }

    public YahooDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public @Valid YahooResponseDto deserialize(JsonParser parser, DeserializationContext context) throws JsonProcessingException, NullPointerException {

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

        List<YahooPriceDto> yahooPriceDtoList = objectMapper.readValue(priceListNode.toString(), new TypeReference<>() {
        });
        List<YahooSplitEventDto> yahooSplitEventDtoList = objectMapper.readValue(eventListNode.toString(), new TypeReference<>() {
        });
        //TODO map eventListNode to DividendEventDtoList

        yahooDto.setYahooPriceDtoList(yahooPriceDtoList);
        yahooDto.setYahooSplitEventDtoList(yahooSplitEventDtoList);

        return yahooDto;

    }
}
