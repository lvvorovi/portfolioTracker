package portfolioTracker.dividend;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import portfolioTracker.core.ValidList;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.service.DividendService;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.util.JsonUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import static portfolioTracker.dto.eventType.EventType.DIVIDEND;

@RunWith(SpringRunner.class)
@WebMvcTest({DividendController.class, JsonUtil.class, JavaTimeModule.class})
class DividendControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonUtil jsonUtil;
    @MockBean
    private DividendService service;

    @Test
    void findById_whenNotAuthenticated_thenStatus401() throws Exception {
        mvc.perform(get(linkTo(methodOn(DividendController.class)
                        .findById(1L)).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));

        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void findById_whenId_thenReturnJson_andStatus200() throws Exception {
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
    }

/*    @WithMockUser
    @Test
    void findById_whenIllegalId_thenStatus400() throws Exception {
        MvcResult result = mvc.perform(get("http://localhost:8080/api/v1/dividends/abc")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertTrue(resultBody.contains("errorMessage"));
        verifyNoInteractions(service);
    }*/ //TODO refactor IDs to UUID

    @Test
    void findAll_whenNotAuthenticated_thenStatus401() throws Exception {
        MvcResult result = mvc
                .perform(get(linkTo(methodOn(DividendController.class)
                        .findAll(null)).toUri())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals("", body);
        verifyNoInteractions(service);
    }

    @WithMockUser
    @Test
    void findAll_whenAuthenticated_thenReturnJsonArray_andStatus200() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse response = newDividendResponseDto(entity);
        ArrayList<DividendDtoResponse> dividendList = newDividendDtoResponseList(response);
        when(service.findAll()).thenReturn(dividendList);

        MvcResult result = mvc
                .perform(get(linkTo(methodOn(DividendController.class)
                        .findAll(null)).toUri())
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
        verify(service, times(1)).findAll();
    }

    @WithMockUser
    @Test
    void findAll_whenAuthenticated_andPortfolioIdParam_thenReturnJsonArray_andStatus200() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoResponse response = newDividendResponseDto(entity);
        ArrayList<DividendDtoResponse> dividendList = newDividendDtoResponseList(response);
        when(service.findAllByPortfolioId(entity.getPortfolio().getId())).thenReturn(dividendList);

        MvcResult result = mvc
                .perform(get(linkTo(methodOn(DividendController.class)
                        .findAll(null)).toUri())
                        .with(csrf())
                        .param("portfolioId", entity.getPortfolio().getId().toString())
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
    }

    @Test
    void save_whenNotAuthenticated_thenStatus401() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);

        MvcResult result = mvc.perform(post(linkTo(methodOn(DividendController.class)
                        .save(request)).toUri())
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
    void save_whenDto_thenReturnJson_andStatus201() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        when(service.save(request)).thenReturn(response);

        MvcResult result = mvc.perform(post(linkTo(methodOn(DividendController.class)
                        .save(request)).toUri())
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
    void saveAll_whenDto_thenReturnJson_andStatus201() throws Exception {
        DividendDtoCreateRequest request = newDividendDtoCreateRequest(newDividendEntity());
        DividendDtoResponse response = newDividendResponseDto(newDividendEntity());
        ValidList<DividendDtoCreateRequest> requestList = newDividendDtoCreateList(request);
        List<DividendDtoResponse> responseList = newDividendDtoResponseList(response);
        when(service.saveAll(requestList)).thenReturn(responseList);

        MvcResult result = mvc.perform(post(linkTo(methodOn(DividendController.class)
                        .saveAll(requestList)).toUri())
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
    }

    @Test
    void deleteById_whenNotAuthenticated_thenStatus401() throws Exception {
        MvcResult result = mvc.perform(delete(linkTo(methodOn(DividendController.class)
                        .deleteById(1L)).toUri())
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
    void deleteById_whenDto_thenDelegateToService_andStatus204() throws Exception {
        Long id = 1L;
        doNothing().when(service).deleteById(id);
        MvcResult result = mvc.perform(delete(linkTo(methodOn(DividendController.class)
                        .deleteById(id)).toUri())
                        .with(csrf()))
                .andExpect(status().is(204))
                .andReturn();

        String resultBody = result.getResponse().getContentAsString();
        assertEquals(Strings.EMPTY, resultBody);
        verify(service, times(1)).deleteById(id);
    }

    @Test
    void update_whenNotAuthenticated_thenStatus401() throws Exception {
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(newDividendEntity());
        MvcResult result = mvc.perform(put(linkTo(methodOn(DividendController.class)
                        .update(request)).toUri())
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
    void update_whenDto_thenDelegateToService_andStatus200() throws Exception {
        DividendEntity entity = newDividendEntity();
        DividendDtoUpdateRequest request = newDividendDtoUpdateRequest(entity);
        DividendDtoResponse response = newDividendResponseDto(entity);
        when(service.update(request)).thenReturn(response);

        MvcResult result = mvc.perform(put(linkTo(methodOn(DividendController.class)
                .update(request)).toUri())
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
    }

    private DividendEntity newDividendEntity() {
        DividendEntity entity = new DividendEntity();
        entity.setId(1L);
        entity.setUsername("john@email.com");
        entity.setPortfolio(newPortfolioEntity());
        entity.setAmount(new BigDecimal(100));
        entity.setDate(LocalDate.now());
        entity.setTicker("BRK-B");
        entity.setExDate(LocalDate.now().minusDays(2));
        entity.setType(DIVIDEND);
        return entity;
    }

    private DividendDtoUpdateRequest newDividendDtoUpdateRequest(DividendEntity entity) {
        DividendDtoUpdateRequest dto = new DividendDtoUpdateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setId(entity.getId());
        dto.setTicker(entity.getTicker());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        return dto;
    }

    private DividendDtoCreateRequest newDividendDtoCreateRequest(DividendEntity entity) {
        DividendDtoCreateRequest dto = new DividendDtoCreateRequest();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        dto.setUsername(entity.getUsername());
        dto.setExDate(entity.getExDate());
        dto.setTicker(entity.getTicker());
        dto.setPortfolioId(entity.getPortfolio().getId());
        dto.setType(entity.getType());
        return dto;
    }

    private DividendDtoResponse newDividendResponseDto(DividendEntity entity) {
        DividendDtoResponse dto = new DividendDtoResponse();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setUsername(entity.getUsername());
        dto.setDate(entity.getDate());
        dto.setTicker(entity.getTicker());
        dto.setExDate(entity.getExDate());
        dto.setType(entity.getType());
        dto.setPortfolioId(entity.getPortfolio().getId());
        return dto;
    }

    private ArrayList<DividendDtoResponse> newDividendDtoResponseList(DividendDtoResponse response) {
        ArrayList<DividendDtoResponse> dividendList = new ArrayList<>();
        dividendList.add(response);
        dividendList.add(response);
        dividendList.add(response);
        return dividendList;
    }

    private ValidList<DividendDtoCreateRequest> newDividendDtoCreateList(DividendDtoCreateRequest request) {
        ValidList<DividendDtoCreateRequest> dividendList = new ValidList<>();
        dividendList.add(request);
        dividendList.add(request);
        dividendList.add(request);
        return dividendList;
    }

    private PortfolioEntity newPortfolioEntity() {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(1L);
        entity.setDividendEntityList(null);
        entity.setTransactionEntityList(null);
        entity.setUsername("john@email.com");
        entity.setCurrency("EUR");
        entity.setName("portfolioName");
        entity.setStrategy("portfolioStrategy");
        return entity;
    }


}