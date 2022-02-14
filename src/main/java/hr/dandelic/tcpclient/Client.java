package hr.dandelic.tcpclient;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author dominikandelic
 */
public class Client {

    static String[] activeUserArray;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket socket;
    private static String userId;
    private static String username;
    private static Scanner scanner;
    private static MessageReader messageReader;
    private static ClientGui gui;

    public static void main(String[] args) {
        Client client = new Client();
        try {
            SwingUtilities.invokeAndWait((gui = new ClientGui()));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        while (true) {
            if (gui.getUsername() != null) {
                username = gui.getUsername();
                break;
            }
        }

        client.start("localhost", 25555);
        gui.setOut(out);
        gui.addSubmitMessageListener();
        Thread readerThread = new Thread(messageReader = new MessageReader(in, username, userId, gui));
        readerThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.stop();
            if (messageReader.isRunnable()) {
                messageReader.kill();
            }
        }));
    }

    public void start(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Successfully connected, sending greeting.");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.printf("handshake\n");

            userId = in.readLine();
            System.out.println("ID: " + userId);
            scanner = new Scanner(System.in);
            out.println(username);

            while (true) {
                if (in.ready()) {
                    String activeUserListString = in.readLine();
                    activeUserArray = activeUserListString.split(",");
                    gui.setActiveUserList();
                    //System.out.println("Uhvatili smo userlist.");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to connect.");
            System.exit(-1);

        }
    }

    public void stop() {
        try {
            out.close();
            in.close();
            socket.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
