package Own.Client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Own.Server.Packet;
import Own.Server.keyword;
//import Own.Server.util;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private JSplitPane Textpanel;
    private JPanel inputPanel, LeftPanel, RightPanel, mainPanel;
    private JTextArea ul;
    // private JScrollPane users;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String serverIP;
    private Socket connection;
    private String username = "Default";
    private ArrayList<String> connectedClients = new ArrayList<String>();

    public Client(String host) {
        super("Client");
        serverIP = host;
        Textpanel = new JSplitPane();
        mainPanel = new JPanel();
        inputPanel = new JPanel();
        RightPanel = new JPanel();
        LeftPanel = new JPanel();
        userText = new JTextField();
        chatWindow = new JTextArea();
        ul = new JTextArea();
        ul.setAlignmentX(Component.CENTER_ALIGNMENT);
        userText.setEditable(false);

        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!e.getActionCommand().equals("")) {
                            sendData(e.getActionCommand());
                        } else {
                            chatWindow.setForeground(new Color(255, 255, 255));
                            showMessage("\nCannot send blank message");;
                        }
                        userText.setText("");
                    }

                });
        setPreferredSize(new Dimension(600, 450));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(mainPanel);
        Color c = new Color(0, 51, 102);
        setBackground(c);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // mainPanel.add(inputPanel);
        // inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        // inputPanel.add(userText);

        mainPanel.add(Textpanel);
        Textpanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        Textpanel.setDividerLocation(150);
        Textpanel.setLeftComponent(LeftPanel);
        Textpanel.setRightComponent(RightPanel);

        LeftPanel.setLayout(new BoxLayout(LeftPanel, BoxLayout.Y_AXIS));

        RightPanel.setLayout(new BoxLayout(RightPanel, BoxLayout.Y_AXIS));
        RightPanel.add(new JScrollPane(chatWindow));
        RightPanel.add(inputPanel);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(userText);
        LeftPanel.add(new JScrollPane(ul));
        ul.setEditable(false);
        chatWindow.setEditable(false);
        mainPanel.setBackground(c);
        Textpanel.setBackground(c);
        ul.setBackground(c);
        chatWindow.setBackground(new Color(0, 102, 51));
        chatWindow.setBorder(BorderFactory.createCompoundBorder(
                chatWindow.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        userText.setBackground(new Color(0, 0, 0));
        userText.setBorder(BorderFactory.createCompoundBorder(
                userText.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        chatWindow.setForeground(new Color(255, 255, 255));
        userText.setForeground(new Color(231, 210, 124));
        ul.setForeground(new Color(231, 210, 124));
        ul.setBorder(BorderFactory.createCompoundBorder(
                ul.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        ul.setText("Connect to display\nonline users");
        chatWindow.setLineWrap(true);

        pack();
        // add(userText, BorderLayout.NORTH);
        //
        // add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        // add(new JScrollPane(ul), BorderLayout.EAST);
        setVisible(true);
    }

    public void startRunnig() {
        try {
            connect();
            setupStreams();
            whileChatting();
        } catch (EOFException eofException) {
            showMessage("\nClient terminated connection");
        } catch (IOException ioException) {
            showMessage("\nServer unavailable.");
        } finally {
            closeAll();
        }
    }

    private void connect() throws IOException {
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException {
        out = new ObjectOutputStream(connection.getOutputStream());
        out.flush();
        in = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams setup complete \n");
    }

    private void whileChatting() throws IOException {
        ableToType(true);
        Packet pack = new Packet();
        do {
            try {
                pack = (Packet) in.readObject();
                processInput(pack);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("\nInvalid type \n");
            }
        } while (pack.getKeyword() != keyword.closedown);
    }

    private void closeAll() {
        showMessage("\nclosing connection \n");
        ableToType(false);
        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (connection != null)
                connection.close();
        } catch (IOException ioException) {
            System.out.println("Error occured when closing the connection.");
        }
    }

    private void sendData(String message) {
        try {
            Packet pack;
            if (message.startsWith("/w")) {
                chatWindow.setForeground(new Color(231, 210, 124));
                String mes = message.substring(2).split(": ")[1];
                String user = message.substring(2).split(": ")[0];
                pack = new Packet(keyword.sendTo, user, username, "[whisper] " + mes);
                out.writeObject(pack);
                showMessage("\n[whisper to " + user + "] " + mes);
                out.flush();
                // showMessage("\n"+username+"-"+ pack.getMessage() + "\n");
            } else if (message.equals("\\users")) {
                chatWindow.setForeground(new Color(255, 255, 255));
                for (String u : connectedClients) {
                    showMessage(u + "\n");
                }
            } else if (message.equals("\\list")) {
                chatWindow.setForeground(new Color(255, 255, 255));
                showMessage("\n\\w<Users>: \"Message\"\n\\users");
            } else {
                chatWindow.setForeground(new Color(231, 210, 124));
                pack = new Packet(keyword.broadcast, username, message);
                out.writeObject(pack);
                out.flush();
                // showMessage("\n" + username + "-" + pack.getMessage() + "\n");
            }

        } catch (IOException ioException) {
            chatWindow.append("\nERROR Occured");
        }
    }

    private void showMessage(final String m) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        //chatWindow.setForeground(new Color(245, 245, 245));
                        chatWindow.append(m);
                    }
                });
    }

    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userText.setEditable(tof);
                    }
                });
    }

    private void processInput(Packet pack) {
        switch (pack.getKeyword()) {
            case message:
                chatWindow.setForeground(new Color(231, 210, 124));
                pack.getMessage();
                showMessage("\n" + pack.getUser() + ": " + pack.getMessage());
                break;
            case broadcast:
                chatWindow.setForeground(new Color(231, 210, 124));
                pack.getMessage();
                showMessage("\n" + pack.getUser() + ": " + pack.getMessage());
                break;
            case connected:
                chatWindow.setForeground(new Color(255, 255, 255));
                connectedClients = pack.getUsers();
                showMessage("\n" + pack.getMessage());
                appendList();
                break;
            case username:
                chatWindow.setForeground(new Color(255, 255, 255));
                username = pack.getMessage();
                showMessage("\nYour username is set as: " + username + "\n");
                break;
            case disconnect:
                chatWindow.setForeground(new Color(255, 255, 255));
                connectedClients = pack.getUsers();
                showMessage(pack.getMessage());
                appendList();
                break;
            default:
                chatWindow.setForeground(new Color(255, 255, 255));
                pack.getMessage();
                showMessage("\nServer: " + pack.getMessage());
                //chatWindow.setForeground(new Color(231, 210, 124));
        }
    }

    private void appendList() {
        ul.setText("Online users: " + connectedClients.size() + "\n");
        for (String u : connectedClients) {
            ul.append(u + "\n");
        }
    }

}
