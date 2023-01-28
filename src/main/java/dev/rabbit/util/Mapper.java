package dev.rabbit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper implements Serializable {
    public static final Mapper INSTANCE = new Mapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> readJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Map.class);
    }

    public <K,V> String toJsonString(Map<K, V> row) {
        try {
            return objectMapper.writeValueAsString(row);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
