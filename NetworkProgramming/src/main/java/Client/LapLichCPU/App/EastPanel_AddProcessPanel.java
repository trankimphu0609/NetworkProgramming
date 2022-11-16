/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import UI.Client;

import Client.LapLichCPU.Constant.Constant;
import Client.LapLichCPU.Control.Process_TablePanelAction;

import static Client.LapLichCPU.Control.Process_TablePanelAction.renderGraph;
import static Client.LapLichCPU.Control.Process_TablePanelAction.updateTable;

import Client.LapLichCPU.Entity.ResultAfterExecuteAlgorithm;
import Client.LapLichCPU.Entity.Row;
import com.google.gson.Gson;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author trankimphu0609
 */
public class EastPanel_AddProcessPanel extends JPanel {

    public static JLabel lblTitleOption, lblProcessName, lblProcessTime, lblProcessTimeStart, lblPriority;

    public static JTextField txtPriority, txtProcessName, txtProcessTime, txtProcessTimeStart;

    public static JButton btnAddProcess, btnChooseFile;

    EastPanel_AddProcessPanel() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, 250));
        setBorder(BorderFactory.createTitledBorder(Constant.ADDING_PANEL_NAME));

        lblTitleOption = new JLabel("Nhập từ file:");
        btnChooseFile = new JButton("CHỌN FILE");
        // Chọn file
        btnChooseFile.addActionListener((var arg0) -> {
            handleClickChooseFile();
        });
        lblProcessName = new JLabel("Tên tiến trình");
        lblProcessTime = new JLabel("Thời gian (ms)");
        lblProcessTimeStart = new JLabel("Thời gian bắt đầu (ms)");
        lblPriority = new JLabel("Độ ưu tiên");

        txtPriority = new JTextField(200);
        txtPriority.setEnabled(false);
        txtProcessName = new JTextField(200);

