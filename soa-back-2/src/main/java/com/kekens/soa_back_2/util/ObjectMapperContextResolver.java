package com.kekens.soa_back_2.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;

    public ObjectMapperContextResolver() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}