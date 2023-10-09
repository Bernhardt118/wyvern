package Own.Server;

import java.io.EOFException;

public class ServerThread implements Runnable {

    private User u;
    private UserList ul;

    public ServerThread(User newUser, UserList ul) {
        this.u = newUser;
        this.ul = ul;
    }

    @Override
    public void run() {
        try {
            Packet pack = new Packet(keyword.notification, "Please enter username");
            u.sendPacket(pack);
            u.setUserName(((u.recieveMess()).getMessage()));
            while (!ul.connect(u)) {
                pack = new Packet(keyword.notification, "Username invalid. Please enter a new username. \n");
                u.sendPacket(pack);
                u.setUserName(u.recieveMess().getMessage());
            }

            while (!u.sock.isClosed()) {
                Packet packRes = (Packet) u.recieveMess();
                if (packRes.getKeyword() == keyword.disconnect) {
                    break;
                }
                OwnServer.chatWindow.append("processing message\n");
                processInput(packRes);
            }
        } catch (EOFException eofException) {
            eofException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            ul.disconnect(u);
        }

    }

    private void processInput(Packet pack) {
        switch (pack.getKeyword()) {
            case sendTo:
                Packet sendPack = new Packet(keyword.message, pack.getSendFrom(), pack.getMessage());
                OwnServer.chatWindow.append(("sent message\n"));
                ul.sendPack(pack.getUser(), sendPack);
                break;
            case broadcast:
                ul.broadcast(pack);
                break;
            default:
                OwnServer.chatWindow.append("\n" + util.asString(pack.getKeyword()) + "\n");
                break;
        }
    }
}
