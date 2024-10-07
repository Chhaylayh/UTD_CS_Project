import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    
    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Usage: java Logger <logFileName>");
            return;
        }

        String logFileName = args[0];
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                
                if ("QUIT".equals(line.trim())) {
                    break;
                }
                
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                writer.write(String.format("%s [%s]\n", timestamp, line));
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}