package com.kekens.soa_lab_1.util;

import com.google.gson.Gson;
import com.kekens.soa_lab_1.model.LabWork;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class JsonUtil<T> {

    private final Class<T> type;
    private final Gson gson = new Gson();

    public JsonUtil(Class<T> type) {
        this.type = type;
    }

    public T buildObjectFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }

        return gson.fromJson(sb.toString(), type);
    }

    public String buildJsonStringFromObject(T object) {
        return gson.toJson(object);
    }

    public String buildJsonStringFromList(List<T> list) {
        return gson.toJson(list);
    }
}
