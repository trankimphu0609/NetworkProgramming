/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.Constant;

import Client.LapLichCPU.Entity.Row;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.gantt.GanttCategoryDataset;

/**
 * @author trankimphu0609
 */
public class Constant {

    public static String prefixNameProcess = "";
    public static String defaultTypeAlgorithm = "FCFS";

    // For main panel contains all child components
    public static int WIDTH_PANEL = 1000;
    public static int HEIGHT_PANEL = 600;
    public static ArrayList<Row> arrayListProcess = null;
    public static JPanel centerPanel = null;
    public static JPanel eastPanel = null;
    public static int startNumberProcess = 0;
    public static String defaultStartProcessName = "P0";

    // Main panel
    public static int WIDTH_CENTER_PANEL = 700;

    // Center panel
    public static ChartPanel panel = null;
    public static JTabbedPane jTabbedPane = null;
    public static GanttCategoryDataset dataset = null;
    public static JFreeChart chart = null;

    // East panel
    public static int WIDTH_EAST_PANEL = WIDTH_PANEL - WIDTH_CENTER_PANEL;

    // Border
    public static Color COLOR_BORDER = Color.darkGray;

    // ProcessTablePanel
    public static String PROCESS_TABLE_NAME = "BẢNG TIẾN TRÌNH";
    public static DefaultTableModel defaultTableModel = null;

    // AddProcessPanel
    public static String ADDING_PANEL_NAME = "THÊM TIẾN TRÌNH";
    public static JTextField textFieldProcessName = null;
    public static JTextField textFieldProcessTime = null;
    public static JTextField textFieldProcessTimeStart = null;
    public static String testFilesPath = System.getProperty("user.dir") + "/src/main/java/test";

}
