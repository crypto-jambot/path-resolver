package dev.rabbit.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Path implements Serializable {

    @JsonProperty
    private LinkedList<Node> tokenPath = new LinkedList<>();
    @JsonProperty
    private LinkedList<Swap> swapPath = new LinkedList<>();
    @JsonProperty
    private HashSet<String> addresses = new HashSet<>();

    public static Path of() {
        return new Path();
    }

    public static Path of(Path path) {
        Path newPath = new Path();
        newPath.tokenPath = (LinkedList<Node>) path.tokenPath.clone();
        newPath.swapPath = (LinkedList<Swap>) path.swapPath.clone();
        return newPath;
    }

    public Path addNode(Node node) {
        tokenPath.addLast(node);
        return this;
    }

    public Path addSwap(Swap swap) {
        swapPath.addLast(swap);
        return this;
    }

    public Path popToken() {
        tokenPath.removeLast();
        return this;
    }

    public Path popSwap() {
        swapPath.removeLast();
        return this;
    }

    public boolean containsNode(Node node) {
        return tokenPath.contains(node);
    }

    public boolean containsSwap(Swap swap) {
        return swapPath.contains(swap);
    }

    public int nodeSize() {
        return tokenPath.size();
    }

    public int swapSize() {
        return swapPath.size();
    }

    public TableRow toTableRow() {
        TableRow tableRow = new TableRow();
        List<Map<String, Object>> tokenPathField = tokenPath.stream().map(Node::toMap).collect(Collectors.toList());
        List<Map<String, Object>> swapPathField = swapPath.stream().map(Swap::toMap).collect(Collectors.toList());
        tableRow.put("tokenPath", tokenPathField);
        tableRow.put("swapPath", swapPathField);
        return tableRow;
    }

    public Path deriveUniquePools() {
        this.swapPath.forEach(swap -> {
            swap.getExchanges().forEach(exchange -> {
                this.addresses.add(exchange.getPool());
            });
        });
        return this;
    }
}
