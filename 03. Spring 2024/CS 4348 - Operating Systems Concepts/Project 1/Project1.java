import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Project1 {
    
	public static void main(String[] args) {
        
		if (args.length < 2) {    
            System.err.println("Not enough arguments");
            System.exit(1);
        }
        
		// Get the input program arguments
        String inputProgram = args[0];
		int timeout = Integer.parseInt(args[1]);
		
		Runtime runtime = Runtime.getRuntime();
		
		// Call memory process with the input program arguments
		try {
			Process memory = runtime.exec("java Memory " + inputProgram);
			final InputStream error = memory.getErrorStream();
            
            new Thread(new Runnable() {
                
                public void run() {
					byte[] buffer = new byte[8192];
					int length = -1;
                    
                    try {
						while ((length = error.read(buffer)) > 0)
                            System.err.write(buffer, 0, length);
						
                    } 
					catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }
            } ).start();

            Scanner memoryIn = new Scanner(memory.getInputStream());
			PrintWriter memoryOut = new PrintWriter(memory.getOutputStream());
			CPU cpu = new CPU(memoryIn, memoryOut, timeout);
			cpu.run();
		} 
		catch (IOException exp) {
            exp.printStackTrace();
            System.err.println("Unable to create new process.");
            System.exit(1);
        }
    }

	// Log Class
    private static void log(Object... str) {
        
    }

	// CPU Class
    private static class CPU {
        private int PC, SP, IR, AC, X, Y, timer, timeout;
        private boolean kernelMode;
        private Scanner memoryIn;
        private PrintWriter memoryOut;

		// CPU Constructor
        public CPU(Scanner memoryIn, PrintWriter memoryOut, int timeout) {
            this.memoryIn = memoryIn;
            this.memoryOut = memoryOut;
            this.timeout = timeout;
            kernelMode = false;
            PC = IR = AC = X = Y = timer = 0;
            SP = 1000;
        }
        
		// Reads from memory
        private int readMemory(int address) {
            
            if (address >= 1000 && !kernelMode) {
                System.err.println("Memory Violation: Accessing System address 1000 in User Mode");
                System.exit(-1);
            }
            
            memoryOut.println("r" + address);
            memoryOut.flush();
            
            return Integer.parseInt(memoryIn.nextLine());
        }

		// Writes to memory
        private void writeMemory(int address, int data) {
            memoryOut.printf("w%d,%d\n", address, data);
            memoryOut.flush();
        }

		// Ends memory process
        private void endMemoryProcess() {
            memoryOut.println("e");
            memoryOut.flush();
        }
        
		// Fetches instruction from PC
        private void fetch() {
            IR = readMemory(PC++);
        }
        
		// Pushes data to stack
        private void push(int data) {
            writeMemory(--SP, data);
        }
        
		// Pops data from stack
        private int pop() {
            return readMemory(SP++);
        }

        // Runs the fetch-execution cycle until command end
		public void run() {
            
			boolean running = true;
            
            while (running) {
                fetch();
                running = execute();
                timer++;
                
                if (timer >= timeout) {
                    
                    if (!kernelMode) {
                        timer = 0;
                        kernelMode();
                        PC = 1000;
                    }
                }
            }
        }

		// Enters Keral Mode
        private void kernelMode() {
            log("Entering Kernel Mode");
            kernelMode = true;
            int tempSP = SP;
            SP = 2000;
            push(tempSP);
            push(PC);
            push(IR);
            push(AC);
            push(X);
            push(Y);
        }
		
		// Instruction
        private boolean execute() {
            
            switch (IR) {
                
                case 1: // Load value: Load the value into the AC
                    fetch();
                    log("Loading " + IR + " into AC");
                    AC = IR;
                    break;
                
                case 2: // Load addr: Load the value at the address into the AC
                    fetch();
                    AC = readMemory(IR);
                    log("Loading from address " + IR + " into AC: " + AC);
                    break;
                
                case 3: // LoadInd addr: Load the value from the address found in the given address into the AC
                    fetch();
                    AC = readMemory(readMemory(IR));
                    log("Loading indirectly from address" + IR + " into AC: " + AC);
                    break;
                
                case 4: // LoadIdxX addr: Load the value at (address+X) into the AC
                    fetch();
                    AC = readMemory(IR + X);
                    log("LoadInxX", IR, X, "->", AC);

                    break;
                
                case 5: // LoadIdxY addr: Load the value at (address+Y) into the AC
                    fetch();
                    AC = readMemory(IR + Y);
                    log("LoadInxY", IR, Y, "->", AC);
                    break;
                
                case 6: // LoadSpX: Load from (Sp+X) into the AC 
                    AC = readMemory(SP + X);
                    log("LoadSpX", SP, X, "->", AC);
                    break;
                
                case 7: // Store addr: Store the value in the AC into the address
                    fetch();
                    writeMemory(IR, AC);
                    log("Store", IR, AC);
                    break;
                
                case 8: // Get: Gets a random int from 1 to 100 into the AC
                    AC = (int) (Math.random() * 100 + 1);
                    log("Get", AC);
                    break;
                
                case 9: // Put port:	If port=1, writes AC as an int to the screen
						//				If port=2, writes AC as a char to the screen
                    fetch();
                    
                    if (IR == 1) {
                        System.out.print(AC);
                        log("Put", "int", AC);
                    } 
                    else if (IR == 2) {
                        System.out.print((char) AC);
                        log("Put", "char", (char) AC);
                    }
                    break;
                
                case 10: // AddX: Add the value in X to the AC
                    AC += X;
                    break;
                
                case 11: // AddY: Add the value in Y to the AC
                    AC += Y;
                    break;
                
                case 12: // SubX: Subtract the value in X from the AC
                    AC -= X;
                    break;
                
                case 13: // SubY: Subtract the value in Y from the AC
                    AC -= Y;
                    break;
                
                case 14: // CopyToX: Copy the value in the AC to X
                    X = AC;
                    break;
                
                case 15: // CopyFromX: Copy the value in X to the AC
                    AC = X;
                    break;
                
                case 16: // CopyToY: Copy the value in the AC to Y
                    Y = AC;
                    break;
                
                case 17: // CopyFromY: Copy the value in Y to the AC
                    AC = Y;
                    break;
                
                case 18: // CopyToSp: Copy the value in AC to the SP
                    SP = AC;
                    break;
                
                case 19: // CopyFromSp: Copy the value in SP to the AC
                    AC = SP;
                    break;
                
                case 20: // Jump addr: Jump to the address
                    fetch();
                    PC = IR;
                    break;
               
                case 21: // JumpIfEqual addr: Jump to the address only if the value in the AC is zero
                    fetch();
                    if (AC == 0) 
                        PC = IR;
                    break;
                
                case 22: // JumpIfNotEqual addr: Jump to the address only if the value in the AC is not zero
                    fetch();
                    if (AC != 0)
                        PC = IR;
                    break;
               
                case 23: // Call addr: Push return address onto stack, jump to the address
                    fetch();
                    push(PC);
                    PC = IR;
                    break;
              
                case 24: // Ret: Pop return address from the stack, jump to the address
                    PC = pop();
                    break;
                
                case 25: // IncX: Increment the value in X
                    X++;
                    break;
                
                case 26: // DecX: Decrement the value in X
                    X--;
                    break;
                
                case 27: // Push: Push AC onto stack
                    push(AC);
                    log("Pushing AC", AC);
                    break;
                
                case 28: // Pop: Pop from stack into AC
                    AC = pop();
                    log("Popping AC", AC);
                    break;
                
                case 29: // Int: Perform system call
                    if (!kernelMode) {
                        kernelMode();
                        PC = 1500;
                    }
                    break;
                
                case 30: // IRet: Return from system call
                    log("Exiting Kernel Mode");
                    Y = pop();
                    X = pop();
                    AC = pop();
                    IR = pop();
                    PC = pop();
                    SP = pop();
                    kernelMode = false;
                    break;
                
                case 50: // End: End execution
                    endMemoryProcess();
                    return false;
                
                default: // Invalid Instruction
                    System.err.println("ERROR: Invalid Instruct");
                    endMemoryProcess();
                    return false;
            }
            return true;
        }
    }
}
