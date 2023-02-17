package dev.rabbit.transform;

import com.google.api.services.bigquery.model.TableRow;
import dev.rabbit.pojo.Edge;
import org.apache.beam.sdk.transforms.Combine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BuildGraph extends Combine.CombineFn<TableRow, ConcurrentHashMap<String, List<Edge>>, ConcurrentHashMap<String, List<Edge>>> {

    public static BuildGraph of() {
        return new BuildGraph();
    }

    @Override
    public ConcurrentHashMap<String, List<Edge>> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public ConcurrentHashMap<String, List<Edge>> addInput(ConcurrentHashMap<String, List<Edge>> accumulator, TableRow input) {
        addRow(accumulator, input);
        return accumulator;
    }

    @Override
    public ConcurrentHashMap<String, List<Edge>> mergeAccumulators(Iterable<ConcurrentHashMap<String, List<Edge>>> accumulators) {
        ConcurrentHashMap<String, List<Edge>> merged = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<String, List<Edge>> accumulator : accumulators) {
            merged.putAll(accumulator);
        }
        return merged;
    }

    @Override
    public ConcurrentHashMap<String, List<Edge>> extractOutput(ConcurrentHashMap<String, List<Edge>> accumulator) {
        return accumulator;
    }

    @SuppressWarnings("unchecked")
    private void addRow(ConcurrentHashMap<String, List<Edge>> graph, TableRow row) {
        String startingNode = (String) row.get("node");
        List<Map<String, Object>> tableRowEdges = (List<Map<String, Object>>) row.get("edges");
        List<Edge> edges = tableRowEdges.stream().map((edge -> {
            String endingNode = (String) edge.get("node");
            return Edge.of(endingNode, (Map<String, Object>) edge.get("swap"));
        })).collect(Collectors.toList());
        graph.put(startingNode, edges);
    }
}