//        textFieldProcessName.setText(Constant.prefixNameProcess + String.valueOf(Constant.startNumberProcess)); // For Process auto increament
//        textFieldProcessName.setEditable(false);
        txtProcessName.setText(Constant.defaultStartProcessName);
        txtProcessName.setToolTipText("Vui lòng nhập tên tiến trình");

        lblProcessTime.setToolTipText("Vui lòng nhập thời gian ");
        lblProcessTimeStart.setToolTipText("Vui lòng nhập thời gian bắt đầu");

        txtProcessTime = new JTextField(200);
        txtProcessTimeStart = new JTextField(200);
        btnAddProcess = new JButton("THÊM TIẾN TRÌNH");
        btnAddProcess.addActionListener((ActionEvent arg0) -> {
            String processName = Constant.textFieldProcessName.getText();
            String processTime = Constant.textFieldProcessTime.getText();
            String processTimeStart = Constant.textFieldProcessTimeStart.getText();
            String processPriority = txtPriority.getText();

            if (processName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập tên tiến trình!");
            } else if (processTime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập thời gian");
            } else if (processTime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập thời gian bắt đầu");

            } else {
                Row row = null;
                boolean flag = true;
                try {
                    if (Constant.defaultTypeAlgorithm.equals("PP") || Constant.defaultTypeAlgorithm.equals("PNP")) {
                        if (Integer.parseInt(processTimeStart) < 0
                                || Integer.parseInt(processTime) < 0
                                || Integer.parseInt(processPriority) < 0) {
                            JOptionPane.showMessageDialog(null, "Phải >= 0");
                            return;
                        }
                        row = new Row(processName,
                                Integer.parseInt(processTimeStart),
                                Integer.parseInt(processTime),
                                Integer.parseInt(processPriority)
                        );
                    } else {
                        if (Integer.parseInt(processTimeStart) < 0
                                || Integer.parseInt(processTime) < 0) {
                            JOptionPane.showMessageDialog(null, "Phải >= 0");
                            return;
                        }
                        row = new Row(processName,
                                Integer.parseInt(processTimeStart),
                                Integer.parseInt(processTime)
                        );
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Tiến trình có giá trị không phải là số", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    flag = false;
                }

                if (flag) { // nếu mà không bị throw NumberFormatException
                    // check exists process name in list
                    int index = -1;
                    for (int i = 0; i < Constant.arrayListProcess.size(); i++) {
                        if (row.getProcessName().equals(Constant.arrayListProcess.get(i).getProcessName())) {
                            index = i;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (index != -1) { // nếu bị trùng
                        int option = JOptionPane.showConfirmDialog(null, "Tiến trình bị trùng. Hãy kiểm tra lại!", "Warning", JOptionPane.YES_NO_OPTION);
                        // mún override cái bị trùng hay không?
                        // Ok 0
                        // No 1
                        // Close -1

                        System.out.println(option);

                        if (option == -1) {
                            // keep it and do nothing
                        } else if (option == 0) {
                            // đồng ý override bị trùng
                            Constant.arrayListProcess.set(index, row);

                            // Constant.textFieldProcessName.setText("");
                            // Constant.textFieldProcessTime.setText("");
                            // Constant.textFieldProcessTimeStart.setText("");
                            // call server and get result
                            try {
                                Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                                Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                                ResultAfterExecuteAlgorithm result = new Gson().fromJson(Client.socketReadLine(), ResultAfterExecuteAlgorithm.class);
                                // Việc Update Table dưới client
                                updateTable();
                                // Update Graph, việc vẽ grap sẽ do server trả kết quả về
                                renderGraph(result);
                            } catch (Exception ex) {
                                Logger.getLogger(Process_TablePanelAction.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (option == 1) {
                            // keep it and do nothing
                        }
                    } else { // không bị trùng tên process
                        Constant.arrayListProcess.add(row);
                        // thêm xong làm trống input
                        // Constant.textFieldProcessName.setText("");
                        // Constant.textFieldProcessTime.setText("");
                        // Constant.textFieldProcessTimeStart.setText("");

                        // call server and get result
                        try {
                            Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                            Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                            String s = Client.socketReadLine();
                            ResultAfterExecuteAlgorithm result = new Gson().fromJson(s, ResultAfterExecuteAlgorithm.class);
                            // Việc Update Table Sẽ được server xử kết quả sau đó render ra
                            // Option 1 to update
                            // Việc Update Table dưới client
                            // Option 2 to update
                            // Constant.defaultTableModel.addRow(new Object[]{processName, processTime, processTimeStart});
                            // Constant.defaultTableModel.fireTableDataChanged();
                            // Update Graph, việc vẽ grap sẽ do server trả kết quả về
                            updateTable();
                            renderGraph(result);
                        } catch (Exception ex) {
                            Logger.getLogger(Process_TablePanelAction.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        // refer to constant file
        Constant.textFieldProcessName = txtProcessName;
        Constant.textFieldProcessTime = txtProcessTime;
        Constant.textFieldProcessTimeStart = txtProcessTimeStart;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(lblTitleOption, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(btnChooseFile, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(lblProcessName, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(txtProcessName, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(lblProcessTimeStart, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(txtProcessTimeStart, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(lblProcessTime, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(txtProcessTime, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(lblPriority, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(txtPriority, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(btnAddProcess, gbc);
    }

    // cái này là upload file lên
    private int handleClickChooseFile() {
        JFileChooser fileChooser = new JFileChooser(new File(Constant.testFilesPath));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            ArrayList<Row> arr = new ArrayList<>();
            try {
                Scanner scanner = new Scanner(file);

                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    Pattern p = Pattern.compile("^\\w+ \\d \\d \\d$");
                    Matcher matcher = p.matcher(line);

                    if (matcher.find()) {
                        String[] values = line.split(" ");

                        String processName = values[0];
                        int arrivalTime = 0;
                        int burstTime = 0;
                        int priority = 0;

                        try {
                            arrivalTime = Integer.parseInt(values[1]);
                            burstTime = Integer.parseInt(values[2]);
                            priority = Integer.parseInt(values[3]);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Tiến trình có giá trị không phải là số", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return -1; // thoát ra và không cần xử lý
                        }

                        arr.add(new Row(processName, arrivalTime, burstTime, priority));
                    } else {
                        JOptionPane.showMessageDialog(null, "Nội dung file sai!", "Error File", JOptionPane.ERROR_MESSAGE);
                        return -1;
                    }
                }

                // validate file
                // duplicated process
                for (int i = 0; i < arr.size(); i++) {
                    int found = -1;
                    for (int j = 0; j < Constant.arrayListProcess.size(); j++) {
                        if (Constant.arrayListProcess.get(j).getProcessName().equals(arr.get(i).getProcessName())) {
                            found = j;
                            break;
                        } else {
                            continue;
                        }
                    }

                    // nếu mà đã được add rồi thì update lại giá trị của nó
                    if (found == -1) {
                        // Add new
                        Constant.arrayListProcess.add(arr.get(i));
                    } else {
                        // Update that value
                        Constant.arrayListProcess.set(found, arr.get(i));
                    }
                }

                // Update graph
                // Gửi yêu cầu cho server
                Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                Client.socketSend(new Gson().toJson(Constant.arrayListProcess));
                // Nhận kết quả của server
                ResultAfterExecuteAlgorithm result = new Gson().fromJson(Client.socketReadLine(), ResultAfterExecuteAlgorithm.class);
                // Render ra kết quả
                renderGraph(result);
                // Update table
                Process_TablePanelAction.updateTable();
            } catch (Exception ex) {
                Logger.getLogger(EastPanel_AddProcessPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 1;
        } else {
            System.out.println("Canceled by user");
            return -1;
        }
    }
}
