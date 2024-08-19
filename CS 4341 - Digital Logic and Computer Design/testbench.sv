// Testbench
module test;

  reg a, b, c, y;
  
  // Instantiate design under test
  example EXAMPLE (.a(a), .b(b), .c(c), .y(y));

  initial begin
    // Dump waves
    $dumpfile("dump.vcd");
    $dumpvars(1);
    
    $display("Start");
    {a,b,c} = 3'b000;
    display;
    {a,b,c} = 3'b001;
    display;
    {a,b,c} = 3'b010;
    display;
    {a,b,c} = 3'b011;
    display;
    {a,b,c} = 3'b100;
    display;
    {a,b,c} = 3'b101;
    display;
    {a,b,c} = 3'b110;
    display;
    {a,b,c} = 3'b111;
    display;
  end
  
   task display;
     #1 $display("a:%0b,b:%0b,c:%0b,y:%0b",
      a,b,c,y);
  endtask
  
endmodule