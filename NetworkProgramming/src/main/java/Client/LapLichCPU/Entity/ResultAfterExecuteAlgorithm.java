/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.Entity;

import java.util.ArrayList;

/**
 * @author trankimphu0609
 */
public class ResultAfterExecuteAlgorithm {

    private ArrayList<Row> rows;
    private ArrayList<Event> timeline;
    private int timeQuantum;

    public ResultAfterExecuteAlgorithm(ArrayList<Row> rows, ArrayList<Event> timeline, int timeQuantum) {
        this.rows = rows;
        this.timeline = timeline;
        this.timeQuantum = timeQuantum;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public ArrayList<Event> getTimeline() {
        return timeline;
    }

    public void setTimeline(ArrayList<Event> timeline) {
        this.timeline = timeline;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

}
