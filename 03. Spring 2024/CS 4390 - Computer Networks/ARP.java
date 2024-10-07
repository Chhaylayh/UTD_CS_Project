public class ARP {
    private String type;
    private String targetIP;
    private String sourceIP;
    private String sourceMac;
    private String targetMac;

    public ARP(String type, String targetIP, String sourceIP, String sourceMac) {
        this.type = type;
        this.targetIP = targetIP;
        this.sourceIP = sourceIP;
        this.sourceMac = sourceMac;
        this.targetMac = null;
    }

    public ARP(String type, String targetIP, String sourceIP, String sourceMac, String targetMac) {
        this.type = type;
        this.targetIP = targetIP;
        this.sourceIP = sourceIP;
        this.sourceMac = sourceMac;
        this.targetMac = targetMac;
    }

    public ARP(String ARPString) {
        String[] parts = ARPString.split(" ");
        this.type = parts[0];
        this.targetIP = parts[1];
        this.sourceIP = parts[2];
        this.sourceMac = parts[3];
        this.targetMac = parts.length == 5 ? parts[4] : null;
    }

    public String getType() {
        return type;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public String getSourceMac() {
        return sourceMac;
    }

    public String getTargetMac() {
        return targetMac;
    }

    @Override
    public String toString() {
        if (targetMac == null) {
            return String.format("%s %s %s %s", type, targetIP, sourceIP, sourceMac);
        } else {
            return String.format("%s %s %s %s %s", type, targetIP, targetMac, sourceIP, sourceMac);
        }
    }
}