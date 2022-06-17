package com.portfolioTracker.contract;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.event.EventDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface EventMapperContract {

    EventDto transactionToEvent(@NotNull TransactionResponseDto transaction);

    EventDto dividendToEvent(@NotNull DividendResponseDto dividend);

    DividendResponseDto eventToDividend(@NotNull EventDto eventDto);

    TransactionResponseDto eventToTransaction(@NotNull EventDto eventDto);

}
