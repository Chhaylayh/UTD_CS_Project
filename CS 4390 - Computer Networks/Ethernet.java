public class Ethernet {
    private String destination;
    private String source;
    private String packetType;
    private String data;

    public Ethernet(String destination, String source, String packetType, String data) {
        this.destination = destination;
        this.source = source;
        this.packetType = packetType;
        this.data = data;
    }

    public Ethernet(String EthernetString) {
        String[] parts = EthernetString.split(" ", 4);
        this.destination = parts[0];
        this.source = parts[1];
        this.packetType = parts[2];
        this.data = parts[3];
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public String getPacketType() {
        return packetType;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", destination, source, packetType, data);
    }
}
