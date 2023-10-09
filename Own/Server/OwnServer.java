package Own.Server;

import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class OwnServer extends JFrame {

    private int port;
    private ServerSocket serSoc;
    public static JTextArea chatWindow;

    public OwnServer(int portInt) {
        super("Server");
        this.port = portInt;
        chatWindow = new JTextArea();
        DefaultCaret caret = (DefaultCaret) chatWindow.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
        chatWindow.setBorder(BorderFactory.createCompoundBorder(
                chatWindow.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(new JScrollPane(chatWindow));
        setSize(500, 450);
        setVisible(true);
    }

    public void run() {
        UserList ul = new UserList();

        try {
            serSoc = new ServerSocket(port);
            while (!serSoc.isClosed()) {
                Socket soc = serSoc.accept();
                User newUser = new User(soc);
                Thread t = new Thread(new ServerThread(newUser, ul));
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Issue opening the thread.");
        }
    }
}
