package com.portfolioTracker.summaryModule.event.mapper;

import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;

public interface EventDtoMapper {

    EventDto toEvent(TransactionDtoResponse transaction);

    EventDto toEvent(DividendDtoResponse dividend);

    DividendDtoResponse toDividend(EventDto event);

    TransactionDtoResponse toTransaction(EventDto event);
}
