import java.io.*;
import java.util.*;

public class Memory {
    static int[] memory;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Not Enough Arguments");
            System.exit(1);
        }
        
        String inputPath = args[0];
        
        try {
            initializeMemory(inputPath);
        } 
        catch (FileNotFoundException exp) {
            System.err.println("Input File Not Found.");
            System.exit(1);
        }

        Scanner input = new Scanner(System.in);
       
        while (input.hasNextLine()) {
            String line = input.nextLine();
            char command = line.charAt(0);
            int address, data;
            
            switch (command) {
                case 'r': // read command
                    address = Integer.parseInt(line.substring(1));
                    System.out.println(read(address));
                    break;
               
                case 'w': // write command
                    String[] params = line.substring(1).split(",");
                    address = Integer.parseInt(params[0]);
                    data = Integer.parseInt(params[1]);
                    write(address, data);
                    break;
                
                case 'e': // end process command
                    System.exit(0);
            }
        }
        
        input.close();
    }

    private static void initializeMemory(String inputFilePath) throws FileNotFoundException {
        memory = new int[2000];
        Scanner scan = new Scanner(new File(inputFilePath));
        int memIndex = 0;
        
        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            
            if (line.length() < 1) // Empty line, skip it.
                continue;
            
// For lines like ".1000", move to that position in memory
            if (line.charAt(0) == '.') {
                memIndex = Integer.parseInt(line.substring(1).split("\\s+")[0]);
                continue;
            }
            
            if (line.charAt(0) < '0' || line.charAt(0) > '9') 
                continue;
            
            // Split by whitespace to get first number
            String[] split = line.split("\\s+");
           
            if (split.length < 1) // An empty line, skip it
                continue;
            else // Read the first integer into memory
                memory[memIndex++] = Integer.parseInt(split[0]);
        }
        scan.close();
    }

    private static void log(String str) {
        
    }

    private static int read(int address) {
        log("Reading " + address + " = " + memory[address]);
        return memory[address];
    }

    private static void write(int address, int data) {
        log("Writing " + data + " to " + address);
        memory[address] = data;
    }
}
