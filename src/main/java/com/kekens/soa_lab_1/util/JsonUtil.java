package com.kekens.soa_lab_1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kekens.soa_lab_1.validator.IntegrityError;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonUtil<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil(Class<T> type) {
        this.type = type;
        objectMapper.findAndRegisterModules();
    }

    public T buildObjectFromRequest(HttpServletRequest request) throws IOException, IncorrectDataException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }

        String json = sb.toString();
        String error = validateJson(json);

        if (error.equals("")) {
            return objectMapper.readValue(json, type);
        } else {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, error)));
        }

    }

    public String buildJsonStringFromObject(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public String buildJsonStringFromList(List<T> list) throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }

    private String validateJson(String json) {
        try {
            objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
            objectMapper.treeToValue(objectMapper.readTree(json), type);
        } catch (JsonProcessingException e) {
            String message = e.getMessage();
            if (message.startsWith("Unrecognized field")) {
                return message.substring(0, message.indexOf("(") - 1).replaceAll("\\\\","").replaceAll("\"", "'");
            } else if (message.startsWith("Unexpected character")) {
                return message.substring(0, message.indexOf(" to"));
            } else if (message.startsWith("Cannot deserialize")) {
                return message.substring(0, message.indexOf(": not")).replaceAll("\\\\", "").replaceAll("\"", "'");
            } else if (message.startsWith("Unexpected end-of-input")) {
                return "Unexpected end-of-input";
            } else {
                return message;
            }
        }

        return "";
    }
}
