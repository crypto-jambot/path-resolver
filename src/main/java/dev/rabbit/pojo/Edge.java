package dev.rabbit.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Edge implements Serializable {
    private final String node;
    private final Swap swap;

    public static Edge of(String node, Map<String, Object> swapMap) {
        return new Edge(node, Swap.of(swapMap));
    }
}
