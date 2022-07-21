package portfolioTracker.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.service.PortfolioService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static portfolioTracker.core.ExceptionErrors.NOT_BLANK_ERROR_MESSAGE;
import static portfolioTracker.util.JsonUtil.*;
import static portfolioTracker.util.PortfolioTestUtil.*;

@WebMvcTest(PortfolioController.class)
class PortfolioControllerTest {

    @MockBean
    PortfolioService service;
    @MockBean
    LinkUtil linkUtil;
    @Autowired
    MockMvc mvc;

    @Test
    void save_whenNotAuth_andDto_thenStatus401() throws Exception {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(entity);

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void save_whenIsAuth_andDto_thenReturnJson_thenStatus201() throws Exception {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoCreateRequest requestDto = newPortfolioDtoCreateRequest(entity);
        PortfolioDtoResponse responseDto = newPortfolioDtoResponse(entity);
        when(service.save(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(201))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToPortfolioDtoResponse(resultContent));
        verify(service, times(1)).save(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void save_whenIsAuth_andIllegalDto_thenErrorMessage_andStatus400() throws Exception {
        PortfolioDtoCreateRequest requestDto = new PortfolioDtoCreateRequest();

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage", containsString(NOT_BLANK_ERROR_MESSAGE)));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void findById_whenNoAuth_thenStatus401() throws Exception {
        mvc.perform(get(findByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(blankOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void findById_whenIsAuth_noParam_andFound_thenJson_status200() throws Exception {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse responseDto = newPortfolioDtoResponse(entity);
        responseDto.setDividendList(null);
        responseDto.setTransactionList(null);
        when(service.findById(id)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(get(findByIdUri)
                        .with(csrf()))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        PortfolioDtoResponse resultDto = jsonToPortfolioDtoResponse(resultContent);
        assertEquals(responseDto, resultDto);
        assertNull(resultDto.getDividendList());
        assertNull(resultDto.getTransactionList());
        verify(service, times(1)).findById(id);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void findById_whenIsAuth_andIsParam_thenReturnJsonWithEvents_andStatus200() throws Exception {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoResponse responseDto = newPortfolioDtoResponse(entity);
        when(service.findByIdIncludeEvents(id)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(get(findByIdUri)
                        .with(csrf())
                        .param("includeEvents", "true"))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        PortfolioDtoResponse resultDto = jsonToPortfolioDtoResponse(resultContent);
        assertEquals(responseDto, resultDto);
        assertNotNull(resultDto.getTransactionList());
        assertNotNull(resultDto.getDividendList());
        verify(service, times(1)).findByIdIncludeEvents(id);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void findAllByUsername_whenNotAuth_thenStatus401() throws Exception {
        mvc.perform(get(findAllByUsernameUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(blankOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser(username = username)
    void findAllByUsername_whenIsAuth_andNoParam_thenReturnJson_andStatus200() throws Exception {
        List<PortfolioDtoResponse> responsesDtoList = newPortfolioDtoResponseDtoList(newPortfolioEntityList());
        responsesDtoList.forEach(dto -> dto.setTransactionList(null));
        responsesDtoList.forEach(dto -> dto.setDividendList(null));
        when(service.findAllByUsername(username)).thenReturn(responsesDtoList);
        responsesDtoList.forEach(dto -> doNothing().when(linkUtil).addLinks(dto));

        MvcResult result = mvc.perform(get(findAllByUsernameUri)
                        .with(csrf()))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        List<PortfolioDtoResponse> resultDtoList = jsonToPortfolioDtoResponseList(resultContent);
        assertEquals(resultDtoList, resultDtoList);
        resultDtoList.forEach(dto -> assertNull(dto.getDividendList()));
        resultDtoList.forEach(dto -> assertNull(dto.getTransactionList()));
        verify(service, times(1)).findAllByUsername(username);
        responsesDtoList.forEach(dto -> verify(linkUtil, times(1)).addLinks(dto));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser(username = username)
    void findAllByUsername_whenIsAuth_andIsParam_thenReturnJsonWithEvents_andStatus200() throws Exception {
        List<PortfolioDtoResponse> responsesDtoList = newPortfolioDtoResponseDtoList(newPortfolioEntityList());
        when(service.findAllByUsernameIncludeEvents(username)).thenReturn(responsesDtoList);
        responsesDtoList.forEach(dto -> doNothing().when(linkUtil).addLinks(dto));

        MvcResult result = mvc.perform(get(findAllByUsernameUri)
                        .with(csrf())
                        .param("includeEvents", "true"))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        List<PortfolioDtoResponse> resultDtoList = jsonToPortfolioDtoResponseList(resultContent);
        assertEquals(responsesDtoList, resultDtoList);
        resultDtoList.forEach(dto -> assertNotNull(dto.getDividendList()));
        resultDtoList.forEach(dto -> assertNotNull(dto.getTransactionList()));
        verify(service, times(1)).findAllByUsernameIncludeEvents(username);
        responsesDtoList.forEach(dto -> verify(linkUtil, times(1)).addLinks(dto));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void update_whenNoAuth_thenStatus401() throws Exception {
        mvc.perform(put(updateUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(blankOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void update_whenIsAuth_andDto_thenReturnDto_status200() throws Exception {
        PortfolioEntity entity = newPortfolioEntity();
        PortfolioDtoUpdateRequest requestDto = newPortfolioDtoUpdateRequest(entity);
        PortfolioDtoResponse responseDto = newPortfolioDtoResponse(entity);
        responseDto.setDividendList(null);
        responseDto.setTransactionList(null);
        when(service.update(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        PortfolioDtoResponse resultDto = jsonToPortfolioDtoResponse(resultContent);
        assertEquals(responseDto, resultDto);
        assertNull(resultDto.getTransactionList());
        assertNull(resultDto.getDividendList());
        verify(service, times(1)).update(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
    }

    @Test
    @WithMockUser
    void update_whenIsAuth_andIllegalDto_thenReturnErrorMessage_status400() throws Exception{
        mvc.perform(put(updateUri)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(new PortfolioDtoUpdateRequest())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage", containsString(NOT_BLANK_ERROR_MESSAGE)));
    }

    @Test
    void deleteById_whenNoAuth_thenStatus401() throws Exception {
        mvc.perform(delete(deleteByIdUri)
                .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(blankOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void deleteById_whenIsAuth_thenDelegateToService_status204() throws Exception {
        doNothing().when(service).deleteById(id);

        mvc.perform(delete(deleteByIdUri)
                .with(csrf()))
                .andExpect(status().is(204))
                .andExpect(content().string(blankOrNullString()));

        verify(service, times(1)).deleteById(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(linkUtil);
    }

}