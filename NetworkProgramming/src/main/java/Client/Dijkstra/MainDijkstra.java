/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.Dijkstra;


import UI.Client;

import com.google.gson.Gson;
import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTextField;

/**
 * @author trankimphu0609
 */
public class MainDijkstra extends JPanel {

    private final Gson gson = new Gson();
    private String[] path = {};
    private final String[] emptyPath = {};
    private ArrayList<NodeEdge> edges = new ArrayList<>();
    boolean isValid = true;

    public JPanel pnlNodeGraph = new JPanel(); // left side

    public JPanel pnlRightSide = new JPanel(); // right side

    public JPanel pnlInputEdge = new JPanel(new GridBagLayout()); // them node 
    public JXTextField txtSrc = new JXTextField("");
    public JXTextField txtDest = new JXTextField("");
    public JXTextField txtWeight = new JXTextField("");
    public JButton btnAdd = new JXButton("THÊM");

    public JPanel pnlUploadFile = new JPanel();   // up file tu thu muc testfile
    public JLabel lblFileName = new JLabel();
    public JButton btnChooseFile = new JButton("CHỌN FILE");

    public JPanel pnlFindPath = new JPanel(new GridBagLayout()); // tim kiem
    public JXTextField txtStart = new JXTextField("");
    public JXTextField txtEnd = new JXTextField("");
    public JButton btnFind = new JButton("TÌM KIẾM");

    public JButton btnExport = new JButton("XUẤT HÌNH"); // xuat hinh 

    public JButton btnClear = new JButton("XOÁ NODE"); // xoa node tra ve ban dau

    public MainDijkstra() {
        init();
    }

    private void init() {
        NodeGraph nodeGraph = new NodeGraph(path, edges);

//        pnlNodeGraph.setBackground(Color.red);
//        pnlRightSide.setBackground(Color.yellow);


        // -------------------------------------------------------------------------------------------
        pnlInputEdge.setBorder(BorderFactory.createTitledBorder("THÊM MỘT CẠNH"));
//        pnlInputEdge.setPreferredSize(new Dimension(Constant.addNodeFormWidth - 20, 120));
        pnlInputEdge.setPreferredSize(new Dimension(Constant.addNodeFormWidth - 20, 120));


        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

        // full width
        gridBagConstraints1.weightx = 1;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;

        // x: col
        // y: row
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        pnlInputEdge.add(new JLabel("Nút nguồn:"), gridBagConstraints1);
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        pnlInputEdge.add(txtSrc, gridBagConstraints1);
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        pnlInputEdge.add(new JLabel("Nút đích:"), gridBagConstraints1);
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        pnlInputEdge.add(txtDest, gridBagConstraints1);
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        pnlInputEdge.add(new JLabel("Trọng số:"), gridBagConstraints1);
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        pnlInputEdge.add(txtWeight, gridBagConstraints1);
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 2;
        pnlInputEdge.add(btnAdd, gridBagConstraints1);

        // -------------------------------------------------------------------------------------------
        pnlUploadFile.setBorder(BorderFactory.createTitledBorder("TÌM THEO FILE"));
        pnlUploadFile.setPreferredSize(new Dimension(Constant.addNodeFormWidth - 20, 100));

        lblFileName.setPreferredSize(new Dimension(150, 23));
        pnlUploadFile.add(lblFileName);
        pnlUploadFile.add(btnChooseFile);

        // -------------------------------------------------------------------------------------------
        pnlFindPath.setBorder(BorderFactory.createTitledBorder("TÌM ĐƯỜNG ĐI NGẮN NHẤT"));
        pnlFindPath.setPreferredSize(new Dimension(Constant.addNodeFormWidth - 20, 120));
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

        // full width
        gridBagConstraints2.weightx = 1;
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;

        // x: col
        // y: row
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        pnlFindPath.add(new JLabel("Nút bắt đầu:"), gridBagConstraints2);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        pnlFindPath.add(txtStart, gridBagConstraints2);
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        pnlFindPath.add(new JLabel("Nút kết thúc:"), gridBagConstraints2);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        pnlFindPath.add(txtEnd, gridBagConstraints2);
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 2;
        pnlFindPath.add(btnFind, gridBagConstraints2);
        pnlNodeGraph.setPreferredSize(new Dimension(Constant.nodeGraphWidth - 10, Constant.mainHeight));
        // -------------------------------------------------------------------------------------------
        pnlRightSide.setPreferredSize(new Dimension(Constant.addNodeFormWidth, Constant.mainHeight));
        pnlRightSide.add(pnlInputEdge);
        pnlRightSide.add(pnlUploadFile);
        pnlRightSide.add(pnlFindPath);
        pnlRightSide.add(btnExport);
        pnlRightSide.add(btnClear);

        // -------------------------------------------------------------------------------------------
        btnAdd.addActionListener((e) -> {
            btnAddEdge();
        });
        btnChooseFile.addActionListener((e) -> {
            btnChooseFile();
        });
        btnFind.addActionListener((e) -> {
            btnFindPath();
        });
        btnExport.addActionListener((e) -> {
            btnExportImage();
        });
        btnClear.addActionListener((e) -> {
            btnClear();
        });

        // -------------------------------------------------------------------------------------------
        pnlNodeGraph.add(nodeGraph);

        this.setLayout(new GridBagLayout());
        this.setSize(Constant.mainWidth, Constant.mainHeight);

        this.add(pnlNodeGraph, new GridBagConstraints());
        this.add(pnlRightSide, new GridBagConstraints());

    }

