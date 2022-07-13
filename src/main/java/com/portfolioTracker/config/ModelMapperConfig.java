package com.portfolioTracker.config;

import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.summaryModule.event.EventDtoSplitAndDividend;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<TransactionDtoResponse, EventDtoSplitAndDividend> transactionToEventTypeMap = mapper
                .createTypeMap(TransactionDtoResponse.class, EventDtoSplitAndDividend.class);
        transactionToEventTypeMap.addMapping(TransactionDtoResponse::getPrice, EventDtoSplitAndDividend::setPriceAmount);

        TypeMap<DividendDtoResponse, EventDtoSplitAndDividend> dividendToEventTypeMap = mapper
                .createTypeMap(DividendDtoResponse.class, EventDtoSplitAndDividend.class);
        dividendToEventTypeMap.addMapping(DividendDtoResponse::getAmount, EventDtoSplitAndDividend::setDividend);

        TypeMap<EventDtoSplitAndDividend, DividendDtoResponse> eventToDividendTypeMap = mapper
                .createTypeMap(EventDtoSplitAndDividend.class, DividendDtoResponse.class);
        eventToDividendTypeMap.addMapping(EventDtoSplitAndDividend::getDividend, DividendDtoResponse::setAmount);

        TypeMap<EventDtoSplitAndDividend, TransactionDtoResponse> eventToTransactionTypeMap = mapper
                .createTypeMap(EventDtoSplitAndDividend.class, TransactionDtoResponse.class);
        eventToTransactionTypeMap.addMapping(EventDtoSplitAndDividend::getPriceAmount, TransactionDtoResponse::setPrice);

        return mapper;
    }

}
