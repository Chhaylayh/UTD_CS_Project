// Design
// D flip-flop
module example(input  a, b, c,
               output y);
  assign y = ~((a & ~b) | (~b & ~c));
endmodule
