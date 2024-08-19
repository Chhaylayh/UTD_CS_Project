import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Host {
    private String IPAddress;
    private String EthernetAddress;
    private String defaultRouter;
    private String bridgeID;
    private int portNumber;
    private String destinationIP;
    private String dataString;

    private Map<String, String> ARPCache = new HashMap<>();
    private String fromBridgeFile;
    private String toBridgeFile;

    public Host(String[] args) {
        this.IPAddress = args[0];
        this.EthernetAddress = args[1];
        this.defaultRouter = args[2];
        this.bridgeID = args[3];
        this.portNumber = Integer.parseInt(args[4]);
        this.destinationIP = args[5];
        this.dataString = args.length > 6 ? args[6] : null;
        this.fromBridgeFile = String.format("from%sP%d.txt", bridgeID, portNumber);
        this.toBridgeFile = String.format("to%sP%d.txt", bridgeID, portNumber);
    }

    public void transportPeriodicTasks() {
        if (dataString != null) {
            Transport transport = new Transport("DA", 0, 0, dataString.substring(0, Math.min(5, dataString.length())));
            IP ip = new IP(destinationIP, IPAddress, transport.toString());
            sendToIP(ip);
            dataString = null;
        }
    }

    public void receiveFromIP(IP ip) {
        Transport transport = new Transport(ip.getData());
        System.out.printf("Received Transport Message: %s%n", transport.toString());
    }

    public void sendToIP(IP ip) {
        String destinationMac = ARPCache.get(ip.getDestination());
        
        if (destinationMac == null) {
            ARP ARPReq = new ARP("REQ", ip.getDestination(), IPAddress, EthernetAddress);
            sendToEthernet(new Ethernet("99", EthernetAddress, "ARP", ARPReq.toString()));
        } else {
            sendToEthernet(new Ethernet(destinationMac, EthernetAddress, "IP", ip.toString()));
        }
    }

    public void receiveFromEthernet(Ethernet ethernet) {
        switch (ethernet.getPacketType()) {
            case "IP":
                receiveFromIP(new IP(ethernet.getData()));
                break;
            
            case "ARP":
                ARP ARPMessage = new ARP(ethernet.getData());
                if ("REQ".equals(ARPMessage.getType())) {
                    ARP ARPReply = new ARP("REP", ARPMessage.getTargetIP(), EthernetAddress, ARPMessage.getSourceIP(), ARPMessage.getSourceMac());
                    sendToEthernet(new Ethernet(ARPMessage.getSourceMac(), EthernetAddress, "ARP", ARPReply.toString()));
                }
                ARPCache.put(ARPMessage.getSourceIP(), ARPMessage.getSourceMac());
                break;
            
            default:
                System.out.printf("Unknown Ethernet Frame: %s%n", ethernet.toString());
                break;
        }
    }

    public void sendToEthernet(Ethernet ethernet) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(toBridgeFile), StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            writer.write(ethernet.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean receiveFromBridge() {
        try {
            Path filePath = Paths.get(fromBridgeFile);
            
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                boolean dataReceived = false;
                while ((line = reader.readLine()) != null) {
                    Ethernet ethernet = new Ethernet(line);
                    receiveFromEthernet(ethernet);
                    dataReceived = true;
                }
                return dataReceived; 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void mainLoop() {
        boolean dataReceived = true;
        while (dataReceived) {
            transportPeriodicTasks();
            dataReceived = receiveFromBridge();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 6) {
            System.err.println("Usage: host <IP> <Ethernet> <Default Router> <Bridge ID> <Port Number> <Destination Node> [Data]");
            System.exit(1);
        }
        Host host = new Host(args);
        host.mainLoop();
    }
}
