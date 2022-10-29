/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import Client.Client;
import Client.LapLichCPU.Constant.Constant;
import Client.LapLichCPU.Entity.ResultAfterExecuteAlgorithm;
import static Client.LapLichCPU.Control.Process_TablePanelAction.renderGraph;
import static Client.LapLichCPU.Control.Process_TablePanelAction.renderDefaultGraph;
import com.google.gson.Gson;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author trankimphu0609
 */
public class EastPanel_ChooseTypeAlgorithmPanel extends JPanel {

    public static JLabel labelType;
    public static JComboBox<String> typeAlgorithmArr;

    public EastPanel_ChooseTypeAlgorithmPanel() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, 50));
        setBorder(BorderFactory.createTitledBorder("Choose algorithm type"));

        labelType = new JLabel("Algorithm Type: ");
        typeAlgorithmArr = new JComboBox<>();
        Constant.defaultTypeAlgorithm = "FCFS";

        typeAlgorithmArr.addItem("FCFS");
        typeAlgorithmArr.addItem("SJF"); // không độc quyền 1 tiến trình sẽ chạy hết
        typeAlgorithmArr.addItem("SRT"); // độc quyền của SJF. Tới giây của 1 tiến trình nào đó thì phải bị dừng lại
        typeAlgorithmArr.addItem("RR");
        typeAlgorithmArr.addItem("PP");  // độc quyền
        typeAlgorithmArr.addItem("PNP"); // không độc quyền

        // on algorithm type change
        typeAlgorithmArr.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent item) {
                // set global algorithm type
                Constant.defaultTypeAlgorithm = item.getItem().toString();

                // check empty
                if (!Constant.defaultTypeAlgorithm.isEmpty()) {
                    try {
                        // active priority textbox
                        if (Constant.defaultTypeAlgorithm.equals("PP") || Constant.defaultTypeAlgorithm.equals("PNP")) {
                            EastPanel_AddProcessPanel.textFieldPriority.setEnabled(true);
                        } else {
                            EastPanel_AddProcessPanel.textFieldPriority.setEnabled(false);
                        }

                        // send type and list process to server
                        Client.socketSend("get-algorythm-" + Constant.defaultTypeAlgorithm);
                        Client.socketSend(new Gson().toJson(Constant.arrayListProcess));

                        // receive result from server
                        String stringResult = Client.socketReadLine();
                        // parse string to object
                        ResultAfterExecuteAlgorithm result = new Gson().fromJson(stringResult, ResultAfterExecuteAlgorithm.class);

                        renderGraph(result);
                    } catch (Exception ex) {
                        Logger.getLogger(EastPanel_ChooseTypeAlgorithmPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    renderDefaultGraph();
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(labelType, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(typeAlgorithmArr, gbc);
    }

}
