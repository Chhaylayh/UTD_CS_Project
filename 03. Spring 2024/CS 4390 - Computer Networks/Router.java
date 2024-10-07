import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Router {
    private String IPAddress;
    private String bridgeID;
    private int portNumber;
    private String EthernetAddress;

    private List<String> networkInterfaces = new ArrayList<>();
    private Map<String, String> ARPCache = new HashMap<>();
    private Set<String> neighbors = new HashSet<>();
    private Map<String, Integer> broadcastSequences = new HashMap<>();
    private Map<String, String> outputFiles = new HashMap<>();
    private Map<String, String> inputFiles = new HashMap<>();
    private String routerIP;

    public Router(String[] args) {
        this.IPAddress = args[0];
        this.bridgeID = args[1];
        this.portNumber = Integer.parseInt(args[2]);
        this.EthernetAddress = args[3];

        for (int i = 0; i < args.length; i += 5) {
            String networkIP = args[i];
            int bridgePort = Integer.parseInt(args[i + 2]);
            String ethernetAddress = args[i + 3];
            networkInterfaces.add(networkIP);
            ARPCache.put(networkIP, ethernetAddress);
            outputFiles.put(networkIP, String.format("to%sP%d.txt", bridgeID, bridgePort));
            inputFiles.put(networkIP, String.format("from%sP%d.txt", bridgeID, bridgePort));
            if (i == 0) routerIP = networkIP;
        }
    }

    public void periodicTasks() {
        sendMessages();
    }

    private void sendMessages() {
        for (String networkIP : networkInterfaces) {
            Ethernet helloFrame = new Ethernet("99", ARPCache.get(networkIP), "HL", networkIP + " " + ARPCache.get(networkIP));
            sendToEthernet(helloFrame, networkIP);
        }
    }

    public void receiveFromEthernet(Ethernet ethernet, String networkIP) {
        switch (ethernet.getPacketType()) {
            case "IP":
                IP ip = new IP(ethernet.getData());
                if (networkInterfaces.contains(ip.getDestination())) {
                    sendToIP(ip, networkIP);
                } else {
                    forwardIpPacket(ip, networkIP);
                }
                break;
           
            case "ARP":
                ARP ARPMessage = new ARP(ethernet.getData());
                if ("REQ".equals(ARPMessage.getType())) {
                    ARP ARPReply = new ARP("REP", ARPMessage.getTargetIP(), ARPCache.get(networkIP), ARPMessage.getSourceIP(), ARPMessage.getSourceMac());
                    sendToEthernet(new Ethernet(ARPMessage.getSourceMac(), ARPCache.get(networkIP), "ARP", ARPReply.toString()), networkIP);
                }
                ARPCache.put(ARPMessage.getSourceIP(), ARPMessage.getSourceMac());
                break;
            
            case "HL":
                String[] helloData = ethernet.getData().split(" ");
                neighbors.add(helloData[0]);
                break;
            
            case "BC":
                Broadcast broadcast = new Broadcast(ethernet.getData());
                String key = broadcast.getSourceRouterIP() + "-" + broadcast.getSequenceNumber();
                if (!broadcastSequences.containsKey(key)) {
                    broadcastSequences.put(key, broadcast.getSequenceNumber());
                    if (networkInterfaces.contains(broadcast.getDestinationNetwork())) {
                        sendToIP(new IP(broadcast.getIPPacket()), networkIP);
                    } else {
                        forwardBroadcast(broadcast, networkIP);
                    }
                }
                break;
            
            default:
                System.out.printf("Unknown Ethernet Frame: %s%n", ethernet.toString());
                break;
        }
    }

    public void sendToIP(IP ip, String networkIp) {
        String destinationMac = ARPCache.get(ip.getDestination());
        if (destinationMac == null) {
            ARP ARPReq = new ARP("REQ", ip.getDestination(), networkIp, ARPCache.get(networkIp));
            sendToEthernet(new Ethernet("99", ARPCache.get(networkIp), "ARP", ARPReq.toString()), networkIp);
        } else {
            sendToEthernet(new Ethernet(destinationMac, ARPCache.get(networkIp), "IP", ip.toString()), networkIp);
        }
    }

    public void forwardIpPacket(IP ip, String fromNetwork) {
        Broadcast broadcast = new Broadcast(routerIP, getNextSequenceNumber(routerIP), ip.getDestinationNetwork(), ip.toString());
        forwardBroadcast(broadcast, fromNetwork);
    }

    public void forwardBroadcast(Broadcast broadcast, String fromNetwork) {
        for (String networkIP : networkInterfaces) {
            if (!networkIP.equals(fromNetwork)) {
                sendToEthernet(new Ethernet("99", ARPCache.get(networkIP), "BC", broadcast.toString()), networkIP);
            }
        }
    }

    public void sendToEthernet(Ethernet ethernet, String networkIp) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFiles.get(networkIp)), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(ethernet.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFromBridge(String networkIP) {
        try {
            Path filePath = Paths.get(inputFiles.get(networkIP));

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFiles.get(networkIP)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Ethernet ethernet = new Ethernet(line);
                    receiveFromEthernet(ethernet, networkIP);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextSequenceNumber(String sourceRouterIp) {
        int nextSequence = broadcastSequences.getOrDefault(sourceRouterIp, 0) + 1;
        broadcastSequences.put(sourceRouterIp, nextSequence);
        return nextSequence;
    }

    // public void mainLoop() {
    //     boolean dataReceived = true;
    //     while (dataReceived) {
    //         while (true) {
    //             periodicTasks();
    //             for (String networkIp : networkInterfaces) {
    //                 receiveFromBridge(networkIp);
    //             }
    //             try {
    //                 Thread.sleep(1000); // Sleep for 1 second
    //             } catch (InterruptedException e) {
    //                 Thread.currentThread().interrupt();
    //             }
    //         }
    //     }
    // }

    public void mainLoop() {
        boolean dataReceived = true;
        while (dataReceived) {
            for (String networkIp : networkInterfaces) {
                receiveFromBridge(networkIp);
            }
            periodicTasks();
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Check if there's any ongoing communication on any network interface
            dataReceived = isDataReceived();
        }
    }
    
    private boolean isDataReceived() {
        // Check if there are any ongoing communications on any network interface
        for (String networkIp : networkInterfaces) {
            Path filePath = Paths.get(inputFiles.get(networkIp));
            try {
                // Check if the input file for the network interface is not empty
                if (Files.exists(filePath) && Files.size(filePath) > 0) {
                    return true; // Data is still being received
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
        return false; // No data is being received on any network interface
    }
    


    public static void main(String[] args) {
        if (args.length < 5 || args.length % 5 != 0) {
            System.err.println("Usage: router <IP1> <BridgeID1> <Port1> <MAC1> <IP2> <BridgeID2> <Port2> <MAC2>");
            System.exit(1);
        }
        Router router = new Router(args);
        router.mainLoop();
    }
}