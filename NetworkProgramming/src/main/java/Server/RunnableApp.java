/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Server.Dijkstra.Dijkstra;
import Server.LapLichCPU.ExecuteCPUAlgorithm;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;

/**
 *
 * @author trankimphu0609
 */
public class RunnableApp implements Runnable {

    private final Socket socket;
    private final Gson gson = new Gson();
    boolean isValid = true;
    public static DataInputStream in;
    public static DataOutputStream out;
    private final int keySize = 2048;

    private static PublicKey pubServer; // server key

    private static PrivateKey priServer; // server key

    public static PublicKey pubClient; // client key

    public RunnableApp(Socket socket) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); 
//        general public and private keys of current client connected
        keyPairGenerator.initialize(keySize, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        pubServer = keyPair.getPublic();
        priServer = keyPair.getPrivate();

        out.write(pubServer.getEncoded()); // gửi khoá public của Server tới Client
        out.flush();

        byte[] pubBytes = new byte[keySize]; // nhận khoá public từ Client
        in.read(pubBytes, 0, keySize);

        X509EncodedKeySpec ks = new X509EncodedKeySpec(pubBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        pubClient = kf.generatePublic(ks); // lưu lại
    }

    public static String socketReadLine() throws Exception {

        String result = "";

        int blockCount = in.readInt(); // lấy tổng số block cần phải nhận

        for (int i = 0; i < blockCount; i++) { // lấy tất cả các block
            String line = in.readUTF();

            Cipher cipher = Cipher.getInstance("RSA"); // dịch block
            cipher.init(Cipher.DECRYPT_MODE, priServer);
            byte decryptOut[] = cipher.doFinal(Base64.getDecoder().decode(line));

            result += new String(decryptOut);
        }

        System.out.println("socketReadline:" + result);
        return result;
    }

    public static void socketSend(String line) throws Exception {

        Cipher c = Cipher.getInstance("RSA"); // để mã hóa chuỗi gửi cho Client
        c.init(Cipher.ENCRYPT_MODE, pubClient); // dùng pubClient

        final RSAPublicKey rsapub = (RSAPublicKey) pubClient; // lấy độ dài bit của khoá public Client
        int keyBitLength = rsapub.getModulus().bitLength();

        byte[] lineBytes = line.getBytes();

        int validSize = keyBitLength / 8 - 11; // chuẩn độ dài mã hoá RSA hợp lệ

        int blockCount = (int) Math.ceil((float) lineBytes.length / validSize);
        int remaining = line.getBytes().length; // chia thành từng block theo độ dài hợp lệ

        out.writeInt(blockCount); // trả về tổng số block cho Client
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

    @Override
    @SuppressWarnings("ConvertToTryWithResources")
    public void run() {
        System.out.println("New Client Connected...");
        try {
            while (true) {
                String input = socketReadLine();

                if (input.contains("get-shortest-path")) { // theo cấu trúc get-shortest-path-A-B
                    String nodeStart = input.split("-")[3]; // lấy cái A
                    String nodeEnd = input.split("-")[4]; // lấy cái B

                    String edgesString = socketReadLine();
                    Dijkstra dijkstra = new Dijkstra(edgesString);

                    if (dijkstra.validateGraph(nodeStart, nodeEnd)) {
                        List<String> result = dijkstra.getShortestPath(nodeStart, nodeEnd);
                        if (result == null) {
                            socketSend("ERROR: Func ERR.");
                        } else {
                            socketSend(gson.toJson(result));
                        }
                    } else {
                        socketSend("ERROR: Your nodes invalid or does not have a path.");
                    }
                } else if (input.contains("get-algorythm")) { // theo cấu trúc get-algorythm-NameOfAlgorythm
                    String algorithm = input.split("-")[2]; // tìm tên thuật toán trong LapLichCPU
                    System.out.println(algorithm);

                    ExecuteCPUAlgorithm executeCPUAlgorythm = new ExecuteCPUAlgorithm(
                            socketReadLine(),
                            algorithm); // hàm của Server xử lý

                    socketSend(gson.toJson(executeCPUAlgorythm.execute())); // execute trả về LapLichCPU. 
                    // Client nhận và xử lý thành dataset để dùng hiển thị chart
                    // trả kết quả về Client để xuất ra đồ thị
                }

                if (input.equals("bye")) {
                    break;
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
