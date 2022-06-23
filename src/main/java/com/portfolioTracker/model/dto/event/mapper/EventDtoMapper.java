package com.portfolioTracker.model.dto.event.mapper;

import com.portfolioTracker.core.contract.EventMapperContract;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.event.EventDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@Validated
public class EventDtoMapper implements EventMapperContract {

    private final ModelMapper mapper;

    public EventDtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public EventDto transactionToEvent(@NotNull TransactionResponseDto transaction) {
        return mapper.map(transaction, EventDto.class);
    }

    @Override
    public EventDto dividendToEvent(@NotNull DividendResponseDto dividend) {
        return mapper.map(dividend, EventDto.class);
    }

    @Override
    public DividendResponseDto eventToDividend(@NotNull EventDto event) {
        return mapper.map(event, DividendResponseDto.class);
    }

    @Override
    public TransactionResponseDto eventToTransaction(@NotNull EventDto event) {
        return mapper.map(event, TransactionResponseDto.class);
    }
}
