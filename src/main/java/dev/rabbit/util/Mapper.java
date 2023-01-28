package dev.rabbit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rabbit.pojo.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper implements Serializable {
    public static final Mapper INSTANCE = new Mapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String toJsonString(Path path) {
        try {
            return objectMapper.writeValueAsString(path);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
