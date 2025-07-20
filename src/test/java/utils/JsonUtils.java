package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    public static JsonNode lerJsonArray(String caminho) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(caminho);
        return mapper.readTree(inputStream);
    }
}
