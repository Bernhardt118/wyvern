package Own.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserList {

    private List<User> userList;
    private ArrayList<String> past;
    private ArrayList<Pending> pen;

    public UserList() {
        userList = Collections.synchronizedList(new ArrayList<User>());
        past = new ArrayList<String>();
        pen = new ArrayList<Pending>();
    }

    public boolean connect(User u) {
        String username = u.getUserName();
        if (username.contains(" ")) {
            System.out.println("space");
            return false;
        }

        if (checkUserName(u.getUserName())) {
            userList.add(u);
            u.connect();
            past.add(u.getUserName());
            Packet packU = new Packet(keyword.username, u.getUserName());
            sendPack(u.getUserName(), packU);
            List<String> mapped = (userList.stream().map(e -> e.getUserName()).collect(Collectors.toList()));
            ArrayList<String> listy = new ArrayList<>();
            listy.addAll(mapped);
            Packet pack = new Packet(keyword.connected, listy, u.getUserName() + " has joined the chat\n");
            broadcast(pack);
            for (Pending un : pen) {
                if (un.getSendTo().equals(u.getUserName())) {
                    sendPack(un.getSendTo(), un.getPen());
                }
            }
            return true;
        }
        return false;
    }

    public void broadcast(Packet pack) {
        for (User u : userList) {
            try {
                u.sendPacket(pack);
            } catch (Exception e) {
                System.out.println("Could not broadcast message");
            }
        }
    }

    public boolean connect(String u) {
        while (true) {
            if (checkUserName(u)) {
                break;
            }
            return false;
        }
        return true;
    }

    private boolean checkUserName(String u) {
        for (int i = 0; i < userList.size(); i++) {
            if (u.equals(userList.get(i).getUserName())) {
                return false;
            }
        }
        return true;
    }

    public void disconnect(User u) {
        if (!u.isConnected()) {
            return;
        }
        userList.remove(u);
        try {
            u.disconnect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            List<String> mapped = (userList.stream().map(e -> e.getUserName()).collect(Collectors.toList()));
            ArrayList<String> listy = new ArrayList<>();
            listy.addAll(mapped);
            Packet pack = new Packet(keyword.disconnect, listy, "\n" + u.getUserName() + " has disconnected\n");
            broadcast(pack);
        }
    }

    public boolean sendPack(String sendTO, Packet pack) {
        for (User u : userList) {
            OwnServer.chatWindow.append("Final step to send mess\n");
            if (u.getUserName().equals(sendTO)) {
                u.sendPacket(pack);
                return true;
            }
        }
        for (String u : past) {
            if (u.equals(sendTO)) {
                Pending p = new Pending(u, pack);
                pen.add(p);
                return true;
            }
        }
        OwnServer.chatWindow.append("\nCould not find user: " + sendTO + "\n");
        for (User u : userList) {
            if (u.getUserName().equals(pack.getUser())) {
                Packet p = new Packet(keyword.notification, "Could not find user: " + sendTO + "\n");
                u.sendPacket(p);
            }
        }
        return false;
    }
}
