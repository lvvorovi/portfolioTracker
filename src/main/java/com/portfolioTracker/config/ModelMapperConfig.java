package com.portfolioTracker.config;

import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dto.event.EventDto;
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
        TypeMap<TransactionDtoResponse, EventDto> transactionToEventTypeMap = mapper
                .createTypeMap(TransactionDtoResponse.class, EventDto.class);
        transactionToEventTypeMap.addMapping(TransactionDtoResponse::getPrice, EventDto::setPriceAmount);

        TypeMap<DividendDtoResponse, EventDto> dividendToEventTypeMap = mapper
                .createTypeMap(DividendDtoResponse.class, EventDto.class);
        dividendToEventTypeMap.addMapping(DividendDtoResponse::getAmount, EventDto::setDividend);

        TypeMap<EventDto, DividendDtoResponse> eventToDividendTypeMap = mapper
                .createTypeMap(EventDto.class, DividendDtoResponse.class);
        eventToDividendTypeMap.addMapping(EventDto::getDividend, DividendDtoResponse::setAmount);

        TypeMap<EventDto, TransactionDtoResponse> eventToTransactionTypeMap = mapper
                .createTypeMap(EventDto.class, TransactionDtoResponse.class);
        eventToTransactionTypeMap.addMapping(EventDto::getPriceAmount, TransactionDtoResponse::setPrice);

        return mapper;
    }

}
