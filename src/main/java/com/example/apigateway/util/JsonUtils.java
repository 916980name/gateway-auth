package com.example.apigateway.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .registerModule(new JodaModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private JsonUtils() {
        throw new IllegalStateException("JsonUtils class");
    }

    public static String marshal(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T unmarshal(String jsonString, Class<T> type) throws IOException {
        return objectMapper.readValue(jsonString, type);
    }

    public static <T> T unmarshal(String jsonString, TypeReference<T> type) throws IOException {
        return objectMapper.readValue(jsonString, type);
    }

    public static <T> T transformTo(Object object, Class<T> type) throws IOException {
        if (object == null) {
            throw new IllegalArgumentException("source object can not be null.");
        }
        return unmarshal(marshal(object), type);
    }

    public static <T> T treeToObject(JsonNode node, TypeReference<T> type) throws IOException {
        return objectMapper.readerFor(type).readValue(node);
    }

    public static <T> T treeToObject(JsonNode node, Class<T> type) throws IOException {
        return objectMapper.treeToValue(node, type);
    }

    public static String silentMarshal(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("[silentMarshal]", e);
        }
        return null;
    }

    public static <T> T silentUnmarshal(String jsonString, Class<T> type) {
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException e) {
            log.error("[silentUnmarshal]", e);
        }
        return null;
    }

    public static <T> T bytesToObject(byte[] bytes, Class<T> clazz) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }

    public static <T> T streamToObject(InputStream is, Class<T> clazz) throws IOException {
        return objectMapper.readValue(is, clazz);
    }

    public static <T> Object getSpecialDataInParametricType(String s, Class baseClazz, Class<T> parametricClazz) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new JodaModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        final JavaType javaType = objectMapper.getTypeFactory().constructParametricType(baseClazz, parametricClazz);
        final Object obj = objectMapper.readValue(s, javaType);
        return obj;

    }
}

