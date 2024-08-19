public class Transport {
    private String type;
    private int sequenceNumber;
    private int channelNumber;
    private String data;

    public Transport(String type, int sequenceNumber, int channelNumber, String data) {
        this.type = type;
        this.sequenceNumber = sequenceNumber;
        this.channelNumber = channelNumber;
        this.data = data;
    }

    public Transport(String transportString) {
        String[] parts = transportString.split(" ", 4);
        this.type = parts[0];
        this.sequenceNumber = Integer.parseInt(parts[1]);
        this.channelNumber = Integer.parseInt(parts[2]);
        this.data = parts[3];
    }

    public String getType() {
        return type;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %s", type, sequenceNumber, channelNumber, data);
    }
}