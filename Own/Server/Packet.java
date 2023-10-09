package Own.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
    private String message;
    private keyword keyword;
    private String user;
    private ArrayList<String> users = new ArrayList<String>();
    private String sendfrom;

    public Packet(keyword kw, String m) {
        keyword = kw;
        message = m;
    }

    public Packet(keyword kw, ArrayList<String> users) {
        keyword = kw;
        this.users = users;
    }

    public Packet(keyword kw, String user, String m) {
        keyword = kw;
        this.user = user;
        message = m;
    }

    public Packet() {
    }

    public Packet(keyword kw, ArrayList<String> users, String m) {
        keyword = kw;
        this.users = users;
        message = m;
    }

    public Packet(Own.Server.keyword kw, String sendTo, String sendFrom, String mes) {
        keyword = kw;
        this.user = sendTo;
        this.sendfrom = sendFrom;
        message = mes;

    }

    public String getSendFrom() {
        return sendfrom;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public String getMessage() {
        return message;
    }

    public keyword getKeyword() {
        return keyword;
    }

    public String getUser() {
        return user;
    }

}
