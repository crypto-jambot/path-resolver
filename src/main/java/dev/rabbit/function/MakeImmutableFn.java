package dev.rabbit.function;

import dev.rabbit.pojo.Edge;
import dev.rabbit.pojo.Node;
import org.apache.beam.sdk.transforms.SimpleFunction;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MakeImmutableFn extends SimpleFunction<ConcurrentHashMap<Node, List<Edge>>, Map<Node, List<Edge>>> {

    public static MakeImmutableFn of() {
        return new MakeImmutableFn();
    }

    @Override
    public Map<Node, List<Edge>> apply(ConcurrentHashMap<Node, List<Edge>> input) {
        return Collections.unmodifiableMap(input);
    }
}
