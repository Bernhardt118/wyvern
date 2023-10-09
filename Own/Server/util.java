package Own.Server;

public class util {
    public static String asString(keyword kw) {
        switch (kw) {
            case notification:
                return "notification";
            case data:
                return "data";
            case message:
                return "message";
            case broadcast:
                return "broadcast";
            case sendTo:
                return "Recipient";
            case disconnect:
                return "disconnect";
            default:
                return "unknown";
        }
    }
}
