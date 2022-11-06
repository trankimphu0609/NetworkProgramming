/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Trần Kim Phú
 */
public class header extends JPanel {

    private int height, width;
    private JFrame frame;

    public header(int w, int h) {
        width = w;
        height = h;
        init();
    }

    public void init() {
        setLayout(null);
        setSize(width, height);
        setBackground(null);

        Font font = new Font("Segoe UI", Font.BOLD, 15);
        JLabel name = new JLabel("ĐỀ 12", JLabel.CENTER);
        name.setFont(font);
        name.setForeground(Color.white);
        name.setBounds(new Rectangle(60, 0, 150, 40));

        add(name);

    }
}
