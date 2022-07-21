package portfolioTracker.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.service.TransactionService;
import portfolioTracker.util.JsonUtil;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static portfolioTracker.util.JsonUtil.*;
import static portfolioTracker.util.TransactionTestUtil.*;

@WebMvcTest(value = {TransactionController.class, JsonUtil.class})
class TransactionControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    TransactionService service;
    @MockBean
    LinkUtil linkUtil;

    @Test
    void findById_whenNoAuth_thenStatus401() throws Exception {
        mvc.perform(get(findByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void findById_whenId_thenReturnDto_andStatus200() throws Exception {
        TransactionDtoResponse responseDto = newTransactionDtoResponse(newTransactionEntity());
        when(service.findById(id)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(get(findByIdUri)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToTransactionDtoResponse(resultContent));
        verify(service, times(1)).findById(id);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void findAll_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(get(findAllUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser(username = "user")
    void findAll_whenIsAuth_noParam_thenFindByUsername_andReturnList_status200() throws Exception {
        List<TransactionDtoResponse> responseList = newTransactionDtoResponseList(newTransactionEntityList());
        when(service.findAllByUsername(username)).thenReturn(responseList);
        responseList.forEach(index -> doNothing().when(linkUtil).addLinks(index));

        MvcResult result = mvc.perform(get(findAllUri)
                        .with(csrf()))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseList, jsonToTransactionDtoResponseList(resultContent));
        verify(service, times(1)).findAllByUsername(username);
        responseList.forEach(index -> verify(linkUtil, times(1)).addLinks(index));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void findAll_whenIsAuth_isParam_thenFindByPortfolioId_andReturnList_Status200() throws Exception {
        List<TransactionDtoResponse> responseList = newTransactionDtoResponseList(newTransactionEntityList());
        when(service.findAllByPortfolioId(portfolioId)).thenReturn(responseList);
        responseList.forEach(index -> doNothing().when(linkUtil).addLinks(index));

        MvcResult result = mvc.perform(get(findAllUri)
                        .with(csrf())
                        .param("portfolioId", portfolioId))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseList, jsonToTransactionDtoResponseList(resultContent));
        verify(service, times(1)).findAllByPortfolioId(portfolioId);
        responseList.forEach(index -> verify(linkUtil, times(1)).addLinks(index));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    void save_whenNoAuth_thenStatus401() throws Exception {
        TransactionDtoCreateRequest requestDto = newTransactionDtoCreateRequest(newTransactionEntity());

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
    void save_whenIsAuth_andDto_thenReturnDto_andStatus201() throws Exception {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoCreateRequest requestDto = newTransactionDtoCreateRequest(entity);
        TransactionDtoResponse responseDto = newTransactionDtoResponse(entity);
        when(service.save(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(201))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToTransactionDtoResponse(resultContent));
        verify(service, times(1)).save(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void save_whenIsAuth_andIllegalDto_thenErrorMessage_status400() throws Exception {
        TransactionDtoCreateRequest requestDto = new TransactionDtoCreateRequest();

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage", containsString("null")));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void saveAll_whenNoAuth_thenStatus401() throws Exception {
        List<TransactionDtoCreateRequest> requestList = List.of(new TransactionDtoCreateRequest());

        mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestList)))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void saveAll_whenIsAuth_andDtoList_thenReturnDtoList_andStatus201() throws Exception {
        List<TransactionEntity> entityList = newTransactionEntityList();
        List<TransactionDtoCreateRequest> requestDtoList = newTransactionDtoCreateRequestList(entityList);
        List<TransactionDtoResponse> responseDtoList = newTransactionDtoResponseList(entityList);
        when(service.saveAll(requestDtoList)).thenReturn(responseDtoList);
        responseDtoList.forEach(index -> doNothing().when(linkUtil).addLinks(index));

        MvcResult result = mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDtoList)))
                .andExpect(status().is(201))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDtoList, jsonToTransactionDtoResponseList(resultContent));
        verify(service, times(1)).saveAll(requestDtoList);
        responseDtoList.forEach(index -> verify(linkUtil, times(1)).addLinks(index));
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void saveAll_whenIsAUth_andIllegalDto_thenErrorMessage_status400() throws Exception {
        List<TransactionDtoCreateRequest> requestList = List.of(new TransactionDtoCreateRequest());

        mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestList)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void update_whenNoAuth_thenStatus401() throws Exception {
        TransactionDtoUpdateRequest requestDto = newTransactionDtoUpdateRequest(newTransactionEntity());

        mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void update_whenIsAuth_andDto_thenReturnDto_andStatus201() throws Exception {
        TransactionEntity entity = newTransactionEntity();
        TransactionDtoUpdateRequest requestDto = newTransactionDtoUpdateRequest(entity);
        TransactionDtoResponse responseDto = newTransactionDtoResponse(entity);
        when(service.update(requestDto)).thenReturn(responseDto);
        doNothing().when(linkUtil).addLinks(responseDto);

        MvcResult result = mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().is(200))
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        assertEquals(responseDto, jsonToTransactionDtoResponse(resultContent));
        verify(service, times(1)).update(requestDto);
        verify(linkUtil, times(1)).addLinks(responseDto);
        verifyNoMoreInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void update_whenIsAuth_andIllegalDto_thenErrorMessage_status400() throws Exception {
        mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(new DividendDtoUpdateRequest())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    void deleteById_whenNoAuth_thenStatus401() throws Exception {
        mvc.perform(put(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void deleteById_whenIsAuth_andId_thenDelegateToService_status204() throws Exception {
        doNothing().when(service).deleteById(id);

        mvc.perform(delete(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(204))
                .andExpect(content().string(emptyOrNullString()));

        verify(service, times(1)).deleteById(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(linkUtil);
    }

}