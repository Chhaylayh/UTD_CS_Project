import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    
    private static List<String> history = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DriverProgram <logFileName>");
            return;
        }

        String logFileName = args[0];
        try {
            Process loggerProcess = new ProcessBuilder("java", "Logger", logFileName).start();
            Process encryptionProcess = new ProcessBuilder("java", "EncryptionProgram").start();

            BufferedWriter loggerWriter = new BufferedWriter(new OutputStreamWriter(loggerProcess.getOutputStream()));
            BufferedWriter encryptionWriter = new BufferedWriter(new OutputStreamWriter(encryptionProcess.getOutputStream()));
            BufferedReader encryptionReader = new BufferedReader(new InputStreamReader(encryptionProcess.getInputStream()));

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String command;
            while (true) {
                System.out.println("Enter command (password, encrypt, decrypt, history, quit): ");
                command = userInput.readLine().trim().toLowerCase();

                if ("quit".equals(command)) {
                    sendToProcess(loggerWriter, "QUIT");
                    sendToProcess(encryptionWriter, "QUIT");
                    break;
                } else if ("password".equals(command)) {
                    System.out.println("Enter passkey:");
                    String passkey = userInput.readLine();
                    sendToProcess(encryptionWriter, "PASSKEY " + passkey);
                } else if ("encrypt".equals(command)) {
                    System.out.println("Enter text to encrypt:");
                    String text = userInput.readLine();
                    history.add(text);
                    sendToProcess(encryptionWriter, "ENCRYPT " + text);
                    System.out.println(readFromProcess(encryptionReader));
                } else if ("decrypt".equals(command)) {
                    System.out.println("Enter text to decrypt:");
                    String text = userInput.readLine();
                    history.add(text);
                    sendToProcess(encryptionWriter, "DECRYPT " + text);
                    System.out.println(readFromProcess(encryptionReader));
                } else if ("history".equals(command)) {
                    showHistory();
                } else {
                    System.out.println("Unknown command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendToProcess(BufferedWriter writer, String message) throws IOException {
        writer.write(message + "\n");
        writer.flush();
    }

    private static String readFromProcess(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private static void showHistory() {
        System.out.println("History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, history.get(i));
        }
    }
}
