package dev.rabbit.transform;

import com.google.api.services.bigquery.model.TableRow;
import dev.rabbit.pojo.Edge;
import dev.rabbit.pojo.Node;
import org.apache.beam.sdk.transforms.Combine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BuildGraph extends Combine.CombineFn<TableRow, ConcurrentHashMap<Node, List<Edge>>, ConcurrentHashMap<Node, List<Edge>>> {

    public static BuildGraph of() {
        return new BuildGraph();
    }

    @Override
    public ConcurrentHashMap<Node, List<Edge>> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public ConcurrentHashMap<Node, List<Edge>> addInput(ConcurrentHashMap<Node, List<Edge>> accumulator, TableRow input) {
        addRow(accumulator, input);
        return accumulator;
    }

    @Override
    public ConcurrentHashMap<Node, List<Edge>> mergeAccumulators(Iterable<ConcurrentHashMap<Node, List<Edge>>> accumulators) {
        ConcurrentHashMap<Node, List<Edge>> merged = new ConcurrentHashMap<>();
        for (ConcurrentHashMap<Node, List<Edge>> accumulator : accumulators) {
            merged.putAll(accumulator);
        }
        return merged;
    }

    @Override
    public ConcurrentHashMap<Node, List<Edge>> extractOutput(ConcurrentHashMap<Node, List<Edge>> accumulator) {
        return accumulator;
    }

    @SuppressWarnings("unchecked")
    private void addRow(ConcurrentHashMap<Node, List<Edge>> graph, TableRow row) {
        Node startingNode = Node.of((Map<String, Object>) row.get("node"));
        List<Map<String, Object>> tableRowEdges = (List<Map<String, Object>>) row.get("edges");
        List<Edge> edges = tableRowEdges.stream().map((edge -> {
            Node endingNode = Node.of((Map<String, Object>) edge.get("node"));
            return Edge.of(endingNode, (Map<String, Object>) edge.get("swap"));
        })).collect(Collectors.toList());
        graph.put(startingNode, edges);
    }
}
