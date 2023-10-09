package Own.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {
    private String userName, password;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean connected;
    Socket sock;

    public User(Socket sock) throws IOException {
        this.sock = sock;
        out = new ObjectOutputStream(this.sock.getOutputStream());
        in = new ObjectInputStream(this.sock.getInputStream());
    }

    public Packet recieveMess() throws ClassNotFoundException, IOException {
        Packet message;
        message = (Packet) (in.readObject());
        OwnServer.chatWindow.append(userName + " " + message.getMessage() + "\n");
        return message;
    }

    public void sendPacket(Packet pack) {
        try {
            out.writeObject(pack);
            OwnServer.chatWindow.append("\n" + userName + " " + pack.getMessage());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void connect() {
        this.connected = true;
    }

    public void disconnect() throws IOException {
        sock.close();
    }

}
