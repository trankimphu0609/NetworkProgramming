/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Client.Connect;
import Client.Dijkstra.MainDijkstra;
import Client.LapLichCPU.App.MainPanel;
import UI.model.header;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.model.navItem;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @author Trần Kim Phú
 */
public class Client extends JFrame implements MouseListener {
    
    private boolean flag = true;
    private JPanel header, nav, main;
    private int DEFAULT_HEIGHT = 650, DEFALUT_WIDTH = 1240;
    private ArrayList<String> navItem = new ArrayList<>();  // chứa thông tin có button cho menu
    private ArrayList<navItem> navObj = new ArrayList<>();  // chứa button trên thanh menu

    public static Socket socket = null;
    public static DataOutputStream out;
    public static DataInputStream in;
    BufferedReader stdIn = null;
    private final int keySize = 2048;
    public static int port = 4334;
    
    public static PublicKey pubServer; // server key

    public static PublicKey pubClient; // client key
    public static PrivateKey priClient; // client key

    public Client(String address, int port) throws Exception {
        socket = new Socket(address, port);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); // General public and private keys
        keyPairGenerator.initialize(keySize, new SecureRandom());
        KeyPair kp = keyPairGenerator.genKeyPair();
        pubClient = kp.getPublic();
        priClient = kp.getPrivate();
        
        out.write(pubClient.getEncoded()); // gửi khoá public từ Client tới Server
        out.flush();
        
        byte[] publicKeyBytes = new byte[keySize]; // nhận public key từ Server
        in.read(publicKeyBytes, 0, keySize);
        X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        
        pubServer = kf.generatePublic(ks); // lưu lại

