package com.portfolioTracker.summaryModule.event.mapper;

import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventDtoMapperImpl implements EventDtoMapper {

    private final ModelMapper mapper;

    @Override
    public EventDto toEvent(TransactionDtoResponse transaction) {
        return mapper.map(transaction, EventDto.class);
    }

    @Override
    public EventDto toEvent(DividendDtoResponse dividend) {
        return mapper.map(dividend, EventDto.class);
    }

    @Override
    public DividendDtoResponse toDividend(EventDto event) {
        return mapper.map(event, DividendDtoResponse.class);
    }

    @Override
    public TransactionDtoResponse toTransaction(EventDto event) {
        return mapper.map(event, TransactionDtoResponse.class);
    }

}
