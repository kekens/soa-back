package com.kekens.soa_lab_1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class JsonUtil<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil(Class<T> type) {
        this.type = type;
        objectMapper.findAndRegisterModules();
    }

    public T buildObjectFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }

        return objectMapper.readValue(sb.toString(), type);
    }

    public String buildJsonStringFromObject(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public String buildJsonStringFromList(List<T> list) throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }
}
