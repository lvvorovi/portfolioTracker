package com.portfolioTracker.domain.dividend.service;

import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;

import java.util.List;

public interface DividendService {

    DividendDtoResponse save(DividendDtoCreateRequest requestDto);

    List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList);

    List<DividendDtoResponse> findAll();

    List<DividendDtoResponse> findAllByTickerList(List<String> tickerList);

    DividendDtoResponse findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    DividendDtoResponse update(DividendDtoUpdateRequest requestDto);

    Boolean existsById(Long id);

    List<DividendDtoResponse> findAllByTicker(String ticker);
}
