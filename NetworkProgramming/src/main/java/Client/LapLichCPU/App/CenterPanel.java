/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import Client.LapLichCPU.Constant.Constant;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author trankimphu0609
 */
public class CenterPanel extends JPanel {
    
    public static JPanel mainPanel;
    public static ChartPanel panel;
    public static JTabbedPane jTabbedPane;
    public static JFreeChart chart;

    CenterPanel() {
        // Init Center Panel
        setPreferredSize(new Dimension(Constant.WIDTH_CENTER_PANEL, Constant.HIGHT_PANEL));
    }
}
