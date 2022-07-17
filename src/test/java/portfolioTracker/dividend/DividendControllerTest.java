package portfolioTracker.dividend;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Disabled;
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
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.service.DividendService;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static portfolioTracker.dividend.DividendTestUtil.*;
import static portfolioTracker.util.JsonUtil.*;

@WebMvcTest({DividendController.class})
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("logging-test")
class DividendControllerTest {

    private final String id = "id";
    private final String username = "user";
    private final String portfolioId = "portfolioId";
    private final URI findByIdUri = linkTo(methodOn(DividendController.class).findById(id)).toUri();
    private final URI findAllUri = linkTo(DividendController.class).toUri();
    private final URI saveUri = linkTo(DividendController.class).toUri();
    private final URI saveAllUri = linkTo(methodOn(DividendController.class).saveAll(new ValidList<>())).toUri();
    private final URI deleteByIdUri = linkTo(methodOn(DividendController.class).deleteById(id)).toUri();
    private final URI updateUri = linkTo(DividendController.class).toUri();


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
    void findById_whenId_thenReturnJson_andStatus200(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "get", findByIdUri);
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
    void findAll_whenIsAuth_thenReturnJsonArray_andStatus200(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "get", findAllUri);
    }

    @WithMockUser
    @Test
    void findAll_whenIsAuth_andIsParam_thenReturnJsonArray_andStatus200(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "get", findAllUri);
    }

    @Test
    void save_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void save_whenIllegalDto_thenValidates_andReturns400(CapturedOutput output) throws Exception {
        DividendDtoCreateRequest illegalRequestDto = new DividendDtoCreateRequest();

        mvc.perform(post(saveUri)
                        .with(csrf())
                        .content(objectToJson(illegalRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());

        verifyNoInteractions(service, linkUtil);
        assertOutputContainsExpected(output,"post", saveUri);
    }

    @WithMockUser
    @Test
    void save_whenDto_thenReturnJson_andStatus201(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output,"post", saveUri);
    }

    @Test
    void saveAll_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void saveAll_whenDto_thenReturnJson_andStatus201(CapturedOutput output) throws Exception {
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

        assertOutputContainsExpected(output,"post", saveAllUri);

    }

    @Test
    void deleteById_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(delete(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(401))
                .andExpect(content().string(emptyOrNullString()));

       verifyNoInteractions(service, linkUtil);
    }

    @WithMockUser
    @Test
    void deleteById_whenId_thenDelegateToService_andStatus204(CapturedOutput output) throws Exception {
        doNothing().when(service).deleteById(id);

        mvc.perform(delete(deleteByIdUri)
                        .with(csrf()))
                .andExpect(status().is(204))
                .andExpect(content().string(emptyOrNullString()));

        verify(service, times(1)).deleteById(id);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(linkUtil);
        assertOutputContainsExpected(output, "delete", deleteByIdUri);
    }

    @Test
    void update_whenNotAuthenticated_thenStatus401() throws Exception {
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
    void update_whenDto_thenDelegateToService_andStatus200(CapturedOutput output) throws Exception {
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
        assertOutputContainsExpected(output, "put", updateUri);
    }

/*
    //DTO validation testing

    @WithMockUser
    @Test
    @Disabled
    void save_whenAllFieldsNull_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest requestDto = newAllFieldsNullCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        saveRequestAllFieldsNullErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    @Disabled
    void save_whenBlankFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest requestDto = newTickerBlankCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        saveTickerBlankRequestErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    @Disabled
    void save_whenTooLongFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest requestDto = newTickerTooLongCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        saveTickerTooLongRequestErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    @Disabled
    void update_whenAllFieldsNull_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest requestDto = newAllFieldsNullUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        allFieldsNullUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    @Disabled
    void update_whenBlankFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest requestDto = newFieldsBlankUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        fieldsBlankUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    @Disabled
    void update_whenTooLongFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest requestDto = newTooLongFieldsUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        tooLongFieldsUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultContent.contains(message)));
        verifyNoInteractions(service);
    }*/


}