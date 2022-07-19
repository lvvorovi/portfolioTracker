package portfolioTracker.dividend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.service.DividendService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static portfolioTracker.util.DividendTestUtil.*;
import static portfolioTracker.util.JsonUtil.*;

@WebMvcTest({DividendController.class})
class DividendControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private DividendService service;
    @MockBean
    private LinkUtil linkUtil;

    @Test
    void findById_whenNotAuth_thenStatus401() throws Exception {
        mvc.perform(get(findByIdUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void findById_whenId_thenReturnJson_andStatus200() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse responseDto = newDividendResponseDto(entity);
        when(service.findById(id)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(get(findByIdUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseDtoBody = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToDividendDtoResponse(responseDtoBody));
        verify(service, times(1)).findById(id);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void findAll_whenNotAuth_thenStatus401() throws Exception {
        mvc.perform(get(findAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser(username = username)
    @Test
    void findAll_whenIsAuth_thenReturnJsonArray_andStatus200() throws Exception {
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();
        when(service.findAllByUsername(username)).thenReturn(responseDtoList);
        responseDtoList.forEach(dto -> doNothing().when(linkUtil).addLinks(dto));

        MvcResult result = mvc.perform(get(findAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDtoList, jsonToDividendDtoResponseList(resultContent));
        verify(service, times(1)).findAllByUsername(username);
        responseDtoList.forEach(dto -> verify(linkUtil, times(1)).addLinks(dto));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void findAll_whenIsParam_thenReturnJsonArray_andStatus200() throws Exception {
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();
        when(service.findAllByPortfolioId(portfolioId)).thenReturn(responseDtoList);
        responseDtoList.forEach(dto -> doNothing().when(linkUtil).addLinks(dto));

        MvcResult result = mvc.perform(get(findAllUri)
                        .with(csrf())
                        .param("portfolioId", portfolioId))
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDtoList, jsonToDividendDtoResponseList(resultContent));
        verify(service, times(1)).findAllByPortfolioId(portfolioId);
        responseDtoList.forEach(dto -> verify(linkUtil, times(1)).addLinks(dto));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void save_whenNotAuth_thenStatus401() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(entity);

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void save_whenDto_thenReturnJson_andStatus201() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest requestDto = newDividendDtoCreateRequest(entity);
        DividendDtoResponse responseDto = newDividendResponseDto(entity);
        when(service.save(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToDividendDtoResponse(resultContent));
        verify(service, times(1)).save(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void save_whenIllegalDto_thenValidates_andReturns400() throws Exception {
        DividendDtoCreateRequest illegalRequestDto = new DividendDtoCreateRequest();

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .content(objectToJson(illegalRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", containsString("null")));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void saveAll_whenNotAuth_thenStatus401() throws Exception {
        List<DividendDtoCreateRequest> requestDtoList = newDividendDtoCreateList();

        mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDtoList)))
                .andExpect(status().is(401));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void saveAll_whenDto_thenReturnJson_andStatus201() throws Exception {
        List<DividendDtoCreateRequest> requestDtoList = newDividendDtoCreateList();
        List<DividendDtoResponse> responseDtoList = newDividendDtoResponseList();
        when(service.saveAll(requestDtoList)).thenReturn(responseDtoList);
        responseDtoList.forEach(dto -> doNothing().when(linkUtil).addLinks(dto));

        MvcResult result = mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDtoList)))
                .andExpect(status().is(201))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDtoList, jsonToDividendDtoResponseList(resultContent));
        verify(service, times(1)).saveAll(requestDtoList);
        responseDtoList.forEach(dto -> verify(linkUtil, times(1)).addLinks(dto));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void saveAll_whenIllegalDto_thenValidates_andReturns400() throws Exception {
        List<DividendDtoCreateRequest> illegalRequestDtoList = List.of(
                new DividendDtoCreateRequest(), new DividendDtoCreateRequest(), new DividendDtoCreateRequest());

        mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .content(objectToJson(illegalRequestDtoList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", containsString("null")));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void deleteById_whenNotAuth_thenStatus401() throws Exception {
        mvc.perform(delete(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void deleteById_whenId_thenDelegateToService_andStatus204() throws Exception {
        doNothing().when(service).deleteById(id);

        mvc.perform(delete(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(204))
                .andExpect(content().string(emptyOrNullString()));

        verify(service, times(1)).deleteById(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(linkUtil);
    }

    @Test
    void update_whenNotAuth_thenStatus401() throws Exception {
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(newDividendEntity());

        mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void update_whenDto_thenDelegateToService_andStatus200() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest requestDto = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse responseDto = newDividendResponseDto(entity);
        when(service.update(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToDividendDtoResponse(resultContent));
        verify(service, times(1)).update(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void update_whenIllegalDto_thenValidates_andReturns400() throws Exception {
        DividendDtoUpdateRequest illegalRequestDto = new DividendDtoUpdateRequest();

        mvc.perform(post(updateUri)
                        .with(csrf())
                        .content(objectToJson(illegalRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", containsString("null")));

        verifyNoInteractions(service, linkUtil);
    }

}