        view();
    }
    
    public static String socketReadLine() throws Exception {
        String result = "";
        
        int blockCount = in.readInt(); // lấy tổng số block cần phải nhận

        for (int i = 0; i < blockCount; i++) { // lấy tất cả các block
            String line = in.readUTF();
            
            Cipher c = Cipher.getInstance("RSA"); // dịch block
            c.init(Cipher.DECRYPT_MODE, priClient);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(line));
            
            result += new String(decryptOut);
        }
        
        System.out.println("socketReadline:" + result);
        return result;
    }
    
    public static void socketSend(String line) throws Exception {
        
        Cipher c = Cipher.getInstance("RSA"); // để mã hoá chuỗi gửi cho Server
        c.init(Cipher.ENCRYPT_MODE, pubServer); // dùng pubServer

        final RSAPublicKey rsapub = (RSAPublicKey) pubClient; // lấy độ dài bit của pub
        int keyBitLength = rsapub.getModulus().bitLength();
        
        byte[] lineBytes = line.getBytes();
        
        int validSize = keyBitLength / 8 - 11; // chuẩn độ dài mã hoá RSA hợp lệ

        int blockCount = (int) Math.ceil((float) lineBytes.length / validSize);
        int remaining = line.getBytes().length; // chia thành từng block theo độ dài hợp lệ

        out.writeInt(blockCount); // trả về tổng số block cho Server
        out.flush();
        
        ByteBuffer bb = ByteBuffer.wrap(lineBytes); // trả từng block theo độ dài hợp lệ
        for (int i = 0; i < blockCount; i++) {
            
            int minLength = Math.min(validSize, remaining); // lấy độ dài nhỏ nhất
            remaining -= minLength;
            
            byte[] block = new byte[minLength]; // lấy giá trị cho block
            bb.get(block, 0, minLength);
            
            byte encryptOut[] = c.doFinal(block); // mã hoá và trả về
            String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
            
            out.writeUTF(strEncrypt);
            out.flush();
        }
    }
    
    public Client() throws FileNotFoundException, Exception {
        Toolkit screen = Toolkit.getDefaultToolkit();
        view();
        System.out.println("heheh");
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Connect form = new Connect();
                    form.setLocationRelativeTo(null);
                    form.pack();
                    form.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void view() throws FileNotFoundException {
        Font font = new Font("Time new", Font.BOLD, 14);
        setTitle("DIJKSTRA - CPU SCHEDULER");
        ImageIcon logo = new ImageIcon("./src/main/java/img/icons8-course-64.png");
        setIconImage(logo.getImage());
        setLayout(new BorderLayout());
        setSize(DEFALUT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        // header
        header = new JPanel(null);
        header.setBackground(new Color(25, 25, 34));
        header.setPreferredSize(new Dimension(DEFALUT_WIDTH, 40));
        
        header hmain = new header(DEFALUT_WIDTH, 40);

        //Tạo btn EXIT & MINIMIZE
        navItem exit = new navItem("", new Rectangle(DEFALUT_WIDTH - 50, -8, 50, 50), "exit_25px.png", "exit_25px.png", "exit_hover_25px.png", new Color(240, 71, 74));
        navItem mini = new navItem("", new Rectangle(DEFALUT_WIDTH - 100, -8, 50, 50), "minimize_25px.png", "minimize_25px.png", "minimize_hover_25px.png", new Color(80, 80, 80));
        
        hmain.add(exit.isButton());
        hmain.add(mini.isButton());
        
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        
        mini.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                setState(Frame.ICONIFIED);
            }
        });
        
        header.add(hmain);

        // nav
        nav = new JPanel(null);
        nav.setBackground(new Color(55, 63, 81));
        nav.setPreferredSize(new Dimension(220, DEFAULT_HEIGHT));
        
        navItem = new ArrayList<>();  //Chứa thông tin có button cho menu gồm ( Tên btn : icon : icon hover )
        navItem.add("TÌM ĐƯỜNG:icons8-combo-chart-30.png:icons8-combo-chart-30.png");
        navItem.add("LẬP LỊCH:icons8-planner-30.png:icons8-planner-30.png");
        
        outNav();

        // phần main (hiển thị)
        main = new JPanel(null);
        navObj.get(0).doActive();
        changeMainInfo(0);
        
        add(header, BorderLayout.NORTH);
        add(nav, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    public void outNav() {
        navObj.clear();
        for (int i = 0; i < navItem.size(); i++) {
            String s = navItem.get(i).split(":")[0];
            String icon = navItem.get(i).split(":")[1];
            String iconActive = navItem.get(i).split(":")[2];
            navObj.add(new navItem(s, new Rectangle(0, 200 + 50 * i, 220, 50), icon, iconActive));
            navObj.get(i).addMouseListener(this);
        }
        if (!flag && navObj.size() > 8) {
            navObj.get(4).setColorNormal(new Color(86, 94, 127));
            navObj.get(5).setColorNormal(new Color(86, 94, 127));
        }

        // xuất ra Naigation
        nav.removeAll();
        JLabel profile = new JLabel(new ImageIcon("./src/main/java/img/business-icon.png"));
        profile.setBounds(0, 0, 210, 210);
        nav.add(profile);
        for (navItem n : navObj) {
            nav.add(n);
        }
        repaint();
        revalidate();
    }
    
    public void changeMainInfo(int i) throws FileNotFoundException {
        // đổi phần hiển thị khi bấm btn trên menu
        switch (i) {
            case 0: //  DIJKSTRA
                main.removeAll();
                main.add(new MainDijkstra());
                main.repaint();
                main.revalidate();
                break;
            case 1: // CPU SCHEDULER
                main.removeAll();
                main.add(new MainPanel());
                main.repaint();
                main.revalidate();
                break;
            
        }
    }
    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        for (int i = 0; i < navObj.size(); i++) {
            navItem item = navObj.get(i); // lấy vị trí item trong menu
            if (e.getSource() == item) {
                item.doActive(); // Active NavItem đc chọn
                try {
                    changeMainInfo(i); // Hiển thị ra phần main
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                item.noActive();
            }
        }
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
    }
    
    @Override
    public void mouseEntered(MouseEvent me) {
    }
    
    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}
