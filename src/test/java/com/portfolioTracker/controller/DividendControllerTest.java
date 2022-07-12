package com.portfolioTracker.controller;

import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.service.DividendService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.portfolioTracker.domain.dto.event.eventType.EventType.DIVIDEND;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({DividendController.class, JsonUtil.class})
class DividendControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonUtil jsonUtil;

    @MockBean
    private DividendService dividendService;

    @WithMockUser
    @Test
    void when_findById_then_return_Json_and_status_OK() throws Exception {
        DividendDtoResponse responseDto = getDividendResponseDto("BRK");
        responseDto.setId(1L);
        when(dividendService.findById(1L)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/dividends/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.ticker", is("BRK")))
                .andExpect(jsonPath("$.type", is("DIVIDEND")));

        verify(dividendService, times(1)).findById(any());
    }

    @WithMockUser
    @Test
    void when_findAll_then_return_Json_array_and_status_OK() throws Exception {
        DividendDtoResponse brk = getDividendResponseDto("BRK");
        DividendDtoResponse ncc = getDividendResponseDto("NCC");

        List<DividendDtoResponse> dividendList = Arrays.asList(brk, ncc);

        given(dividendService.findAll()).willReturn(dividendList);

        mvc.perform(get("/api/v1/dividends")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ticker", is(dividendList.get(0).getTicker())))
                .andExpect(jsonPath("$[0].type", is("DIVIDEND")))
                .andExpect(jsonPath("$[1].ticker", is(dividendList.get(1).getTicker())))
                .andExpect(jsonPath("$[1].type", is("DIVIDEND")));
    }

    @WithMockUser
    @Test
    void when_save_then_status_Created_and_savedDto_returned() throws Exception {
        DividendDtoUpdateRequest dividendDtoUpdateRequest = new DividendDtoUpdateRequest();
        dividendDtoUpdateRequest.setTicker("BRK");
        dividendDtoUpdateRequest.setAmount(new BigDecimal(100));
        dividendDtoUpdateRequest.setType(DIVIDEND);
        dividendDtoUpdateRequest.setExDate(LocalDate.now());
        dividendDtoUpdateRequest.setDate(LocalDate.now());
        dividendDtoUpdateRequest.setPortfolioId(1L);

        DividendDtoResponse dividendDtoResponse = new DividendDtoResponse();
        dividendDtoResponse.setId(1L);
        dividendDtoResponse.setAmount(dividendDtoUpdateRequest.getAmount());
        dividendDtoResponse.setType(DIVIDEND);
        dividendDtoResponse.setTicker(dividendDtoUpdateRequest.getTicker());

        given(dividendService.save(dividendDtoUpdateRequest)).willReturn(dividendDtoResponse);

        mvc.perform(post("/api/v1/dividends")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(dividendDtoUpdateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker", is(dividendDtoUpdateRequest.getTicker())))
                .andExpect(jsonPath("$.amount", is(100)))
                .andExpect(jsonPath("$.id", is(1)));

    }

    @Test
    void findById() throws Exception {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void update() {
    }

    private DividendDtoResponse getDividendResponseDto(String ticker) {
        DividendDtoResponse dividendDtoResponse = new DividendDtoResponse();
        dividendDtoResponse.setTicker(ticker);
        dividendDtoResponse.setType(DIVIDEND);
        return dividendDtoResponse;
    }

}