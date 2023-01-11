package dev.rabbit.doFn;

import com.google.api.services.bigquery.model.TableRow;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepthFirstSearch extends DoFn<List<TableRow>, TableRow> {

    private final Map<Node, List<Edge>> graph = new HashMap<>();

    public static DepthFirstSearch of() {
        return new DepthFirstSearch();
    }

    @ProcessElement
    public void processElement(@Element List<TableRow> element, OutputReceiver<TableRow> out) {
        buildGraph(element);
//        System.out.println("Starting the algorithm.");
//        long startTime = System.currentTimeMillis();
        long numPaths = resolvePaths(out);
//        long endTime = System.currentTimeMillis();
//        System.out.println("Time Taken: " + (endTime - startTime) / 1000 + "seconds.");
        element.forEach(out::output);
    }

    private void buildGraph(List<TableRow> rows) {
        rows.forEach(this::addRow);
    }

    @SuppressWarnings("unchecked")
    private void addRow(TableRow row) {
        Node startingNode = Node.of((Map<String, Object>) row.get("node"));
        List<Map<String, Object>> tableRowEdges = (List<Map<String, Object>>) row.get("edges");
        List<Edge> edges = tableRowEdges.stream().map((edge -> {
            Node endingNode = Node.of((Map<String, Object>) edge.get("node"));
            Swap swap = Swap.of((Map<String, Object>) edge.get("swap"));
            return Edge.of(endingNode, swap);
        })).collect(Collectors.toList());
        graph.put(startingNode, edges);
    }

    private long resolvePaths(OutputReceiver<TableRow> out) {
        return 1;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class Node {
        private final String name;
        private final String symbol;
        private final String address;
        private final int decimals;

        public static <K, V> Node of(Map<K, V> nodeMap) {
            return new Node(
                    (String) nodeMap.get("name"),
                    (String) nodeMap.get("symbol"),
                    (String) nodeMap.get("address"),
                    nodeMap.get("decimals") != null ? Integer.parseInt((String) nodeMap.get("decimals")) : 0);
        }
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Edge {
        private final Node node;
        private final Swap swap;

        public static Edge of(Node node, Swap swap) {
            return new Edge(node, swap);
        }
    }

    @Getter
    @EqualsAndHashCode
    @SuppressWarnings("unchecked")
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Swap {
        private final List<Exchange> exchanges;
        private final boolean ordered;

        public static <K, V> Swap of(Map<K, V> swapMap) {
            List<Exchange> exchanges = ((List<Map<String, Object>>) swapMap.get("exchanges")).stream().map(Exchange::of).collect(Collectors.toList());
            return new Swap(exchanges, (boolean) swapMap.get("ordered"));
        }
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Exchange {
        private final String name;
        private final int fee;
        private final String pool;

        public static <K, V> Exchange of(Map<K, V> exchangeMap) {
            return new Exchange(
                    (String) exchangeMap.get("name"),
                    Integer.parseInt((String) exchangeMap.get("fee")),
                    (String) exchangeMap.get("pool"));
        }
    }

    public enum NodeList {
        WETH(new Node("Wrapped Ether", "WETH", "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2", 18), 4),
        USDC(new Node("USD Coin", "USDC", "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", 6), 4),
        USDT(new Node("Tether USD", "USDT", "0xdAC17F958D2ee523a2206206994597C13D831ec7", 6), 4),
        DAI(new Node("Dai Stablecoin", "DAI", "0x6B175474E89094C44Da98b954EedeAC495271d0F", 18), 4),
        WBTC(new Node("Wrapped BTC", "WBTC", "0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599", 8), 4),
        UNI(new Node("Uniswap", "UNI", "0x1f9840a85d5aF5bf1D1762F925BDADdC4201F984", 18), 4),
        SOTU(new Node("Seed Of The Universe", "SOTU", "0xa91c82B6933105C23298E631A0C1Be751D239162", 18), 4),
        ICHI(new Node("ICHI", "ICHI", "0x111111517e4929D3dcbdfa7CCe55d30d4B6BC4d6", 18), 4),
        LINK(new Node("ChainLink Token", "LINK", "0x514910771AF9Ca656af840dff83E8264EcF986CA", 18), 4),
        SUSHI(new Node("SushiToken", "SUSHI", "0x6B3595068778DD592e39A122f4f5a5cF09C90fE2", 18), 4);

        private final Node node;
        private final int searchDepth;

        NodeList(Node node, int searchDepth) {
            this.node = node;
            this.searchDepth = searchDepth;
        }

        public Node getNode() {
            return node;
        }

        public int getSearchDepth() {
            return searchDepth;
        }
    }
}
