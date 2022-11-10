/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.Dijkstra;

import com.google.gson.Gson;
import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author trankimphu0609
 */
public final class Dijkstra {

    private final Gson gson = new Gson();
    private ArrayList<NodeEdge> edges = new ArrayList<>();
    boolean isValid = true;

    public Dijkstra(String edges) throws IOException {
        applyEdgesFromJsonString(edges);
    }

    public boolean validateGraph(String nodeStart, String nodeEnd) {
        GraphBuilder<String, Integer> g = GraphBuilder.<String, Integer>create(); // bắt đầu đồ thị

        for (int i = 0; i < edges.size(); i++) { // kết nối edge tới đồ thị
            NodeEdge edge = edges.get(i);
            g.connect(edge.getSource()).to(edge.getDestination()).withEdge(edge.getWeight());
        }

        HipsterGraph<String, Integer> graph = g.createUndirectedGraph();

        Iterable<String> vertices = graph.vertices();
        String mainNode = nodeStart.isBlank() ? edges.get(0).getSource() : nodeStart;

        // tạo tìm kiếm từ mainNode
        SearchProblem p = GraphSearchProblem
                .startingFrom(mainNode)
                .in(graph)
                .takeCostsFromEdges()
                .build();

        if (!nodeEnd.isBlank()) { // nếu có nodeEnd
            Algorithm.SearchResult result = Hipster.createDijkstra(p).search(nodeEnd);
            List<String> path = (List<String>) result.getOptimalPaths().get(0);
            if (!path.contains(nodeEnd)) {
                isValid = false;
                return false;
            }
        } else {
            vertices.forEach((destNode) -> { // đồ thị sẽ hợp lệ là mỗi nút có đường dẫn từ mainNode đến destNode
                Algorithm.SearchResult result = Hipster.createDijkstra(p).search(destNode); // tìm kiếm đường đi ngắn nhất
                List<String> path = (List<String>) result.getOptimalPaths().get(0);
                if (!path.contains(destNode)) {
                    isValid = false;
                    return;
                }
            });
        }

        return isValid;
    }

    public List<String> getShortestPath(String nodeStart, String nodeEnd) {
        try {
            GraphBuilder<String, Integer> g = GraphBuilder.<String, Integer>create(); // bắt đầu đồ thị
            for (int i = 0; i < edges.size(); i++) { // kết nối edge tới đồ thị
                NodeEdge edge = edges.get(i);
                g.connect(edge.getSource()).to(edge.getDestination()).withEdge(edge.getWeight());
            }

            HipsterGraph<String, Integer> graph = g.createUndirectedGraph();

            SearchProblem p = GraphSearchProblem
                    .startingFrom(nodeStart)
                    .in(graph)
                    .takeCostsFromEdges()
                    .build();

            Algorithm.SearchResult result = Hipster.createDijkstra(p).search(nodeEnd); // tìm kiếm đường đi ngắn nhất
            List<String> path = (List<String>) result.getOptimalPaths().get(0);
            System.out.println(path);
            return path;
        } catch (Exception e) {
            System.out.println("Get Shortest Path Fail!!!");
            return null;
        }
    }

    public void applyEdgesFromJsonString(String string) {
        List list = gson.fromJson(string, List.class);
        ArrayList<NodeEdge> newEdges = new ArrayList<>();

        for (Object objEdge : list) {
            NodeEdge edge = gson.fromJson(gson.toJson(objEdge), NodeEdge.class);
            newEdges.add(edge);
        }

        this.edges = newEdges;
    }
}

