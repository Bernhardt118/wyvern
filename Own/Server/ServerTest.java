package Own.Server;

import javax.swing.JFrame;

public class ServerTest {
    public static void main(String[] args) {
        OwnServer s1 = new OwnServer(6789);
        s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        s1.run();
    }
}
