package portfolioTracker.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.core.ValidList;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.transaction.domain.TransactionEntity;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.service.TransactionService;
import portfolioTracker.util.JsonUtil;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static portfolioTracker.transaction.TransactionTestUtil.*;
import static portfolioTracker.util.JsonUtil.*;

@WebMvcTest(value = {TransactionController.class, JsonUtil.class})
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("logging-test")
class TransactionControllerTest {

    private final String id = "id";
    private final String username = "user";
    private final String portfolioId = "portfolioId";
    private final URI findByIdUri = linkTo(methodOn(TransactionController.class).findById(id)).toUri();
    private final URI findAllUri = linkTo(TransactionController.class).toUri();
    private final URI saveUri = linkTo(TransactionController.class).toUri();
    private final URI updateUri = linkTo(TransactionController.class).toUri();
    private final URI deleteByIdUri = linkTo(methodOn(TransactionController.class).deleteById(id)).toUri();
    private final URI saveAllUri = linkTo(methodOn(TransactionController.class).saveAll(new ValidList<>())).toUri();

    @Autowired
    MockMvc mvc;
    @MockBean
    TransactionService service;
    @MockBean
    LinkUtil linkUtil;

    @WithMockUser
    @Test
    void findById_whenId_thenReturnDto_andStatus200(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "get", findByIdUri);
    }

    @Test
    void findById_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(get(findByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
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
    void findAll_whenNoParam_thenFindByUsername_andReturnList_status200(CapturedOutput output) throws Exception {
        List<TransactionDtoResponse> responseList = newTransactionDtoResponseList();
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
        assertOutputContainsExpected(output, "get", findAllUri);
    }

    @Test
    @WithMockUser
    void findAll_whenIsParam_thenFindByPortfolioId_andReturnList_Status200(CapturedOutput output) throws Exception {
        List<TransactionDtoResponse> responseList = newTransactionDtoResponseList();
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
        assertOutputContainsExpected(output, "get", findAllUri);
    }

    @Test
    void save_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void save_whenAuthenticated_andDto_thenDelegateToService_andReturnSaved_andStatus201(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "post", saveUri);
    }

    @Test
    @WithMockUser
    void save_whenInvalidFieldValue_thenValidation_andErrorResponse_status400(CapturedOutput output) throws Exception {
        mvc.perform(post(saveUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(new DividendDtoCreateRequest())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
        assertOutputContainsExpected(output, "post", saveUri);
    }

    @Test
    @WithMockUser
    void saveAll_whenInvalidFieldValue_thenValidation_andErrorResponse_status400(CapturedOutput output) throws Exception {
        List<TransactionDtoCreateRequest> requestList = List.of(new TransactionDtoCreateRequest());

        mvc.perform(post(saveAllUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestList)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
        assertOutputContainsExpected(output, "post", saveAllUri);
    }

    @Test
    void saveAll_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void saveAll_whenAuthenticated_andDtoList_thenDelegatesToService_returnsResponseDtoList_andStatus201(CapturedOutput output) throws Exception {
        List<TransactionDtoCreateRequest> requestDtoList = newTransactionDtoCreateRequestList();
        List<TransactionDtoResponse> responseDtoList = newTransactionDtoResponseList();
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
        assertOutputContainsExpected(output, "post", saveAllUri);
    }

    @Test
    void update_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void update_whenAuthenticated_andDto_thenDelegateToService_returnSaved_andStatus201(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "put", updateUri);
    }

    @Test
    @WithMockUser
    void update_whenInvalidFieldValue_thenValidation_ErrorResponse_status400(CapturedOutput output) throws Exception {
        mvc.perform(put(updateUri)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(new DividendDtoUpdateRequest())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
        assertOutputContainsExpected(output, "put", updateUri);
    }

    @Test
    void deleteById_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(put(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

        verifyNoInteractions(service, linkUtil);
    }

    @Test
    @WithMockUser
    void deleteById_whenId_thenDelegateToService_status204(CapturedOutput output) throws Exception {
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