package portfolioTracker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonUtil {
//    @Autowired
//    private JavaTimeModule javaTimeModule;

    public byte[] objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsBytes(object);
    }

    public List<DividendDtoResponse> jsonToDividendDtoResponseList(String string) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DividendDtoResponse[] response = mapper.readValue(string, DividendDtoResponse[].class);
        return List.of(response);
    }

    public DividendDtoResponse jsonToDividendDtoResponseIgnoreLinks(String string) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(string, DividendDtoResponse.class);
    }

}
