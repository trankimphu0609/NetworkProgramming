package Server.LapLichCPU;

import Server.LapLichCPU.Algorithms.CPUScheduler;
import Server.LapLichCPU.Algorithms.FCFS;
import Server.LapLichCPU.Algorithms.PNP;
import Server.LapLichCPU.Algorithms.PP;
import Server.LapLichCPU.Algorithms.RR;
import Server.LapLichCPU.Algorithms.SJF;
import Server.LapLichCPU.Algorithms.SRT;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Server.LapLichCPU.Entity.Row;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author trankimphu0609
 */
public class ExecuteCPUAlgorithm {

    private ArrayList<Row> arrayProcess;
    private String algorithmName;

    public ExecuteCPUAlgorithm(String arrayProcessString, String algorithmName) throws IOException {
        applyArrayProcessFromJsonString(arrayProcessString);
        this.algorithmName = algorithmName;
    }

    public CPUScheduler execute() {
        // Implement
        CPUScheduler algorithm = null;
        if (algorithmName == null) {
            algorithmName = "FCFS";
        }
        if (algorithmName.equals("FCFS")) {
            algorithm = new FCFS();
        } else if (algorithmName.equals("SJF")) {
            algorithm = new SJF();
        } else if (algorithmName.equals("RR")) {
            algorithm = new RR();
        } else if (algorithmName.equals("PP")) {
            algorithm = new PP();
        } else if (algorithmName.equals("PNP")) {
            algorithm = new PNP();
        } else if (algorithmName.equals("SRT")) {
            algorithm = new SRT();
        } else {
            // default
            algorithm = new FCFS();
        }

        if (arrayProcess.size() > 0) {
            // add tất cả process trong list vào ham thuat toan
            for (int i = 0; i < arrayProcess.size(); i++) {
                algorithm.add(arrayProcess.get(i));
            }
            // chạy hàm xử lý thuật toán
            algorithm.process();
        }

        return algorithm;
    }

    public void applyArrayProcessFromJsonString(String string) {
        Gson gson = new Gson();
        List list = gson.fromJson(string, ArrayList.class
        );
        ArrayList<Row> arrayProcess = new ArrayList<>();

        for (Object objEdge : list) {
            Row edge = gson.fromJson(gson.toJson(objEdge), Row.class);
            arrayProcess.add(edge);
        }

        this.arrayProcess = arrayProcess;
    }

    public ArrayList<Row> getArrayProcess() {
        return arrayProcess;
    }

    public void setArrayProcess(ArrayList<Row> arrayProcess) {
        this.arrayProcess = arrayProcess;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

}
