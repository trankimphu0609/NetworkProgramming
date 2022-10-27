/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.Entity;

import java.util.ArrayList;

/**
 *
 * @author trankimphu0609
 */
public class ResultAfterExecuteAlgorithm {

    private ArrayList<Row> rows;
    private ArrayList<Event> timeLine;
    private int timeQuantum;

    public ResultAfterExecuteAlgorithm(ArrayList<Row> rows, ArrayList<Event> timeLine, int timeQuantum) {
        this.rows = rows;
        this.timeLine = timeLine;
        this.timeQuantum = timeQuantum;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public ArrayList<Event> getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(ArrayList<Event> timeLine) {
        this.timeLine = timeLine;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

}
