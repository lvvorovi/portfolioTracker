package com.portfolioTracker.contract;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface EventMapperContract {

    Event transactionToEvent(@NotNull TransactionResponseDto transaction);

    Event dividendToEvent(@NotNull DividendResponseDto dividend);

    DividendResponseDto eventToDividend(@NotNull Event event);

    TransactionResponseDto eventToTransaction(@NotNull Event event);

}
