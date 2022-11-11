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
 * @author trankimphu0609
 */
public class EastPanel extends JPanel {

    public static EastPanel_AddProcessPanel pnlAddProcess;
    public static EastPanel_TableShowProcess pnlProcessTable;
    public static EastPanel_ChooseTypeAlgorithmPanel pnlChooseTypeAlgorithm;

    EastPanel() {
        setPreferredSize(new Dimension(Constant.WIDTH_EAST_PANEL, Constant.HEIGHT_PANEL));
        setLayout(new BorderLayout());

        pnlAddProcess = new EastPanel_AddProcessPanel();
        pnlProcessTable = new EastPanel_TableShowProcess();
        pnlChooseTypeAlgorithm = new EastPanel_ChooseTypeAlgorithmPanel();

        add(pnlAddProcess, BorderLayout.SOUTH);
        add(pnlProcessTable, BorderLayout.CENTER);
        add(pnlChooseTypeAlgorithm, BorderLayout.NORTH);

    }

}
