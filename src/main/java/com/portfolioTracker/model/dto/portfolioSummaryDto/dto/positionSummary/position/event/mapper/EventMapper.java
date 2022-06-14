package com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.mapper;

import com.portfolioTracker.contract.EventMapperContract;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class EventMapper implements EventMapperContract {

    private final ModelMapper mapper;

    public EventMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Event transactionToEvent(@NotNull TransactionResponseDto transaction) {
        return mapper.map(transaction, Event.class);
    }

    @Override
    public Event dividendToEvent(@NotNull DividendResponseDto dividend) {
        return mapper.map(dividend, Event.class);
    }

    @Override
    public DividendResponseDto eventToDividend(@NotNull Event event) {
        return mapper.map(event, DividendResponseDto.class);
    }

    @Override
    public TransactionResponseDto eventToTransaction(@NotNull Event event) {
        return mapper.map(event, TransactionResponseDto.class);
    }
}
