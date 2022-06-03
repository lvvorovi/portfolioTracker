package com.portfolioTracker;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.EventMapper;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.eventType.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventMapperTest {

    @Autowired
    private EventMapper eventMapper;

    private TransactionResponseDto getTransactionOfTypeBuy() {
        TransactionResponseDto transaction = new TransactionResponseDto();
        transaction.setId(10L);
        transaction.setTicker("KHC");
        transaction.setDate(LocalDate.now());
        transaction.setPrice(new BigDecimal(1001));
        transaction.setShares(new BigDecimal(500));
        transaction.setCommission(new BigDecimal(102));
        transaction.setType(EventType.BUY);
        transaction.setPortfolioId(1L);
        transaction.setBought(transaction.getPrice().multiply(transaction.getShares()));
        transaction.setSold(new BigDecimal(0));
        return transaction;
    }

    private Event getEventTypeBuy() {
        Event eventBuy = new Event();
        eventBuy.setId(1L);
        eventBuy.setTicker("Ticker");
        eventBuy.setDate(LocalDate.now());
        eventBuy.setPriceAmount(new BigDecimal(100));
        eventBuy.setShares(new BigDecimal(1000));
        eventBuy.setCommission(new BigDecimal(20));
        eventBuy.setType(EventType.BUY);
        eventBuy.setBought(new BigDecimal(1000));
        return eventBuy;
    }

    private Event getEventTypeSell() {
        Event eventSell = new Event();
        eventSell.setId(1L);
        eventSell.setTicker("Ticker");
        eventSell.setDate(LocalDate.now());
        eventSell.setPriceAmount(new BigDecimal(100));
        eventSell.setShares(new BigDecimal(1000));
        eventSell.setCommission(new BigDecimal(20));
        eventSell.setType(EventType.SELL);
        eventSell.setSold(new BigDecimal(1000));
        return eventSell;
    }

    private Event getEventTypeDividend() {
        Event eventDividend = new Event();
        eventDividend.setId(1L);
        eventDividend.setTicker("Ticker");
        eventDividend.setDate(LocalDate.now());
        eventDividend.setType(EventType.DIVIDEND);
        eventDividend.setDividend(new BigDecimal(1000));
        eventDividend.setExDate(LocalDate.now().minusDays(2));
        return eventDividend;
    }

    private DividendResponseDto getDividend() {
        DividendResponseDto dividend = new DividendResponseDto();
        dividend.setId(10L);
        dividend.setTicker("KHC");
        dividend.setDate(LocalDate.now());
        dividend.setType(EventType.DIVIDEND);
        dividend.setAmount(new BigDecimal(150));
        dividend.setExDate(LocalDate.now().minusDays(2));
        return dividend;
    }

    @Test
    void mapsTransactionToEvent() {
        TransactionResponseDto transaction = getTransactionOfTypeBuy();
        Event event = eventMapper.transactionToEvent(transaction);
        assertEquals(transaction.getId(), event.getId());
        assertEquals(transaction.getTicker(), event.getTicker());
        assertEquals(transaction.getDate(), event.getDate());
        assertEquals(transaction.getPrice(), event.getPriceAmount());
        assertEquals(transaction.getShares(), event.getShares());
        assertEquals(transaction.getCommission(), event.getCommission());
        assertEquals(transaction.getType(), event.getType());
        assertEquals(transaction.getBought(), event.getBought());
        assertEquals(transaction.getSold(), event.getSold());


    }

    @Test
    void mapsDividendToEvent() {
        DividendResponseDto dividend = getDividend();
        Event event = eventMapper.dividendToEvent(dividend);
        assertEquals(dividend.getId(), event.getId());
        assertEquals(dividend.getTicker(), event.getTicker());
        assertEquals(dividend.getDate(), event.getDate());
        assertEquals(dividend.getExDate(), event.getExDate());
        assertEquals(dividend.getAmount(), event.getDividend());
        assertEquals(dividend.getType(), event.getType());
    }

    @Test
    void mapEventToTransactionBuy() {
        Event eventTypeBuy = getEventTypeBuy();
        TransactionResponseDto transcation = eventMapper.eventToTransaction(eventTypeBuy);
        assertEquals(eventTypeBuy.getId(), transcation.getId());
        assertEquals(eventTypeBuy.getTicker(), transcation.getTicker());
        assertEquals(eventTypeBuy.getDate(), transcation.getDate());
        assertEquals(eventTypeBuy.getPriceAmount(), transcation.getPrice());
        assertEquals(eventTypeBuy.getShares(), transcation.getShares());
        assertEquals(eventTypeBuy.getCommission(), transcation.getCommission());
        assertEquals(eventTypeBuy.getType(), transcation.getType());
        assertEquals(eventTypeBuy.getBought(), transcation.getBought());
    }

    @Test
    void mapEventToTransactionSell() {
        Event eventTypeSell = getEventTypeSell();
        TransactionResponseDto transcation = eventMapper.eventToTransaction(eventTypeSell);
        assertEquals(eventTypeSell.getId(), transcation.getId());
        assertEquals(eventTypeSell.getTicker(), transcation.getTicker());
        assertEquals(eventTypeSell.getDate(), transcation.getDate());
        assertEquals(eventTypeSell.getPriceAmount(), transcation.getPrice());
        assertEquals(eventTypeSell.getShares(), transcation.getShares());
        assertEquals(eventTypeSell.getCommission(), transcation.getCommission());
        assertEquals(eventTypeSell.getType(), transcation.getType());
        assertEquals(eventTypeSell.getSold(), transcation.getSold());
    }

    @Test
    void mapEventToDividend() {
        Event eventDividend = getEventTypeDividend();
        DividendResponseDto dividend = eventMapper.eventToDividend(eventDividend);
        assertEquals(eventDividend.getId(), dividend.getId());
        assertEquals(eventDividend.getTicker(), dividend.getTicker());
        assertEquals(eventDividend.getDate(), dividend.getDate());
        assertEquals(eventDividend.getExDate(), dividend.getExDate());
        assertEquals(eventDividend.getType(), dividend.getType());
        assertEquals(eventDividend.getDividend(), dividend.getAmount());
    }
}
