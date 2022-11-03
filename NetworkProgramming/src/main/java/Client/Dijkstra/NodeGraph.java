/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.Dijkstra;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author trankimphu0609
 */
public class NodeGraph extends JPanel {

    JPanel panel = new JPanel(new GridLayout()) {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Constant.nodeGraphWidth, Constant.mainHeight);
        }
    };
    private String[] path = {};
    private ArrayList<NodeEdge> edges = new ArrayList<>();

    public NodeGraph(String[] path, ArrayList<NodeEdge> edges) {
        this.path = path;
        this.edges = edges;

        init();
    }

    private void init() {
        // Using swing for library GraphStream
        System.setProperty("org.graphstream.ui", "swing");

        // Create new Graph
        Graph graph = new SingleGraph("Node Graph", false, true);

        // Apply file graphstyle.css to graph
        graph.setAttribute("ui.stylesheet", "url('src/main/java/client/dijkstra/graphstyle.css')");

        // Gender Edges & Nodes
        for (NodeEdge edge : edges) {
            Node a = graph.addNode(edge.getSrc());
            Node b = graph.addNode(edge.getDestination());
            org.graphstream.graph.Edge e = graph.addEdge(edge.getSrc() + edge.getDestination(), a, b, true);
            e.setAttribute("ui.label", edge.getWeight());
        }

        // Add label for each Node
        for (Node node : graph) {
            node.setAttribute("ui.label", node.getId());
        }

        // Active shorted path with color red
        for (int i = 0; i < path.length; i++) {
            String nodeId = path[i];
            String nextNodeId = "";
            try {
                nextNodeId = path[i + 1];
            } catch (Exception e) {
            }
            // active node
            graph.getNode(nodeId).setAttribute("ui.class", "active");

            // active edge
            if (!nextNodeId.isEmpty()) {
                graph.getEdge(nodeId + nextNodeId).setAttribute("ui.class", "active");
            }
        }

        SwingViewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        ViewPanel viewPanel = (ViewPanel) viewer.addDefaultView(false);

        panel.add(viewPanel);

        this.add(panel);
    }
}

