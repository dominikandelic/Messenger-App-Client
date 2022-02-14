/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.dandelic.tcpclient;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author dominikandelic
 */
public class MessageReader implements Runnable {

    private final ClientGui gui;
    BufferedReader in;
    String line;
    String username;
    String userId;
    private boolean runnable = true;

    public MessageReader(BufferedReader in, String username, String userId, ClientGui gui) {
        this.in = in;
        this.userId = userId;
        this.username = username;
        this.gui = gui;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void kill() {
        runnable = false;
    }

    @Override
    public void run() {
        while (runnable) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (in.ready()) {
                    line = in.readLine();
                    if (line.equals("QUIT_CLIENT")) {
                        gui.getChatArea().append("Lost connection to the server.");
                        gui.getMessageArea().setEditable(false);
                        break;
                    }
                    if (line.contains("disconnected")) {
                        String[] temp = line.split(" ");
                        gui.getActiveUserList().removeElement(temp[0]);
                    } else if (line.contains("connected")) {
                        String[] temp = line.split(" ");
                        gui.getActiveUserList().addElement(temp[0]);
                    }
                    gui.getChatArea().append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println("Thread stopped.");
                break;
            }
        }
        runnable = false;

    }

}
