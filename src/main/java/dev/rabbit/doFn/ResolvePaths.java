package dev.rabbit.doFn;

import dev.rabbit.pojo.Edge;
import dev.rabbit.pojo.Node;
import dev.rabbit.pojo.NodeList;
import dev.rabbit.pojo.Path;
import dev.rabbit.pojo.Swap;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.List;
import java.util.Map;

public class ResolvePaths extends DoFn<Map<Node, List<Edge>>, Path> {

    public static ResolvePaths of() {
        return new ResolvePaths();
    }

    @ProcessElement
    public void processElement(@Element Map<Node, List<Edge>> element, OutputReceiver<Path> out) {
        for (NodeList nodeInfo : NodeList.values()) {
            System.out.println("Starting the algorithm for " + nodeInfo.getNode().getName() + " with a search depth of " + nodeInfo.getSearchDepth());
            long startTime = System.currentTimeMillis();
            long numPaths = new DepthFirstSearch(nodeInfo, element, out).run();
            long endTime = System.currentTimeMillis();
            System.out.println("Time Taken: " + (endTime - startTime) + "milliseconds.");
            System.out.println("Number of paths: " + numPaths);
        }
    }

    private static class DepthFirstSearch {
        private final Node startingNode;
        private final Path path = Path.of();
        private int numPaths = 0;
        private final int searchDepth;
        private final Map<Node, List<Edge>> graph;
        private final OutputReceiver<Path> out;

        public DepthFirstSearch(NodeList nodeList, Map<Node, List<Edge>> graph, OutputReceiver<Path> out) {
            startingNode = nodeList.getNode();
            path.addNode(startingNode);
            searchDepth = nodeList.getSearchDepth();
            this.graph = graph;
            this.out = out;
        }

        public int run() {
            List<Edge> edges = graph.get(startingNode);
            edges.forEach(edge -> {
                // Add to the path.
                path.addNode(edge.getNode());
                path.addSwap(edge.getSwap());
                // Search through that branch
                dfs(edge);
                // Remove from the path.
                path.popToken().popSwap();
            });
            return numPaths;
        }

        private void dfs(Edge edge) {
            graph.get(edge.getNode()).forEach(nestedEdge -> {
                Node nestedNode = nestedEdge.getNode();
                Swap nestedSwap = nestedEdge.getSwap();
                // Conditions must be met to be a valid path
                // 1. The ending node must equal the starting node. ex. WETH -> TOKEN_2 -> TOKEN_N -> WETH
                // 2. Both of the following condition must not both be true:
                //    - The total path size is 3 (current size plus the node we are peeking)
                //    - There is only one exchange option for the last trade.
                //    This implies a 2 trade path where the only option is swapping twice on the same pool.
                if (nestedNode.equals(startingNode) && !(path.nodeSize() == 2 && nestedSwap.exchangeSize() == 1)) {
                    numPaths++;
                    out.output(Path.of(path).addNode(nestedNode).addSwap(nestedSwap).deriveUniquePools());
                }

                // If the path doesn't already contain the next node AND is within the search depth limit.
                if (path.nodeSize() < searchDepth && !path.containsNode(nestedNode)) {
                    // Add to the path.
                    path.addNode(nestedNode);
                    path.addSwap(nestedSwap);
                    // Search through that branch
                    dfs(nestedEdge);
                    // Remove from the path.
                    path.popToken().popSwap();
                }
            });
        }
    }
}
