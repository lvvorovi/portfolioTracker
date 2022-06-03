package com.portfolioTracker.config;

import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.Event;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConfigurationProperties(prefix = "app")
@EnableScheduling
@EnableAsync
public class AppProperties {

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory requestFactory = new
                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        return new RestTemplate(requestFactory);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return taskScheduler;
    }

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
