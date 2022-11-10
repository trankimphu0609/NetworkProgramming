/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import Client.LapLichCPU.Entity.Row;
import Client.LapLichCPU.Constant.Constant;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * @author trankimphu0609
 */
public class MainPanel extends JPanel {

    public static CenterPanel centerPanel;
    public static EastPanel eastPanel;
    public static ArrayList<Row> arrayListProcess;

    public MainPanel() {
        setLayout(new BorderLayout());
        setSize(Constant.WIDTH_PANEL, Constant.HEIGHT_PANEL);

        centerPanel = new CenterPanel();
        eastPanel = new EastPanel();
        arrayListProcess = new ArrayList<>();

        // refer to FSFS constaint
        Constant.arrayListProcess = arrayListProcess;
        Constant.centerPanel = centerPanel;
        Constant.eastPanel = eastPanel;

        addComponent();
    }

    private void addComponent() {
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
    }
}
