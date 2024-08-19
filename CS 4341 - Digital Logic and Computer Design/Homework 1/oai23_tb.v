// Testbench
module test;

  reg a0, a1, b0, b1, b2, out;
  
  // Instantiate design under test
  oai23 OAI23 (.a0(a0), .a1(a1), .b0(b0), .b1(b1), .b2(b2), .out(out));
          
  initial begin
    // Dump waves
    $dumpfile("dump.vcd");
    $dumpvars(1);
    
    $display("Start");
    {a0, a1, b0, b1, b2} = 5'b0_0000;
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0001;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0010;  
    display; 
    {a0, a1, b0, b1, b2} = 5'b0_0011;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0100;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0101;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0110;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_0111;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1000;
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1001;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1010;  
    display; 
    {a0, a1, b0, b1, b2} = 5'b0_1011;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1100;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1101;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1110;    
    display;
    {a0, a1, b0, b1, b2} = 5'b0_1111;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0000;
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0001;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0010;  
    display; 
    {a0, a1, b0, b1, b2} = 5'b1_0011;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0100;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0101;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0110;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_0111;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1000;
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1001;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1010;  
    display; 
    {a0, a1, b0, b1, b2} = 5'b1_1011;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1100;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1101;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1110;    
    display;
    {a0, a1, b0, b1, b2} = 5'b1_1111;    
    display;
  end
  
   task display;
     #1 $display("a0:%0b,a1:%0b,b0:%0b,b1:%0b,b2:%0b, out:%b",
      a0, a1, b0, b1, b2, out);
  endtask
  
endmodule