// Design

module oai23 (a0, a1, b0, b1, b2,out);
  input      a0;
  input      a1;
  input      b0;
  input 	 b1; 
  input 	 b2;
  output     out;

  assign out = ~((a0 | a1) & (b0 | b1 | b2));

endmodule