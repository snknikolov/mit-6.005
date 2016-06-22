# MIT 6.005 (Elements of Software Construction)
My solutions to [MIT's 6.005](http://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-005-elements-of-software-construction-fall-2011/index.htm) assignments.

##### Pi poetry :heavy_check_mark:
Find English words in the digits of Pi.

##### Midi piano :heavy_check_mark:
Implement a simple midi keyboard - play notes, change instruments, change octaves and record/play recording.

##### Calculator parser :heavy_check_mark:
Implement a calculator parser.  
1) Define a grammar:  
 Symbols:  
 ```
 +, -, *, /  
 scalar, in (inches), pt (point)  
 ( )  
 ```
    
 Grammar: 
 ```
 calculator ::= (regular | parenthesised)+  
 parenthesised = OPEN_P (regular | parenthesised)+ CLOSE_P  
 regular ::= (unit operator unit)+  
 unit ::= scalar | IN | PT  
 operator ::= PLUS | MINUS | MUL | DIV  
 scalar ::= [\d]  
 ```
 2) Implement the lexer to tokenize the input.  
 3) Implement the parser to evaluate the expressions.  
 4) Put it all together.  


##### Build a Sudoku solver with SAT :heavy_check_mark:
Implement the DPLL algorithm and use it to solve sudoku puzzles.

##### Finding prime factors with networking :heavy_check_mark:
Make a server to do prime factor searching. Create a client that makes requests to servers it is connected to. The client's connected servers must perform its operations concurrently.

##### Multiplayer minesweeper :heavy_check_mark:
Implement a server and thread-safe data structure for playing a multiplayer variant of "Minesweeper".

##### Jotto client GUI