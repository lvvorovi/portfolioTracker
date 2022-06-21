package com.portfolioTracker.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dividend.service.DividendService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.portfolioTracker.model.dto.event.eventType.EventType.DIVIDEND;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DividendController.class)
class DividendControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DividendService dividendService;

    @Test
    void when_findById_then_return_Json_and_status_OK() throws Exception {
        DividendResponseDto responseDto = getDividendResponseDto("BRK");
        responseDto.setId(1L);
        given(dividendService.findById(1L)).willReturn(responseDto);

        mvc.perform(get("/dividend/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.ticker", is("BRK")))
                .andExpect(jsonPath("$.type", is("DIVIDEND")));

        verify(dividendService, times(1)).findById(any());
    }

    @Test
    void when_findAll_then_return_Json_array_and_status_OK() throws Exception {
        DividendResponseDto brk = getDividendResponseDto("BRK");
        DividendResponseDto ncc = getDividendResponseDto("NCC");

        List<DividendResponseDto> dividendList = Arrays.asList(brk, ncc);

        given(dividendService.findAll()).willReturn(dividendList);

        mvc.perform(get("/dividend")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ticker", is(dividendList.get(0).getTicker())))
                .andExpect(jsonPath("$[0].type", is("DIVIDEND")))
                .andExpect(jsonPath("$[1].ticker", is(dividendList.get(1).getTicker())))
                .andExpect(jsonPath("$[1].type", is("DIVIDEND")));
    }

    @Test
    void when_save_then_status_Created_and_savedDto_returned() throws Exception {
        DividendRequestDto dividendRequestDto = new DividendRequestDto();
        dividendRequestDto.setTicker("");
        dividendRequestDto.setAmount(new BigDecimal(100));
        dividendRequestDto.setType(DIVIDEND);

        DividendResponseDto dividendResponseDto = new DividendResponseDto();
        dividendResponseDto.setId(1L);
        dividendResponseDto.setAmount(dividendRequestDto.getAmount());
        dividendResponseDto.setType(DIVIDEND);
        dividendResponseDto.setTicker(dividendRequestDto.getTicker());

        given(dividendService.save(dividendRequestDto)).willReturn(dividendResponseDto);

        mvc.perform(post("/dividend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(dividendRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker", is(dividendRequestDto.getTicker())))
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

    private DividendResponseDto getDividendResponseDto(String ticker) {
        DividendResponseDto dividendResponseDto = new DividendResponseDto();
        dividendResponseDto.setTicker(ticker);
        dividendResponseDto.setType(DIVIDEND);
        return dividendResponseDto;
    }

    private byte[] objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);

    }
}