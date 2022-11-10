/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.LapLichCPU.Algorithms;

import Server.LapLichCPU.Entity.Event;
import Server.LapLichCPU.Entity.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author trankimphu0609
 */
public class PNP extends CPUScheduler {

    @Override
    public void process() {
        Collections.sort(this.getRows(), (Object o1, Object o2) -> {
            if (((Row) o1).getArrivalTime() == ((Row) o2).getArrivalTime()) {
                return 0;
            } else if (((Row) o1).getArrivalTime() < ((Row) o2).getArrivalTime()) {
                return -1;
            } else {
                return 1;
            }
        });

        List<Row> rows = Utility.deepCopy(this.getRows());
        int time = rows.get(0).getArrivalTime();

        while (!rows.isEmpty()) {
            List<Row> availableRows = new ArrayList();

            for (Row row : rows) {
                if (row.getArrivalTime() <= time) {
                    availableRows.add(row);
                }
            }

            Collections.sort(availableRows, (Object o1, Object o2) -> {
                if (((Row) o1).getPriorityLevel() == ((Row) o2).getPriorityLevel()) {
                    return 0;
                } else if (((Row) o1).getPriorityLevel() < ((Row) o2).getPriorityLevel()) {
                    return -1;
                } else {
                    return 1;
                }
            });

            Row row = availableRows.get(0);
            this.getTimeline().add(new Event(row.getProcessName(), time, time + row.getBurstTime()));
            time += row.getBurstTime();

            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).getProcessName().equals(row.getProcessName())) {
                    rows.remove(i);
                    break;
                }
            }
        }

        for (Row row : this.getRows()) {
            row.setWaitingTime(this.getEvent(row).getStartTime() - row.getArrivalTime());
            row.setTurnaroundTime(row.getWaitingTime() + row.getBurstTime());
        }
    }
}
