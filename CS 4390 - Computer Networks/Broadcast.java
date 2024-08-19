public class Broadcast {
    private String sourceRouterIP;
    private int sequenceNumber;
    private String destinationNetwork;
    private String IP;

    public Broadcast(String sourceRouterIP, int sequenceNumber, String destinationNetwork, String IP) {
        this.sourceRouterIP = sourceRouterIP;
        this.sequenceNumber = sequenceNumber;
        this.destinationNetwork = destinationNetwork;
        this.IP = IP;
    }

    public Broadcast(String broadcastString) {
        String[] parts = broadcastString.split(" ", 4);
        this.sourceRouterIP = parts[0];
        this.sequenceNumber = Integer.parseInt(parts[1]);
        this.destinationNetwork = parts[2];
        this.IP = parts[3];
    }

    public String getSourceRouterIP() {
        return sourceRouterIP;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getDestinationNetwork() {
        return destinationNetwork;
    }

    public String getIPPacket() {
        return IP;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s %s", sourceRouterIP, sequenceNumber, destinationNetwork, IP);
    }
}
