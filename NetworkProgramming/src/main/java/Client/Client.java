///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Client;
//
//import Client.Dijkstra.Constant;
//import Client.Dijkstra.MainDijkstra;
//import Client.LapLichCPU.App.MainPanel;
//import java.awt.Color;
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import javax.crypto.Cipher;
//import javax.swing.JFrame;
//import javax.swing.JTabbedPane;
//import javax.swing.UIManager;
//
///**
// *
// * @author trankimphu0609
// */
//public class Client {
//
//    public static Socket socket = null;
//    public static DataOutputStream out;
//    public static DataInputStream in;
//    BufferedReader stdIn = null;
//    private final int keySize = 2048;
//    public static int port = 4323;
//
//    public static PublicKey pubServer; // server key
//
//    public static PublicKey pubClient; // client key
//    public static PrivateKey priClient; // client key
//
//    public Client(String address, int port) throws Exception {
//        socket = new Socket(address, port);
//        out = new DataOutputStream(socket.getOutputStream());
//        in = new DataInputStream(socket.getInputStream());
//
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); // General public and private keys
//        keyPairGenerator.initialize(keySize, new SecureRandom());
//        KeyPair kp = keyPairGenerator.genKeyPair();
//        pubClient = kp.getPublic();
//        priClient = kp.getPrivate();
//
//        out.write(pubClient.getEncoded()); // gửi khoá public từ Client tới Server
//        out.flush();
//
//        byte[] publicKeyBytes = new byte[keySize]; // nhận public key từ Server
//        in.read(publicKeyBytes, 0, keySize);
//        X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//
//        pubServer = kf.generatePublic(ks); // lưu lại
//
//        init();
//    }
//
//    public static String socketReadLine() throws Exception {
//        String result = "";
//
//        int blockCount = in.readInt(); // lấy tổng số block cần phải nhận
//
//        for (int i = 0; i < blockCount; i++) { // lấy tất cả các block
//            String line = in.readUTF();
//
//            Cipher c = Cipher.getInstance("RSA"); // dịch block
//            c.init(Cipher.DECRYPT_MODE, priClient);
//            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(line));
//
//            result += new String(decryptOut);
//        }
//
//        System.out.println("socketReadline:" + result);
//        return result;
//    }
//
//    public static void socketSend(String line) throws Exception {
//
//        Cipher c = Cipher.getInstance("RSA"); // để mã hoá chuỗi gửi cho Server
//        c.init(Cipher.ENCRYPT_MODE, pubServer); // dùng pubServer
//
//        final RSAPublicKey rsapub = (RSAPublicKey) pubClient; // lấy độ dài bit của pub
//        int keyBitLength = rsapub.getModulus().bitLength();
//
//        byte[] lineBytes = line.getBytes();
//
//        int validSize = keyBitLength / 8 - 11; // chuẩn độ dài mã hoá RSA hợp lệ
//
//        int blockCount = (int) Math.ceil((float) lineBytes.length / validSize);
//        int remaining = line.getBytes().length; // chia thành từng block theo độ dài hợp lệ
//
//        out.writeInt(blockCount); // trả về tổng số block cho Server
//        out.flush();
//
//        ByteBuffer bb = ByteBuffer.wrap(lineBytes); // trả từng block theo độ dài hợp lệ
//        for (int i = 0; i < blockCount; i++) {
//
//            int minLength = Math.min(validSize, remaining); // lấy độ dài nhỏ nhất
//            remaining -= minLength;
//
//            byte[] block = new byte[minLength]; // lấy giá trị cho block
//            bb.get(block, 0, minLength);
//
//            byte encryptOut[] = c.doFinal(block); // mã hoá và trả về
//            String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
//
//            out.writeUTF(strEncrypt);
//            out.flush();
//        }
//    }
//
//    private void init() throws Exception {
//
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//
//        MainDijkstra dijkstraPanel = new MainDijkstra();
//        MainPanel fCFSPanel = new MainPanel();
//
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.add(dijkstraPanel);
//        tabs.setTitleAt(0, "DIJKSTRA");
//        tabs.add(fCFSPanel);
//        tabs.setTitleAt(1, "CPU SCHEDULE");
//
//        JFrame frame = new JFrame();
//        frame.add(tabs);
//
//        frame.setBackground(Color.lightGray);
//        frame.setSize(Constant.mainWidth, Constant.mainHeight);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//        frame.setResizable(false);
//    }
//
//    public static void main(String args[]) throws Exception {
//
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//
//        Choose_Server form = new Choose_Server();
//
//        form.setLocationRelativeTo(null);
//        form.pack();
//        form.setVisible(true);
//    }
//}
