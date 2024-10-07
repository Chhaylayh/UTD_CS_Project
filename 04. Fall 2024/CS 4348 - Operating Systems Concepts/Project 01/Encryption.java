import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Encryption {
    private static String passkey;

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command.toUpperCase()) {
                    case "PASSKEY":
                        passkey = argument;
                        System.out.println("RESULT");
                        break;
                    case "ENCRYPT":
                        if (passkey == null) {
                            System.out.println("ERROR Password not set");
                        } else {
                            System.out.println("RESULT " + encrypt(argument, passkey));
                        }
                        break;
                    case "DECRYPT":
                        if (passkey == null) {
                            System.out.println("ERROR Password not set");
                        } else {
                            System.out.println("RESULT " + decrypt(argument, passkey));
                        }
                        break;
                    case "QUIT":
                        return;
                    default:
                        System.out.println("ERROR Invalid command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = (char) (((text.charAt(i) + key.charAt(i % key.length())) % 26) + 'A');
            result.append(c);
        }
        return result.toString();
    }

    private static String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = (char) (((text.charAt(i) - key.charAt(i % key.length()) + 26) % 26) + 'A');
            result.append(c);
        }
        return result.toString();
    }
}