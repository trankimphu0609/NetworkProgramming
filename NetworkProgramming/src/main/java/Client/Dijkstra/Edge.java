/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.Dijkstra;

/**
 *
 * @author trankimphu0609
 */
public class Edge {

    private String src;
    private String destination;
    private int weight;

    public Edge(String src, String destination, int weight) {
        this.src = src;
        this.destination = destination;
        this.weight = weight;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String dest) {
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
