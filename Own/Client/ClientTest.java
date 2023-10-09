package Own.Client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientTest {
    public static void main(String[] args) {
        Client c1;
        String ip = JOptionPane.showInputDialog(null, "What is the address of the server?", "25.35.6.239");
        c1 = new Client(ip);
        c1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c1.startRunnig();
    }
}
