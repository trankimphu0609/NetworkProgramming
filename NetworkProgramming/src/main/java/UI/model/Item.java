/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.model;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author Trần Kim Phú
 */
public class Item extends JPanel {

    private JLabel itemName;
    private JLabel itemImage;
    private int x, y;

    public Item(int x, int y, String itemName, String image) {
        this.x = x;
        this.y = y;
        this.itemName = new JLabel(itemName, JLabel.CENTER);
        this.itemImage = new JLabel("Image 200x200", JLabel.CENTER);
        init();
    }

    public void init() {
        JLabel edit = new JLabel("Edit", JLabel.CENTER);
        edit.setBounds(new Rectangle(0, 240, 100, 20));
        JLabel delete = new JLabel("Delete", JLabel.CENTER);
        delete.setBounds(new Rectangle(100, 240, 100, 20));
        JLabel moveUp = new JLabel("Move Up", JLabel.CENTER);
        moveUp.setBounds(new Rectangle(0, 270, 100, 20));
        JLabel moveDown = new JLabel("Mouve Down", JLabel.CENTER);
        moveDown.setBounds(new Rectangle(100, 270, 100, 20));

        setLayout(null);
        setBounds(new Rectangle(x, y, 200, 300));
        JSeparator sep1 = new JSeparator(1);
        sep1.setBounds(new Rectangle(100, 240, 1, 20));

        JSeparator sep2 = new JSeparator(1);
        sep2.setBounds(new Rectangle(100, 270, 1, 20));

        itemName.setBounds(new Rectangle(0, 2, 200, 30));

        JPanel pn = new JPanel();
        pn.setBackground(Color.GREEN);
        pn.setBounds(new Rectangle(0, 30, 200, 200));

        itemImage.setBounds(new Rectangle(0, 50, 200, 100));

        add(itemName);
        add(itemImage);
        add(pn);
        add(edit);
        add(delete);
        add(moveUp);
        add(moveDown);
        add(sep1);
        add(sep2);
    }
}
