/*
package com.portfolioTracker;

import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.summaryModule.event.EventDtoSplitAndDividend;
import com.portfolioTracker.summaryModule.event.eventType.EventType;
import com.portfolioTracker.summaryModule.event.mapper.EventDtoMapper;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventDtoSplitAndDividendMapperTest {

    @Autowired
    private EventDtoMapper eventDtoMapper;

    private TransactionDtoResponse getTransactionOfTypeBuy() {
        TransactionDtoResponse transaction = new TransactionDtoResponse();
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

    private EventDtoSplitAndDividend getEventTypeBuy() {
        EventDtoSplitAndDividend eventDtoBuy = new EventDtoSplitAndDividend();
        eventDtoBuy.setId(1L);
        eventDtoBuy.setTicker("Ticker");
        eventDtoBuy.setDate(LocalDate.now());
        eventDtoBuy.setPriceAmount(new BigDecimal(100));
        eventDtoBuy.setShares(new BigDecimal(1000));
        eventDtoBuy.setCommission(new BigDecimal(20));
        eventDtoBuy.setType(EventType.BUY);
        eventDtoBuy.setBought(new BigDecimal(1000));
        return eventDtoBuy;
    }

    private EventDtoSplitAndDividend getEventTypeSell() {
        EventDtoSplitAndDividend eventDtoSell = new EventDtoSplitAndDividend();
        eventDtoSell.setId(1L);
        eventDtoSell.setTicker("Ticker");
        eventDtoSell.setDate(LocalDate.now());
        eventDtoSell.setPriceAmount(new BigDecimal(100));
        eventDtoSell.setShares(new BigDecimal(1000));
        eventDtoSell.setCommission(new BigDecimal(20));
        eventDtoSell.setType(EventType.SELL);
        eventDtoSell.setSold(new BigDecimal(1000));
        return eventDtoSell;
    }

    private EventDtoSplitAndDividend getEventTypeDividend() {
        EventDtoSplitAndDividend eventDtoDividend = new EventDtoSplitAndDividend();
        eventDtoDividend.setId(1L);
        eventDtoDividend.setTicker("Ticker");
        eventDtoDividend.setDate(LocalDate.now());
        eventDtoDividend.setType(EventType.DIVIDEND);
        eventDtoDividend.setDividend(new BigDecimal(1000));
        eventDtoDividend.setExDate(LocalDate.now().minusDays(2));
        return eventDtoDividend;
    }

    private DividendDtoResponse getDividend() {
        DividendDtoResponse dividend = new DividendDtoResponse();
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
        TransactionDtoResponse transaction = getTransactionOfTypeBuy();
        EventDtoSplitAndDividend eventDto = eventDtoMapper.toEvent(transaction);
        assertEquals(transaction.getId(), eventDto.getId());
        assertEquals(transaction.getTicker(), eventDto.getTicker());
        assertEquals(transaction.getDate(), eventDto.getDate());
        assertEquals(transaction.getPrice(), eventDto.getPriceAmount());
        assertEquals(transaction.getShares(), eventDto.getShares());
        assertEquals(transaction.getCommission(), eventDto.getCommission());
        assertEquals(transaction.getType(), eventDto.getType());
        assertEquals(transaction.getBought(), eventDto.getBought());
        assertEquals(transaction.getSold(), eventDto.getSold());


    }

    @Test
    void mapsDividendToEvent() {
        DividendDtoResponse dividend = getDividend();
        EventDtoSplitAndDividend eventDto = eventDtoMapper.toEvent(dividend);
        assertEquals(dividend.getId(), eventDto.getId());
        assertEquals(dividend.getTicker(), eventDto.getTicker());
        assertEquals(dividend.getDate(), eventDto.getDate());
        assertEquals(dividend.getExDate(), eventDto.getExDate());
        assertEquals(dividend.getAmount(), eventDto.getDividend());
        assertEquals(dividend.getType(), eventDto.getType());
    }

    @Test
    void mapEventToTransactionBuy() {
        EventDtoSplitAndDividend eventDtoTypeBuy = getEventTypeBuy();
        TransactionDtoResponse transcation = eventDtoMapper.toTransaction(eventDtoTypeBuy);
        assertEquals(eventDtoTypeBuy.getId(), transcation.getId());
        assertEquals(eventDtoTypeBuy.getTicker(), transcation.getTicker());
        assertEquals(eventDtoTypeBuy.getDate(), transcation.getDate());
        assertEquals(eventDtoTypeBuy.getPriceAmount(), transcation.getPrice());
        assertEquals(eventDtoTypeBuy.getShares(), transcation.getShares());
        assertEquals(eventDtoTypeBuy.getCommission(), transcation.getCommission());
        assertEquals(eventDtoTypeBuy.getType(), transcation.getType());
        assertEquals(eventDtoTypeBuy.getBought(), transcation.getBought());
    }

    @Test
    void mapEventToTransactionSell() {
        EventDtoSplitAndDividend eventDtoTypeSell = getEventTypeSell();
        TransactionDtoResponse transcation = eventDtoMapper.toTransaction(eventDtoTypeSell);
        assertEquals(eventDtoTypeSell.getId(), transcation.getId());
        assertEquals(eventDtoTypeSell.getTicker(), transcation.getTicker());
        assertEquals(eventDtoTypeSell.getDate(), transcation.getDate());
        assertEquals(eventDtoTypeSell.getPriceAmount(), transcation.getPrice());
        assertEquals(eventDtoTypeSell.getShares(), transcation.getShares());
        assertEquals(eventDtoTypeSell.getCommission(), transcation.getCommission());
        assertEquals(eventDtoTypeSell.getType(), transcation.getType());
        assertEquals(eventDtoTypeSell.getSold(), transcation.getSold());
    }

    @Test
    void mapEventToDividend() {
        EventDtoSplitAndDividend eventDtoDividend = getEventTypeDividend();
        DividendDtoResponse dividend = eventDtoMapper.toDividend(eventDtoDividend);
        assertEquals(eventDtoDividend.getId(), dividend.getId());
        assertEquals(eventDtoDividend.getTicker(), dividend.getTicker());
        assertEquals(eventDtoDividend.getDate(), dividend.getDate());
        assertEquals(eventDtoDividend.getExDate(), dividend.getExDate());
        assertEquals(eventDtoDividend.getType(), dividend.getType());
        assertEquals(eventDtoDividend.getDividend(), dividend.getAmount());
    }
}
*/
