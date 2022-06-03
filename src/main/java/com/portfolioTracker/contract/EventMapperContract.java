package com.portfolioTracker.contract;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.Event;

import javax.validation.constraints.NotNull;

public interface EventMapperContract {

    Event transactionToEvent(@NotNull TransactionResponseDto transaction);

    Event dividendToEvent(@NotNull DividendResponseDto dividend);

    DividendResponseDto eventToDividend(@NotNull Event event);

    TransactionResponseDto eventToTransaction(@NotNull Event event);

}
