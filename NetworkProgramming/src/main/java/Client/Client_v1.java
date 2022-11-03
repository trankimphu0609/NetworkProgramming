/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Client;

import Client.Dijkstra.Constant;
import Client.Dijkstra.MainDijkstra;
import Client.LapLichCPU.App.MainPanel;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author trankimphu0609
 */
public class Client_v1 extends javax.swing.JFrame {

    public static Socket socket = null;
    public static DataOutputStream out;
    public static DataInputStream in;
    BufferedReader stdIn = null;
    private final int keySize = 2048;
    public static int port = 1234;

    public static PublicKey pubServer; // server key

    public static PublicKey pubClient; // client key
    public static PrivateKey priClient; // client key

    /**
     * Creates new form Client_v1
     */
    public Client_v1(String address, int port) throws Exception {
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

        initComponents();
        init();
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
    
    private void init() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        MainDijkstra dijkstraPanel = new MainDijkstra();
        MainPanel fCFSPanel = new MainPanel();

        tabs.add(dijkstraPanel);
        tabs.setBackground(Color.GRAY);
        tabs.setTitleAt(0, "Dijkstra");
        tabs.add(fCFSPanel);
        tabs.setTitleAt(1, "CPU Schedule");

        add(tabs);

        setSize(Constant.mainWidth, Constant.mainHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tabs = new javax.swing.JTabbedPane();
        pnDijkstra = new javax.swing.JPanel();
        pnCPUScheduler = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CLIENT");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnDijkstra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnDijkstraMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnDijkstraLayout = new javax.swing.GroupLayout(pnDijkstra);
        pnDijkstra.setLayout(pnDijkstraLayout);
        pnDijkstraLayout.setHorizontalGroup(
            pnDijkstraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 873, Short.MAX_VALUE)
        );
        pnDijkstraLayout.setVerticalGroup(
            pnDijkstraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        tabs.addTab("DIJKSTRA", pnDijkstra);

        javax.swing.GroupLayout pnCPUSchedulerLayout = new javax.swing.GroupLayout(pnCPUScheduler);
        pnCPUScheduler.setLayout(pnCPUSchedulerLayout);
        pnCPUSchedulerLayout.setHorizontalGroup(
            pnCPUSchedulerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 873, Short.MAX_VALUE)
        );
        pnCPUSchedulerLayout.setVerticalGroup(
            pnCPUSchedulerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        tabs.addTab("CPU SCHEDULER", pnCPUScheduler);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnDijkstraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnDijkstraMouseClicked
        // TODO add your handling code here:
        MainDijkstra dijkstra = new MainDijkstra();
        pnDijkstra.add(dijkstra);
    }//GEN-LAST:event_pnDijkstraMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client_v1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Choose_Server form = new Choose_Server();
                form.setLocationRelativeTo(null);
                form.pack();
                form.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel pnCPUScheduler;
    private javax.swing.JPanel pnDijkstra;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
