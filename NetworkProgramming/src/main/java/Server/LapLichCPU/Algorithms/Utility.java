/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.LapLichCPU.Algorithms;

import Server.LapLichCPU.Entity.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * @author trankimphu0609
 */
public class Utility {

    public static List<Row> deepCopy(List<Row> oldList) {
        List<Row> newList = new ArrayList();

        for (Row row : oldList) {
            newList.add(new Row(row.getProcessName(), row.getArrivalTime(), row.getBurstTime(), row.getPriorityLevel()));
        }

        return newList;
    }

}
