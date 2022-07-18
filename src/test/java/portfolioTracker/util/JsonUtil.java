package portfolioTracker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class JsonUtil {

    public static String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(dateFormat);
        return mapper.writeValueAsString(object);
    }

    public static List<DividendDtoResponse> jsonToDividendDtoResponseList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DividendDtoResponse[] responseArray = mapper.readValue(json, DividendDtoResponse[].class);
        return List.of(responseArray);
    }

    public static DividendDtoResponse jsonToDividendDtoResponse(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, DividendDtoResponse.class);
    }

    public static TransactionDtoResponse jsonToTransactionDtoResponse(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, TransactionDtoResponse.class);
    }

    public static List<TransactionDtoResponse> jsonToTransactionDtoResponseList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        TransactionDtoResponse[] responseArray = mapper.readValue(json, TransactionDtoResponse[].class);
        return List.of(responseArray);
    }

}
