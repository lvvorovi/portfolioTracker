package com.portfolioTracker.config;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<TransactionResponseDto, Event> transactionToEventTypeMap = mapper
                .createTypeMap(TransactionResponseDto.class, Event.class);
        transactionToEventTypeMap.addMapping(TransactionResponseDto::getPrice, Event::setPriceAmount);

        TypeMap<DividendResponseDto, Event> dividendToEventTypeMap = mapper
                .createTypeMap(DividendResponseDto.class, Event.class);
        dividendToEventTypeMap.addMapping(DividendResponseDto::getAmount, Event::setDividend);

        TypeMap<Event, DividendResponseDto> eventToDividendTypeMap = mapper
                .createTypeMap(Event.class, DividendResponseDto.class);
        eventToDividendTypeMap.addMapping(Event::getDividend, DividendResponseDto::setAmount);

        TypeMap<Event, TransactionResponseDto> eventToTransactionTypeMap = mapper
                .createTypeMap(Event.class, TransactionResponseDto.class);
        eventToTransactionTypeMap.addMapping(Event::getPriceAmount, TransactionResponseDto::setPrice);

        return mapper;
    }

}
