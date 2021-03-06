/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.dandelic.tcpclient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author dominikandelic
 */
public class ClientGui implements Runnable {

    private final String username;
    private final DefaultListModel<String> activeUserList;
    private final JTextArea chatArea;
    private final JButton submitMessage;
    private final JTextArea messageArea;
    private PrintWriter out;
    public ClientGui() {
        JFrame mainFrame = new JFrame("Messenger");

        try {
            mainFrame.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Insets margin = new Insets(4, 4, 4, 4);
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.setPreferredSize(new Dimension(750, 300));

        username = JOptionPane.showInputDialog(mainFrame, "Please enter your username", "Username Input", JOptionPane.INFORMATION_MESSAGE);
        if (username == null || username.isEmpty()) {
            System.err.println("No username set.");
            System.exit(-1);
        }
        mainFrame.setTitle("Messenger (" + username + ")");

        mainFrame.add(layoutPanel);

        JPanel eastLayout = new JPanel(new BorderLayout());
        layoutPanel.add(eastLayout, BorderLayout.EAST);

        activeUserList = new DefaultListModel<>();
        JList<String> activeUsers = new JList<>(activeUserList);
        activeUsers.addListSelectionListener((ListSelectionEvent e) -> {
        });

        eastLayout.add(activeUsers, BorderLayout.CENTER);

        JPanel southLayout = new JPanel(new BorderLayout());
        layoutPanel.add(southLayout, BorderLayout.SOUTH);

        submitMessage = new JButton("Send");

        southLayout.add(submitMessage, BorderLayout.EAST);

        messageArea = new JTextArea();
        messageArea.setText("Send a message");
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        southLayout.add(messageArea, BorderLayout.CENTER);
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messageArea.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                messageArea.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                messageArea.setText("");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
        messageArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (messageArea.getText().contains("Send a message")) {
                    messageArea.setText(messageArea.getText().substring(0, 1));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    e.consume();
                    if (messageArea.getText().equals("")) {
                        return;
                    }
                    out.println(messageArea.getText());
                    messageArea.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setMargin(margin);
        chatArea.setLineWrap(true);
        layoutPanel.add(chatArea, BorderLayout.CENTER);

        DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                out.println("QUIT_PROCESS");
            }
        });

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getUsername() {
        return username;
    }

    public JTextArea getChatArea() {
        return chatArea;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public DefaultListModel<String> getActiveUserList() {
        return activeUserList;
    }

    @Override
    public void run() {

    }

    public void addSubmitMessageListener() {
        submitMessage.addActionListener(e -> {
            String line = submitMessage.getText();
            if (line.equals("")) {
                return;
            }
            out.println(line);
            submitMessage.setText("");
        });
    }

    public void setActiveUserList() {
        if (Client.activeUserArray != null) {
            for (String activeUser : Client.activeUserArray) {
                activeUserList.addElement(activeUser);
            }
        }
    }

}
