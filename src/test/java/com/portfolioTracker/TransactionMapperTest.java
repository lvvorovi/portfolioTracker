package com.portfolioTracker;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.model.dto.event.eventType.EventType;
import com.portfolioTracker.model.portfolio.PortfolioEntity;
import com.portfolioTracker.model.portfolio.repository.PortfolioRepository;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionMapperTest {

    @Autowired
    private ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> transactionMapper;
    @MockBean
    private PortfolioRepository portfolioRepository;


    @Test
    public void entityMappedToResponseDtoOfTypeSell() {
        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setId(1L);
        TransactionEntity entity = new TransactionEntity();
        entity.setId(1L);
        entity.setTicker("TICKER");
        entity.setDate(LocalDate.now());
        entity.setPrice(new BigDecimal(125));
        entity.setShares(new BigDecimal(100));
        entity.setCommission(new BigDecimal(10));
        entity.setType(EventType.SELL);
        entity.setPortfolio(portfolio);

        TransactionResponseDto response = transactionMapper.toDto(entity);
        assertEquals(response.getId(), entity.getId());
        assertEquals(response.getTicker(), entity.getTicker());
        assertEquals(response.getDate(), entity.getDate());
        assertEquals(response.getPrice(), entity.getPrice());
        assertEquals(response.getShares(), entity.getShares());
        assertEquals(response.getCommission(), entity.getCommission());
        assertEquals(response.getType(), entity.getType());
        assertEquals(response.getPortfolioId(), entity.getPortfolio().getId());
        assertEquals(response.getSold(), entity.getPrice().multiply(entity.getShares()));
        assertEquals(response.getBought(), new BigDecimal(0));
    }

    @Test
    public void entityMappedToResponseDtoOfTypeBuy() {
        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setId(1L);

        TransactionEntity entity = new TransactionEntity();
        entity.setId(1L);
        entity.setTicker("TICKER");
        entity.setDate(LocalDate.now());
        entity.setPrice(new BigDecimal(125));
        entity.setShares(new BigDecimal(100));
        entity.setCommission(new BigDecimal(10));
        entity.setType(EventType.BUY);
        entity.setPortfolio(portfolio);

        TransactionResponseDto response = transactionMapper.toDto(entity);
        assertEquals(response.getId(), entity.getId());
        assertEquals(response.getTicker(), entity.getTicker());
        assertEquals(response.getDate(), entity.getDate());
        assertEquals(response.getPrice(), entity.getPrice());
        assertEquals(response.getShares(), entity.getShares());
        assertEquals(response.getCommission(), entity.getCommission());
        assertEquals(response.getType(), entity.getType());
        assertEquals(response.getPortfolioId(), entity.getPortfolio().getId());
        assertEquals(response.getBought(), entity.getPrice().multiply(entity.getShares()));
        assertEquals(response.getSold(), new BigDecimal(0));
    }

}
