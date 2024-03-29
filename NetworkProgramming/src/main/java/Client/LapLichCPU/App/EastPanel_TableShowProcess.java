/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import UI.Client;

import Client.LapLichCPU.Constant.Constant;
import Client.LapLichCPU.Entity.ResultAfterExecuteAlgorithm;

import static Client.LapLichCPU.Control.Process_TablePanelAction.renderGraph;
import static Client.LapLichCPU.Control.Process_TablePanelAction.updateTable;

import Client.LapLichCPU.Entity.Row;
import com.google.gson.Gson;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author trankimphu0609
 */
public class EastPanel_TableShowProcess extends JPanel {

    public static JScrollPane jScrollPane;
    public static JTable tblProcess;
    public static DefaultTableModel defaultTableModel;

    public EastPanel_TableShowProcess() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, 250));
        setBorder(BorderFactory.createTitledBorder(Constant.PROCESS_TABLE_NAME));
        setLayout(new BorderLayout());

        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Process");
        defaultTableModel.addColumn("Time start");
        defaultTableModel.addColumn("Time");
        defaultTableModel.addColumn("Priority");

        // refer to constant file
        Constant.defaultTableModel = defaultTableModel;

        tblProcess = new JTable(defaultTableModel);
        jScrollPane = new JScrollPane(tblProcess);
        jScrollPane.setSize(Constant.WIDTH_EAST_PANEL, Constant.HEIGHT_PANEL / 2);
        tblProcess.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                System.out.println("User is choosing at row " + tblProcess.getSelectedRow());
            }
        });
        int condition = JTable.WHEN_FOCUSED;
        InputMap inputMap = tblProcess.getInputMap(condition);
        ActionMap actionMap = tblProcess.getActionMap();

        // khi nhấn nút delete thì xóa row đó
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        actionMap.put("deleteRow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int row = tblProcess.getSelectedRow();
                if (row >= 0) {
                    DefaultTableModel model = (DefaultTableModel) tblProcess.getModel();
                    model.removeRow(row);
                    Constant.arrayListProcess.remove(row);
                    try {
                        Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                        Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                        ResultAfterExecuteAlgorithm result = new Gson().fromJson(Client.socketReadLine(), ResultAfterExecuteAlgorithm.class);
                        // Việc Update Table dưới client
                        updateTable();
                        // Update Graph, việc vẽ grap sẽ do server trả kết quả về
                        renderGraph(result);
                    } catch (Exception ex) {
                        Logger.getLogger(EastPanel_TableShowProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        add(jScrollPane, BorderLayout.CENTER);
        JButton saveButton = new JButton("LƯU BẢNG");
        add(saveButton, BorderLayout.SOUTH);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int totalRow = tblProcess.getModel().getRowCount();
                ArrayList<Row> temp = new ArrayList<>();
                boolean isShouldUpdate = true;
                for (int i = 0; i < totalRow; i++) {
                    String processName = tblProcess.getModel().getValueAt(i, 0).toString();
                    int processTimeStart = 0;
                    int processBurstTime = 0;
                    int processPriority = 0;
                    try {
                        // kiểm tra là số
                        processTimeStart = Integer.parseInt(tblProcess.getModel().getValueAt(i, 1).toString());
                        processBurstTime = Integer.parseInt(tblProcess.getModel().getValueAt(i, 2).toString());
                        processPriority = Integer.parseInt(tblProcess.getModel().getValueAt(i, 3).toString());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Tiến trình tại hàng " + i + " có giá trị không là số", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        isShouldUpdate = false;
                        break;
                    }

                    int flag = -1;
                    for (int j = 0; j < temp.size(); j++) {
                        if (temp.get(j).getProcessName().equals(processName)) {
                            flag = j;
                        }
                    }

                    // kiểm tra trùng tên process
                    if (flag != -1) {
                        JOptionPane.showMessageDialog(null, "Hàng " + (i + 1) + " trùng tên với hàng " + (flag + 1), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        isShouldUpdate = false;
                        break;
                    }
                    temp.add(new Row(processName, processTimeStart, processBurstTime, processPriority));
                }
                if (isShouldUpdate == true) {
                    try {
                        Constant.arrayListProcess = temp;
                        Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                        Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                        ResultAfterExecuteAlgorithm result = new Gson().fromJson(Client.socketReadLine(), ResultAfterExecuteAlgorithm.class);
                        // Việc Update Table dưới client
                        updateTable();
                        // Update Graph, việc vẽ grap sẽ do server trả kết quả về
                        renderGraph(result);
                        JOptionPane.showMessageDialog(null, "Cập nhật thành công", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        Logger.getLogger(EastPanel_TableShowProcess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
