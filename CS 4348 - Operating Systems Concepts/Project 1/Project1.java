import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Project1 {
    
    public static void main(String[] args) {
        
        if (args.length < 2) {    
            System.err.println("Not Enough Arguments");
            System.exit(1);
        }
        
        String inputProgram = args[0];
		int timeout = Integer.parseInt(args[1]);
		Runtime runtime = Runtime.getRuntime();
       
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
            System.err.println("Unable To Create New Process.");
            System.exit(1);
        }
    }

    private static void log(Object... str) {
        
    }

    private static class CPU {
        private int PC, SP, IR, AC, X, Y, timer, timeout;
        private boolean kernelMode;
        private Scanner memoryIn;
        private PrintWriter memoryOut;

        public CPU(Scanner memoryIn, PrintWriter memoryOut, int timeout) {
            this.memoryIn = memoryIn;
            this.memoryOut = memoryOut;
            this.timeout = timeout;
            kernelMode = false;
            PC = IR = AC = X = Y = timer = 0;
            SP = 1000;
        }
        
        private int readMemory(int address) {
            
            if (address >= 1000 && !kernelMode) {
                System.err.println("Memory Violation: Accessing System Address 1000 In User Mode");
                System.exit(-1);
            }
            
            memoryOut.println("r" + address);
            memoryOut.flush();
            
            return Integer.parseInt(memoryIn.nextLine());
        }

        private void writeMemory(int address, int data) {
            memoryOut.printf("w%d,%d\n", address, data);
            memoryOut.flush();
        }

        private void endMemoryProcess() {
            memoryOut.println("e");
            memoryOut.flush();
        }
        
        private void fetch() {
            IR = readMemory(PC++);
        }
        
        private void push(int data) {
            writeMemory(--SP, data);
        }
        
        private int pop() {
            return readMemory(SP++);
        }

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

        private boolean execute() {
            
            switch (IR) {
                
                case 1: // Load value into AC
                    fetch();
                    log("Loading " + IR + " Into AC");
                    AC = IR;
                    break;
                
                case 2: // Load Value At Address Into AC
                    fetch();
                    AC = readMemory(IR);
                    log("Loading From Address " + IR + " Into AC: " + AC);
                    break;
                
                case 3: // LOad Value From Address At Given Address Into AC
                    fetch();
                    AC = readMemory(readMemory(IR));
                    log("Loading Indirectly From Address" + IR + " Into AC: " + AC);
                    break;
                
                case 4: // Load Value At (Given Address + X) Into AC
                    fetch();
                    AC = readMemory(IR + X);
                    log("LoadInxX", IR, X, "->", AC);

                    break;
                
                case 5: // Load Value At (Given Address + Y) Into AC
                    fetch();
                    AC = readMemory(IR + Y);
                    log("LoadInxY", IR, Y, "->", AC);
                    break;
                
                case 6: // Load from (SP+X) Into AC
                    AC = readMemory(SP + X);
                    log("LoadSpX", SP, X, "->", AC);
                    break;
                
                case 7: // Store AC Ro address
                    fetch();
                    writeMemory(IR, AC);
                    log("Store", IR, AC);
                    break;
                
                case 8: // Get Random int 1-100 Rnto AC
                    AC = (int) (Math.random() * 100 + 1);
                    log("Get", AC);
                    break;
                
                case 9: // If port=1, Write AC To Screen As int, If port=2, Write AC To Screen As char
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
                
                case 10: // Add X To AC
                    AC += X;
                    break;
                
                case 11: // Add Y To AC
                    AC += Y;
                    break;
                
                case 12: // Sub X To AC
                    AC -= X;
                    break;
                
                case 13: // Sub Y To AC
                    AC -= Y;
                    break;
                
                case 14: // Copy Value In AC To X
                    X = AC;
                    break;
                
                case 15: // Copy Value In X To AC
                    AC = X;
                    break;
                
                case 16: // Copy AC To Y
                    Y = AC;
                    break;
                
                case 17: // Copy Y To AC
                    AC = Y;
                    break;
                
                case 18: // Copy AC To SP
                    SP = AC;
                    break;
                
                case 19: // Copy SP To AC
                    AC = SP;
                    break;
                
                case 20: // Jump To Address
                    fetch();
                    PC = IR;
                    break;
               
                case 21: // Jump Only If AC Is Zero
                    fetch();
                    if (AC == 0) 
                        PC = IR;
                    break;
                
                case 22: // Jump Only If AC Is Not Zero
                    fetch();
                    if (AC != 0)
                        PC = IR;
                    break;
               
                case 23: // Push Return Address To Stack, Jump
                    fetch();
                    push(PC);
                    PC = IR;
                    break;
              
                case 24: // Pop Return Address, Jump Back
                    PC = pop();
                    break;
                
                case 25: // Increment X
                    X++;
                    break;
                
                case 26: // Decrement X
                    X--;
                    break;
                
                case 27: // Push AC Onto Stack
                    push(AC);
                    log("Pushing AC", AC);
                    break;
                
                case 28: // Pop From Stack Onto AC
                    AC = pop();
                    log("Popping AC", AC);
                    break;
                
                case 29: // Set System Mode, Switch Stack, Push SP/PC, Set New SP/PC
                    if (!kernelMode) {
                        kernelMode();
                        PC = 1500;
                    }
                    break;
                
                case 30: // Restore Registers, Set User Mode
                    log("Exiting Kernel Mode");
                    Y = pop();
                    X = pop();
                    AC = pop();
                    IR = pop();
                    PC = pop();
                    SP = pop();
                    kernelMode = false;
                    break;
                
                case 50: // End Execution
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