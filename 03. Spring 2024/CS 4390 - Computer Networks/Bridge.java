import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Bridge {
    private String bridgeID;
    private int portNumber;
    private List<String> neighboringBridges;

    private Map<String, String> MACTable = new HashMap<>();
    private Map<String, String> outputFiles = new HashMap<>();
    private Map<String, String> inputFiles = new HashMap<>();

    public Bridge(String[] args) {
        this.bridgeID = args[0];
        this.portNumber = Integer.parseInt(args[1]);
        this.neighboringBridges = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));

        for (int port = 1; port <= portNumber; port++) {
            inputFiles.put(String.valueOf(port), String.format("from%sP%d.txt", bridgeID, port));
            outputFiles.put(String.valueOf(port), String.format("to%sP%d.txt", bridgeID, port));
        }
    }

    public void receiveFromEthernet(Ethernet ethernet, String port) {
        MACTable.put(ethernet.getSource(), port);

        String destinationPort = MACTable.get(ethernet.getDestination());

        if (destinationPort == null || "99".equals(ethernet.getDestination())) {
            forwardToAllPorts(ethernet, port);
        } else {
            sendToPort(ethernet, destinationPort);
        }
    }

    public void forwardToAllPorts(Ethernet ethernetFrame, String sourcePort) {
        for (String port : outputFiles.keySet()) {
            if (!port.equals(sourcePort)) {
                sendToPort(ethernetFrame, port);
            }
        }
    }

    public void sendToPort(Ethernet ethernetFrame, String port) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFiles.get(port)),
                StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(ethernetFrame.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFromBridgePort(String port) {
        try {
            Path filePath = Paths.get(inputFiles.get(port));

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFiles.get(port)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Ethernet ethernetFrame = new Ethernet(line);
                    receiveFromEthernet(ethernetFrame, port);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mainLoop() {
        boolean dataReceived = true;
        while (dataReceived) {
            for (String port : inputFiles.keySet()) {
                receiveFromBridgePort(port);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            dataReceived = isDataReceived();
        }
    }
    
    private boolean isDataReceived() {
        for (String port : inputFiles.keySet()) {
            Path filePath = Paths.get(inputFiles.get(port));
            try {
                if (Files.size(filePath) > 0) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: bridge <BridgeID> <NumPorts> [NeighboringBridges]");
            System.exit(1);
        }
        Bridge bridge = new Bridge(args);
        bridge.mainLoop();
    }
}
