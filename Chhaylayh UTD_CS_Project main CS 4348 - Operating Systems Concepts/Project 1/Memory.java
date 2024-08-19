import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory {
    
	// Array of integers for memory
	static int[] memory;

    public static void main(String[] args) {

		// If there are no input, exit
        if (args.length < 1) {
            System.err.println("Not Enough Arguments");
            System.exit(1);
        }
        
        String inputPath = args[0];
        
        try {
            initializeMemory(inputPath);
        } 
        catch (FileNotFoundException exp) {
            System.err.println("Input File Not Found");
            System.exit(1);
        }

		// Process input from the parents process
        Scanner input = new Scanner(System.in);
       
        while (input.hasNextLine()) {
            String line = input.nextLine();
            char command = line.charAt(0);
            int address, data;
            
            switch (command) {
                case 'r': // Read Command
                    address = Integer.parseInt(line.substring(1));
                    System.out.println(read(address));
                    break;
               
                case 'w': // Write Command
                    String[] params = line.substring(1).split(",");
                    address = Integer.parseInt(params[0]);
                    data = Integer.parseInt(params[1]);
                    write(address, data);
                    break;
                
                case 'e': // End Process Command
                    System.exit(0);
            }
        }
        
        input.close();
    }

	// Allocates memory and loads the program from file
    private static void initializeMemory(String inputFilePath) throws FileNotFoundException {
        
		memory = new int[2000];
        Scanner scan = new Scanner(new File(inputFilePath));
        int memIndex = 0;
        
        while (scan.hasNextLine()) {
            
			String line = scan.nextLine().trim();
            
			// If empty, skip
            if (line.length() < 1)
                continue;
            
			// For line starting with ".", move to the position in the memory
            if (line.charAt(0) == '.') {
                memIndex = Integer.parseInt(line.substring(1).split("\\s+")[0]);
                continue;
            }
            
			// If first part is not a number, skip
            if (line.charAt(0) < '0' || line.charAt(0) > '9') 
                continue;
            
			// Split by whitespace to first number
            String[] split = line.split("\\s+");
           
            // If empty, skip
			if (split.length < 1)
                continue;
            else	// Read the first integer into memory
                memory[memIndex++] = Integer.parseInt(split[0]);
        }
        scan.close();
    }

	// Log Class
    private static void log(String str) {
        
    }

	// Reads address
    private static int read(int address) {
        log("Reading " + address + " = " + memory[address]);
        return memory[address];
    }

	// Writes data
    private static void write(int address, int data) {
        log("Writing " + data + " to " + address);
        memory[address] = data;
    }
}
