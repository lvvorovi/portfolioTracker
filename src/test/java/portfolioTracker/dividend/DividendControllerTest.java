package portfolioTracker.dividend;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.ValidList;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.service.DividendService;
import portfolioTracker.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static portfolioTracker.dividend.DividendTestUtil.*;

@RunWith(SpringRunner.class)
@WebMvcTest({DividendController.class, JsonUtil.class, JavaTimeModule.class})
@ExtendWith(OutputCaptureExtension.class)
@ActiveProfiles("logging-test")
class DividendControllerTest {

    private final String id = "id";
    private final String username = "user";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonUtil jsonUtil;
    @MockBean
    private DividendService service;

    @Test
    void findById_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(get(linkTo(methodOn(DividendController.class)
                        .findById(id)).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));

        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void findById_whenId_thenReturnJson_andStatus200(CapturedOutput output) throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse response = newDividendResponseDto(entity);
        when(service.findById(entity.getId())).thenReturn(response);

        MvcResult result = mvc.perform(get(linkTo(methodOn(DividendController.class)
                        .findById(entity.getId())).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost" +
                                linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertEquals(response, jsonUtil.jsonToDividendDtoResponseIgnoreLinks(responseBody));
        verify(service, times(1)).findById(entity.getId());
        assertOutputForLogging(output);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAll_whenNotAuthenticated_thenStatus401() throws Exception {
        MvcResult result = mvc.perform(get(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, body);
        verifyNoInteractions(service);
    }

    @WithMockUser(username = username)
    @Test
    void findAll_whenAuthenticated_thenReturnJsonArray_andStatus200(CapturedOutput output) throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse response = newDividendResponseDto(entity);
        ArrayList<DividendDtoResponse> dividendList = newDividendDtoResponseList(response);
        when(service.findAllByUsername(username)).thenReturn(dividendList);

        MvcResult result = mvc
                .perform(get(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[0].links[0].rel", is("self")))

                .andExpect(jsonPath("$[1].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[1].links[0].rel", is("self")))

                .andExpect(jsonPath("$[2].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[2].links[0].rel", is("self")))

                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals(dividendList, jsonUtil.jsonToDividendDtoResponseList(body));
        verify(service, times(1)).findAllByUsername(username);
        assertOutputForLogging(output);
    }

    @WithMockUser
    @Test
    void findAll_whenAuthenticated_andPortfolioIdParam_thenReturnJsonArray_andStatus200(CapturedOutput output) throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse response = newDividendResponseDto(entity);
        ArrayList<DividendDtoResponse> dividendList = newDividendDtoResponseList(response);
        when(service.findAllByPortfolioId(entity.getPortfolio().getId())).thenReturn(dividendList);

        MvcResult result = mvc
                .perform(get(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .param("portfolioId", entity.getPortfolio().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[0].links[0].rel", is("self")))

                .andExpect(jsonPath("$[1].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[1].links[0].rel", is("self")))

                .andExpect(jsonPath("$[2].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[2].links[0].rel", is("self")))

                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals(dividendList, jsonUtil.jsonToDividendDtoResponseList(body));
        verify(service, times(1)).findAllByPortfolioId(entity.getPortfolio().getId());
        assertOutputForLogging(output);
    }

    @Test
    void save_whenNotAuthenticated_thenStatus401() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().is(401))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, resultBody);
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void save_whenIllegalDto_thenValidates_andReturns400(CapturedOutput output) throws Exception {
        DividendDtoCreateRequest illegalRequest = new DividendDtoCreateRequest();
        mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .content(jsonUtil.objectToJson(illegalRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
        assertOutputForLogging(output);
    }

    @WithMockUser
    @Test
    void save_whenDto_thenReturnJson_andStatus201(CapturedOutput output) throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        when(service.save(request)).thenReturn(response);

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(response, jsonUtil.jsonToDividendDtoResponseIgnoreLinks(resultBody));
        verify(service, times(1)).save(request);
        assertOutputForLogging(output);
    }

    @Test
    void saveAll_whenNotAuthenticated_thenStatus401() throws Exception {
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(newDividendEntity());
        ValidList<DividendDtoCreateRequest> requestList = newDividendDtoCreateList(request);

        mvc.perform(post(linkTo(methodOn(DividendController.class).saveAll(requestList)).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(requestList)))
                .andExpect(status().is(401));

        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void saveAll_whenDto_thenReturnJson_andStatus201(CapturedOutput output) throws Exception {
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(newDividendEntity());
        DividendDtoResponse response = newDividendResponseDto(newDividendEntity());
        ValidList<DividendDtoCreateRequest> requestList = newDividendDtoCreateList(request);
        List<DividendDtoResponse> responseList = newDividendDtoResponseList(response);
        when(service.saveAll(requestList)).thenReturn(responseList);

        MvcResult result = mvc.perform(post(linkTo(methodOn(DividendController.class)
                        .saveAll(any()))
                        .toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(requestList)))
                .andExpect(status().is(201))

                .andExpect(jsonPath("$[0].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[0].links[0].rel", is("self")))

                .andExpect(jsonPath("$[1].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[1].links[0].rel", is("self")))

                .andExpect(jsonPath("$[2].links[0].href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))
                .andExpect(jsonPath("$[2].links[0].rel", is("self")))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals(responseList, jsonUtil.jsonToDividendDtoResponseList(body));
        verify(service, times(1)).saveAll(requestList);
        verifyNoMoreInteractions(service);
        assertOutputForLogging(output);
    }

    @Test
    void deleteById_whenNotAuthenticated_thenStatus401() throws Exception {
        MvcResult result = mvc.perform(delete(linkTo(methodOn(DividendController.class)
                        .deleteById(id)).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, resultBody);
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void deleteById_whenId_thenDelegateToService_andStatus204(CapturedOutput output) throws Exception {
        doNothing().when(service).deleteById(id);
        MvcResult result = mvc.perform(delete(linkTo(methodOn(DividendController.class)
                        .deleteById(id)).toUri())
                        .with(csrf()))
                .andExpect(status().is(204))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, resultBody);
        verify(service, times(1)).deleteById(id);
        verifyNoMoreInteractions(service);
        assertOutputForLogging(output);
    }

    @Test
    void update_whenNotAuthenticated_thenStatus401() throws Exception {
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(newDividendEntity());
        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().is(401))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, resultBody);
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void update_whenDto_thenDelegateToService_andStatus200(CapturedOutput output) throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        when(service.update(request)).thenReturn(response);

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().is(200))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost" +
                        linkTo(methodOn(DividendController.class).findById(response.getId())))))

                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(response, jsonUtil.jsonToDividendDtoResponseIgnoreLinks(resultBody));
        verify(service, times(1)).update(request);
        verifyNoMoreInteractions(service);
        assertOutputForLogging(output);
    }

    @WithMockUser
    @Test
    void save_whenAllFieldsNull_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest request = newAllFieldsNullCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        saveRequestAllFieldsNullErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void save_whenBlankFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest request = newTickerBlankCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        saveTickerBlankRequestErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void save_whenTooLongFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoCreateRequest request = newTickerTooLongCreateRequest();

        MvcResult result = mvc.perform(post(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        saveTickerTooLongRequestErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void update_whenAllFieldsNull_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest request = newAllFieldsNullUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        allFieldsNullUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void update_whenBlankFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest request = newFieldsBlankUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        fieldsBlankUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void update_whenTooLongFields_thenErrorMessage_andStatus400() throws Exception {
        DividendDtoUpdateRequest request = newTooLongFieldsUpdateRequest();

        MvcResult result = mvc.perform(put(linkTo(DividendController.class).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        tooLongFieldsUpdateRequestErrorMessageList().forEach(message -> assertTrue(resultBody.contains(message)));
        verifyNoInteractions(service);
    }

    private void assertOutputForLogging(CapturedOutput output) {
        assertThat(output.toString()).contains("INFO");
        assertThat(output.toString()).contains("MyLoggerFilter");
        assertThat(output.toString()).contains("Request id");
    }


}