package dev.rabbit.function;

import dev.rabbit.pojo.Edge;
import org.apache.beam.sdk.transforms.SimpleFunction;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MakeImmutableFn extends SimpleFunction<ConcurrentHashMap<String, List<Edge>>, Map<String, List<Edge>>> {

    public static MakeImmutableFn of() {
        return new MakeImmutableFn();
    }

    @Override
    public Map<String, List<Edge>> apply(ConcurrentHashMap<String, List<Edge>> input) {
        return Collections.unmodifiableMap(input);
    }
}
