/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.LapLichCPU.App;

import Client.LapLichCPU.Constant.Constant;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author trankimphu0609
 */
public class EastPanel extends JPanel {
    public static EastPanel_AddProcessPanel addProcessPanel;
    public static EastPanel_TableShowProcess processTablePanel;
    public static EastPanel_ChooseTypeAlgorithmPanel chooseTypeAlgorithmPanel;

    EastPanel() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, Constant.HEIGHT_PANEL));
        setLayout(new BorderLayout());

        addProcessPanel = new EastPanel_AddProcessPanel();
        processTablePanel = new EastPanel_TableShowProcess();
        chooseTypeAlgorithmPanel = new EastPanel_ChooseTypeAlgorithmPanel();

        add(chooseTypeAlgorithmPanel, BorderLayout.NORTH);
        add(addProcessPanel, BorderLayout.CENTER);
        add(processTablePanel, BorderLayout.SOUTH);
    }
    
}
