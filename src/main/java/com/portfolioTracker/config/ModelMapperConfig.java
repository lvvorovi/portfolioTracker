package com.portfolioTracker.config;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dto.event.EventDto;
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
        TypeMap<TransactionResponseDto, EventDto> transactionToEventTypeMap = mapper
                .createTypeMap(TransactionResponseDto.class, EventDto.class);
        transactionToEventTypeMap.addMapping(TransactionResponseDto::getPrice, EventDto::setPriceAmount);

        TypeMap<DividendResponseDto, EventDto> dividendToEventTypeMap = mapper
                .createTypeMap(DividendResponseDto.class, EventDto.class);
        dividendToEventTypeMap.addMapping(DividendResponseDto::getAmount, EventDto::setDividend);

        TypeMap<EventDto, DividendResponseDto> eventToDividendTypeMap = mapper
                .createTypeMap(EventDto.class, DividendResponseDto.class);
        eventToDividendTypeMap.addMapping(EventDto::getDividend, DividendResponseDto::setAmount);

        TypeMap<EventDto, TransactionResponseDto> eventToTransactionTypeMap = mapper
                .createTypeMap(EventDto.class, TransactionResponseDto.class);
        eventToTransactionTypeMap.addMapping(EventDto::getPriceAmount, TransactionResponseDto::setPrice);

        return mapper;
    }

}
