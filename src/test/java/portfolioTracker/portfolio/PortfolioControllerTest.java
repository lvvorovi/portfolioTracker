package portfolioTracker.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import portfolioTracker.portfolio.service.PortfolioService;
import portfolioTracker.util.JsonUtil;

@WebMvcTest(value = {PortfolioController.class, JsonUtil.class/*, PortfolioService.class*/})
@Import(OutputCaptureExtension.class)
@ActiveProfiles("logging-test")
class PortfolioControllerTest {

    @MockBean
    PortfolioService service;
    @Autowired
    MockMvc mvc;
    @Autowired
    JsonUtil jsonUtil;


    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}