    private void makePanelImage(Component pnl, int width, int height, String pathname) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        pnl.paint(graphics2D);
        try {
            ImageIO.write(bufferedImage, "png", new File(pathname));
            JOptionPane.showMessageDialog(null, "Đã lưu!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lưu thất bại!");
            System.out.println(e);
        }
    }

    private NodeEdge validateEdgeValue(String src, String dest, String weight) {
        if (src.isBlank() || dest.isBlank() || weight.isBlank()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ!");
            return null;
        }

        if (src.equals(dest)) {
            JOptionPane.showMessageDialog(null, "Tên nút nguồn và đích phải khác nhau!");
            return null;
        }

        try {
            Integer.parseInt(weight);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số!");
            return null;
        }

        if (Integer.parseInt(weight) < 1) {
            JOptionPane.showMessageDialog(null, "Trọng số phải lớn hơn hoặc bằng 1");
            return null;
        }

        return new NodeEdge(src, dest, Integer.parseInt(weight));
    }

    private void btnAddEdge() {
        String src = txtSrc.getText();
        String dest = txtDest.getText();
        String weight = txtWeight.getText();

        NodeEdge edge = validateEdgeValue(src, dest, weight);

        if (edge != null) {
            ArrayList<NodeEdge> tmpEdges = (ArrayList<NodeEdge>) edges.clone();

            tmpEdges.add(edge);
            if (!validateGraph(tmpEdges)) {
                JOptionPane.showMessageDialog(null, "Đồ thị không hợp lệ hoặc có nút ra khỏi đồ thị");
            } else {
                this.edges.add(edge);

                //clear inputs
                txtSrc.setText("");
                txtDest.setText("");
                txtWeight.setText("");

                regenerateGraph();
            }
        }
    }

    private void btnChooseFile() {
        JFileChooser chooser = new JFileChooser(new File(Constant.testFilesPath));

        int r = chooser.showOpenDialog(null);

        if (r == JFileChooser.APPROVE_OPTION) {
            String uploadFilePath = chooser.getSelectedFile().getAbsolutePath();

            try {
                applyEdgesFromFile(uploadFilePath);

                // regenerate graph layout
                regenerateGraph();
            } catch (FileNotFoundException ex) {
                System.out.println("Tải file thất bại");
                System.out.println(ex);
            }

            lblFileName.setText(chooser.getSelectedFile().getName());
        } else {
            System.out.println("Huỷ thao tác.");
        }
    }

    public void applyEdgesFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        String line;
        ArrayList<NodeEdge> newEdges = new ArrayList<>();

        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name())) {
            while (sc.hasNextLine()) {
                line = sc.nextLine();

                // validate line
                String[] items = line.split("-");
                if (items.length != 3 || items[0].isBlank() || items[1].isBlank() || items[2].isBlank()) {
                    JOptionPane.showMessageDialog(null, "Sai cú pháp ('nguồn-đích-trọng số')");
                }

                String src = items[0];
                String dest = items[1];
                int weight = 0;
                try {
                    weight = Integer.parseInt(items[2]);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Sai cú pháp (trọng số phải là số)");
                    // reset graph
                    this.edges = new ArrayList<>();
                    this.path = emptyPath;
                    return;
                }

                newEdges.add(new NodeEdge(src, dest, weight));
            }
        }

        if (!validateGraph(newEdges)) {
            JOptionPane.showMessageDialog(null, "Đồ thị không hợp lệ hoặc có nút ra khỏi đồ thị");
            // reset graph
            this.edges = new ArrayList<>();
            this.path = emptyPath;
        } else {
            this.edges = newEdges;
        }
    }

    public boolean validateGraph(ArrayList<NodeEdge> data) {
        isValid = true;

        // Init graph
        GraphBuilder<String, Integer> g = GraphBuilder.<String, Integer>create();
        // Connect edge to graph
        for (int i = 0; i < data.size(); i++) {
            NodeEdge edge = data.get(i);
            g.connect(edge.getSource()).to(edge.getDestination()).withEdge(edge.getWeight());
        }

        HipsterGraph<String, Integer> graph = g.createUndirectedGraph();

        // Valid graph is each node has path from mainNode to destNode
        Iterable<String> vertices = graph.vertices();
        String mainNode = data.get(0).getSource(); // select any node from edges

        vertices.forEach((destNode) -> {
            // Create the search problem
            SearchProblem p = GraphSearchProblem
                    .startingFrom(mainNode)
                    .in(graph)
                    .takeCostsFromEdges()
                    .build();

            // Search the shortest path from "A" to "C"
            Algorithm.SearchResult result = Hipster.createDijkstra(p).search(destNode);
            List<String> optimalPath = (List<String>) result.getOptimalPaths().get(0);
            if (!optimalPath.contains(destNode)) {
                isValid = false;
            }
        });

        return isValid;
    }

    private void btnFindPath() {
        try {
            String startNode = txtStart.getText();
            String endNode = txtEnd.getText();

            if (startNode.isBlank() || endNode.isBlank()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ!");
                return;
            }

            // call get shortest path
            Client.socketSend("get-shortest-path-" + startNode + "-" + endNode);
            // then send the list current edges
            Client.socketSend(gson.toJson(this.edges));

            String result = Client.socketReadLine();

            if (result.contains("error")) {
                JOptionPane.showMessageDialog(null, result);
                this.path = this.emptyPath;
                regenerateGraph();
            } else {
                String[] shortestPath = gson.fromJson(result, String[].class);
                this.path = shortestPath;
                regenerateGraph();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy!");
            this.path = this.emptyPath;
            regenerateGraph();
            System.out.println(ex);
        }
    }

    private void btnExportImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("File lưu vào...");
        chooser.setSelectedFile(new File("savefile.png"));

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            makePanelImage(pnlNodeGraph, Constant.nodeGraphWidth, Constant.mainHeight, chooser.getSelectedFile().getPath());

        }
    }

    private void regenerateGraph() {
        pnlNodeGraph.removeAll();

        NodeGraph newNodeGraph = new NodeGraph(path, edges);
        pnlNodeGraph.add(newNodeGraph);

        this.validate();
    }

    private void btnClear() {
        pnlNodeGraph.removeAll();

        // empty path & edges
        path = emptyPath;
        edges = new ArrayList<>();

        NodeGraph newNodeGraph = new NodeGraph(path, edges);
        pnlNodeGraph.add(newNodeGraph);

        txtStart.setText("");
        txtEnd.setText("");
        
        this.validate();
    }
}
