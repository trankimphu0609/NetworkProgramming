/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.Entity;

/**
 * @author trankimphu0609
 */
public class ProcessResult {

    // class ProcessResult => bỏ render ra chart
    private String processName;
    private int processStart;
    private int processEnd;

    public ProcessResult(String processName, int processStart, int processEnd) {
        this.processName = processName;
        this.processStart = processStart;
        this.processEnd = processEnd;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProcessStart() {
        return processStart;
    }

    public void setProcessStart(int processStart) {
        this.processStart = processStart;
    }

    public int getProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(int processEnd) {
        this.processEnd = processEnd;
    }

}
