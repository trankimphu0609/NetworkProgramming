/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author trankimphu0609
 */
public class Server {

    public static int port = 1234;
    public static int numThread = 3;
    private static ServerSocket server = null;

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(numThread);

        try {
            server = new ServerSocket(port);
            System.out.println("Waiting for client...");

            while (true) {
                Socket socket = server.accept();
                executor.execute(new RunnableApp(socket));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }
}
