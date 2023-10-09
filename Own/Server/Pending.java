package Own.Server;

class Pending {
    private String sendTo;
    private Packet message;

    public Pending(String sendTo, Packet m) {
        this.sendTo = sendTo;
        message = m;
    }

    public String getSendTo() {
        return sendTo;
    }

    public Packet getPen() {
        return message;
    }
}