public class IP {
    private String destination;
    private String source;
    private String data;

    public IP(String destination, String source, String data) {
        this.destination = destination;
        this.source = source;
        this.data = data;
    }

    public IP(String packetString) {
        String[] parts = packetString.split(" ", 3);
        this.destination = parts[0];
        this.source = parts[1];
        this.data = parts[2];
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public String getData() {
        return data;
    }

    public String getDestinationNetwork() {
        return destination.split("\\.")[0];
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", destination, source, data);
    }
}