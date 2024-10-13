.data
message1: .asciiz "Pick a cell to place your move in: "
message2: .asciiz "Computer's move: "
message3: .asciiz "You are the winner!"
message4: .asciiz "\nComputer is the winner!"
message5: .asciiz "The game is a tie!"
message6: .asciiz "Choose which character you want to be (X or O): "
row:	  .asciiz "\n------------------\n"
disRow:   .asciiz "\n-----------------\n"
row1:	  .asciiz "  1  |  2  |  3  "
row2:	  .asciiz "  4  |  5  |  6  "
row3:	  .asciiz "  7  |  8  |  9  \n"
divider:  .asciiz "   |  "
n1:       .asciiz " \n"
array:    .word 0:8

.text 

# Displaying the game board
li $v0, 4
la $a0, n1
syscall

li $v0, 4
la $a0, row1
syscall
		
li $v0, 4
la $a0, disRow
syscall

li $v0, 4
la $a0, row2
syscall

li $v0, 4
la $a0, disRow
syscall	
		
li $v0, 4
la $a0, row3
syscall

li $v0, 4
la $a0, n1
syscall 

# Prompting user for which character they would like to be
li $v0,4
la $a0, message6
syscall

# Reading the user’s character
li $v0,12
syscall

# Storing the user’s character into a register
move $s0,$v0

# If user's character is X then computer's move is O otherwise set computer’s move to X
beq $s0,'X',comp
li $s1, 'X'
j continue

comp:
li $s1,'O'

continue:
# Setting an indicator to determine who's turn it is
li $t7,0

#counter for total amount of moves
li $t8,0

turn:
# call board display function
jal boardDisplay

#once total moves counter hits 9, prints tie game message and exits
bge $t8, 9, tie_game

# Branch to computer's move if the indicator is 1
beq $t7,1,compMove

userMove:
# Prompting user for their move
li $v0, 4
la $a0, n1
syscall

li $v0,4
la $a0,message1
syscall

# Reading the cell number from user input
li $v0,5
syscall

# Storing the cell number into a register
move $t0,$v0

# check if $t0 < 1  or $t0 > 9 and if true, branch to userMove
blt $t0,1,userMove
bgt $t0,9,userMove

# Decrement by 1 to get the next index 
addi $t0,$t0,-1

# Multiply to get the next cell number
mul $t0,$t0,4 

# Loading address of the array into a register
la $t1,array

# Getting the memory address
add $t3,$t0,$t1

# Get byte at ($t3) and store into $t0
lb $t0,($t3)

# Checking if move has been made in this location
# check if $t0 != 0 and if true, branch to userMove
bne $t0,0,userMove

# Storing the address into a register
sb $s0, ($t3)

#adds to the counter for total number of moves
addi $t8, $t8, 1

# Checking the status to see if there's three in a row of a character vertically, horizontally, or diagonally
la $s3, array
jal checkRow1_X
jal checkRow2_X
jal checkRow3_X
jal checkColumn1_X
jal checkColumn2_X
jal checkColumn3_X
jal checkDiagonal1_X
jal checkDiagonal2_X

jal checkRow1_O
jal checkRow2_O
jal checkRow3_O
jal checkColumn1_O
jal checkColumn2_O
jal checkColumn3_O
jal checkDiagonal1_O
jal checkDiagonal2_O

# Setting $t7 to 1 which indicates it's the computer's turn to move
li $t7,1
j turn

compMove:
# Creating the random number generator and setting the upper bound limit
li $v0,42
li $a0,0

li $a1,8
syscall

# Setting the index for the computer
addi $a0,$a0,1

# Storing that index into a register
move $t0,$a0

# Check if computer's move is in range and if not then reprompt
blt $t0,1,compMove
bgt $t0,9,compMove

li $v0, 4
la $a0, n1
syscall

# Displaying the computer's move
li $v0,4
la $a0,message2
syscall

# Displaying the random number that the computer selected
li $v0,1
move $a0,$t0
syscall

# Decrementing the index by 1 each time
addi $t0,$t0,-1

# Multiply to get the next cell number
mul $t0,$t0,4 

# Loading address of the array into a register
la $t1,array

# Getting the memory address
add $t3,$t0,$t1

# Get byte at ($t3) and store into $t0
lb $t0,($t3)

# Checking if move has been made in this location
# check if $t0 != 0 and if true, branch to compMove
bne $t0,0,compMove

# Storing the address into a register
sb $s1, ($t3)

#adds to the counter for total number of moves
addi $t8, $t8, 1

# Checking the status to see if there's three in a row of a character vertically, horizontally, or diagonally
la $s3, array
jal checkRow1_X_comp
jal checkRow2_X_comp
jal checkRow3_X_comp
jal checkColumn1_X_comp
jal checkColumn2_X_comp
jal checkColumn3_X_comp
jal checkDiagonal1_X_comp
jal checkDiagonal2_X_comp

jal checkRow1_O_comp
jal checkRow2_O_comp
jal checkRow3_O_comp
jal checkColumn1_O_comp
jal checkColumn2_O_comp
jal checkColumn3_O_comp
jal checkDiagonal1_O_comp
jal checkDiagonal2_O_comp

# Setting $t7 to 0 which indicates it's the user's turn to move
li $t7,0
j turn

boardDisplay:
# Setting a counter and a check
li $t0,9
li $t1,0

# Loading 3 elements each time on the board
li $t4,3
li $v0, 4
la $a0, n1
syscall

Loop:
# Getting the memory location
sll $t1,$t1,2

# Load the address of the array into a register
lb $a0,array($t1)

# Move the counter back to its default state
srl $t1,$t1,2

# Check if $a0 == 0 and if it does, set $a0 = 32 to create a space
bne $a0,0,isXorO
li $a0,32
isXorO:
li $v0,11
syscall

# Increment to get to the next index
addi $t1,$t1,1

# Divide $t1 by $t4 and store the remainder into a register
div $t1, $t4
mfhi $t2

# If remainder is equal to 0 then branch to Else
bne $t2,0,Else

# Display the row then jump to skipDivider
li $v0, 4
la $a0, disRow
syscall	
j skipDivider

# Place a divider on the board
Else:
li $v0,4
la $a0,divider
syscall

# If counter is less than number of elements, branch to Loop
# Otherwise, jump back to return address
skipDivider:
blt $t1,$t0,Loop
jr $ra

# Display the tie game message
tie_game:
li $v0, 4          
la $a0, message5    
syscall            

# row 1 for X for user
checkRow1_X:
lw $t0, 0($s3)
lw $t1, 4($s3)
lw $t2, 8($s3)
beq $t0, 'X', checkRow1_2_X
jr $ra
checkRow1_2_X:
beq $t1, 'X', checkRow1_3_X
jr $ra
checkRow1_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# row 2 for X for user
checkRow2_X:
lw $t0, 12($s3)
lw $t1, 16($s3)
lw $t2, 20($s3)
beq $t0, 'X', checkRow2_2_X
jr $ra
checkRow2_2_X:
beq $t1, 'X', checkRow2_3_X
jr $ra
checkRow2_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# row 3 for X for user
checkRow3_X: 
lw $t0, 24($s3)
lw $t1, 28($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkRow3_2_X
jr $ra
checkRow3_2_X:
beq $t1, 'X', checkRow3_3_X
jr $ra
checkRow3_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# column 1 for X for user
checkColumn1_X:
lw $t0, 0($s3)
lw $t1, 12($s3)
lw $t2, 24($s3)
beq $t0, 'X', checkColumn1_2_X
jr $ra
checkColumn1_2_X:
beq $t1, 'X', checkColumn1_3_X
jr $ra
checkColumn1_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# column 2 for X for user
checkColumn2_X:
lw $t0, 4($s3)
lw $t1, 16($s3)
lw $t2, 28($s3)
beq $t0, 'X', checkColumn2_2_X
jr $ra
checkColumn2_2_X:
beq $t1, 'X', checkColumn2_3_X
jr $ra
checkColumn2_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# column 3 for X for user
checkColumn3_X:
lw $t0, 8($s3)
lw $t1, 20($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkColumn3_2_X
jr $ra
checkColumn3_2_X:
beq $t1, 'X', checkColumn3_3_X
jr $ra
checkColumn3_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# diagonal1 for X for user
checkDiagonal1_X:
lw $t0, 0($s3)
lw $t1, 16($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkDiagonal1_2_X
jr $ra
checkDiagonal1_2_X:
beq $t1, 'X', checkDiagonal1_3_X
jr $ra
checkDiagonal1_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# diagonal2 for X for user
checkDiagonal2_X:
lw $t0, 8($s3)
lw $t1, 16($s3)
lw $t2, 24($s3)
beq $t0, 'X', checkDiagonal2_2_X
jr $ra
checkDiagonal2_2_X:
beq $t1, 'X', checkDiagonal2_3_X
jr $ra
checkDiagonal2_3_X:
beq $t2, 'X', checkWinUser
jr $ra

# row 1 for O for user
checkRow1_O:
lw $t0, 0($s3)
lw $t1, 4($s3)
lw $t2, 8($s3)
beq $t0, 'O', checkRow1_2_O
jr $ra
checkRow1_2_O:
beq $t1, 'O', checkRow1_3_O
jr $ra
checkRow1_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# row 2 for O for user
checkRow2_O:
lw $t0, 12($s3)
lw $t1, 16($s3)
lw $t2, 20($s3)
beq $t0, 'O', checkRow2_2_O
jr $ra
checkRow2_2_O:
beq $t1, 'O', checkRow2_3_O
jr $ra
checkRow2_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# row 3 for O for user
checkRow3_O:
lw $t0, 24($s3)
lw $t1, 28($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkRow3_2_O
jr $ra
checkRow3_2_O:
beq $t1, 'O', checkRow3_3_O
jr $ra
checkRow3_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# column 1 for O for user
checkColumn1_O:
lw $t0, 0($s3)
lw $t1, 12($s3)
lw $t2, 24($s3)
beq $t0, 'O', checkColumn1_2_O
jr $ra
checkColumn1_2_O:
beq $t1, 'O', checkColumn1_3_O
jr $ra
checkColumn1_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# column 2 for O for user
checkColumn2_O:
lw $t0, 4($s3)
lw $t1, 16($s3)
lw $t2, 28($s3)
beq $t0, 'O', checkColumn2_2_O
jr $ra
checkColumn2_2_O:
beq $t1, 'O', checkColumn2_3_O
jr $ra
checkColumn2_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# column 3 for O for user
checkColumn3_O:
lw $t0, 8($s3)
lw $t1, 20($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkColumn3_2_O
jr $ra
checkColumn3_2_O:
beq $t1, 'O', checkColumn3_3_O
jr $ra
checkColumn3_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# diagonal1 for O for user
checkDiagonal1_O:
lw $t0, 0($s3)
lw $t1, 16($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkDiagonal1_2_O
jr $ra
checkDiagonal1_2_O:
beq $t1, 'O', checkDiagonal1_3_O
jr $ra
checkDiagonal1_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# diagonal2 for O for user
checkDiagonal2_O:
lw $t0, 8($s3)
lw $t1, 16($s3)
lw $t2, 24($s3)
beq $t0, 'O', checkDiagonal2_2_O
jr $ra
checkDiagonal2_2_O:
beq $t1, 'O', checkDiagonal2_3_O
jr $ra
checkDiagonal2_3_O:
beq $t2, 'O', checkWinUser
jr $ra

# row 1 for X for comp
checkRow1_X_comp:
lw $t0, 0($s3)
lw $t1, 4($s3)
lw $t2, 8($s3)
beq $t0, 'X', checkRow1_2_X_comp
jr $ra
checkRow1_2_X_comp:
beq $t1, 'X', checkRow1_3_X_comp
jr $ra
checkRow1_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# row 2 for X for comp
checkRow2_X_comp:
lw $t0, 12($s3)
lw $t1, 16($s3)
lw $t2, 20($s3)
beq $t0, 'X', checkRow2_2_X_comp
jr $ra
checkRow2_2_X_comp:
beq $t1, 'X', checkRow2_3_X_comp
jr $ra
checkRow2_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# row 3 for X for comp
checkRow3_X_comp:
lw $t0, 24($s3)
lw $t1, 28($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkRow3_2_X_comp
jr $ra
checkRow3_2_X_comp:
beq $t1, 'X', checkRow3_3_X_comp
jr $ra
checkRow3_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# column 1 for X for comp
checkColumn1_X_comp:
lw $t0, 0($s3)
lw $t1, 12($s3)
lw $t2, 24($s3)
beq $t0, 'X', checkColumn1_2_X_comp
jr $ra
checkColumn1_2_X_comp:
beq $t1, 'X', checkColumn1_3_X_comp
jr $ra
checkColumn1_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# column 2 for X for comp
checkColumn2_X_comp:
lw $t0, 4($s3)
lw $t1, 16($s3)
lw $t2, 28($s3)
beq $t0, 'X', checkColumn2_2_X_comp
jr $ra
checkColumn2_2_X_comp:
beq $t1, 'X', checkColumn2_3_X_comp
jr $ra
checkColumn2_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# column 3 for X for comp
checkColumn3_X_comp:
lw $t0, 8($s3)
lw $t1, 20($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkColumn3_2_X_comp
jr $ra
checkColumn3_2_X_comp:
beq $t1, 'X', checkColumn3_3_X_comp
jr $ra
checkColumn3_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# diagonal1 for X for comp
checkDiagonal1_X_comp:
lw $t0, 0($s3)
lw $t1, 16($s3)
lw $t2, 32($s3)
beq $t0, 'X', checkDiagonal1_2_X_comp
jr $ra
checkDiagonal1_2_X_comp:
beq $t1, 'X', checkDiagonal1_3_X_comp
jr $ra
checkDiagonal1_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# diagonal2 for X for comp
checkDiagonal2_X_comp:
lw $t0, 8($s3)
lw $t1, 16($s3)
lw $t2, 24($s3)
beq $t0, 'X', checkDiagonal2_2_X_comp
jr $ra
checkDiagonal2_2_X_comp:
beq $t1, 'X', checkDiagonal2_3_X_comp
jr $ra
checkDiagonal2_3_X_comp:
beq $t2, 'X', checkWinComp
jr $ra

# row 1 for O for comp
checkRow1_O_comp:
lw $t0, 0($s3)
lw $t1, 4($s3)
lw $t2, 8($s3)
beq $t0, 'O', checkRow1_2_O_comp
jr $ra
checkRow1_2_O_comp:
beq $t1, 'O', checkRow1_3_O_comp
jr $ra
checkRow1_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# row 2 for O for comp
checkRow2_O_comp:
lw $t0, 12($s3)
lw $t1, 16($s3)
lw $t2, 20($s3)
beq $t0, 'O', checkRow2_2_O_comp
jr $ra
checkRow2_2_O_comp:
beq $t1, 'O', checkRow2_3_O_comp
jr $ra
checkRow2_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# row 3 for O for comp
checkRow3_O_comp:
lw $t0, 24($s3)
lw $t1, 28($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkRow3_2_O_comp
jr $ra
checkRow3_2_O_comp:
beq $t1, 'O', checkRow3_3_O_comp
jr $ra
checkRow3_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# column 1 for O for comp
checkColumn1_O_comp:
lw $t0, 0($s3)
lw $t1, 12($s3)
lw $t2, 24($s3)
beq $t0, 'O', checkColumn1_2_O_comp
jr $ra
checkColumn1_2_O_comp:
beq $t1, 'O', checkColumn1_3_O_comp
jr $ra
checkColumn1_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# column 2 for O for comp
checkColumn2_O_comp:
lw $t0, 4($s3)
lw $t1, 16($s3)
lw $t2, 28($s3)
beq $t0, 'O', checkColumn2_2_O_comp
jr $ra
checkColumn2_2_O_comp:
beq $t1, 'O', checkColumn2_3_O_comp
jr $ra
checkColumn2_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# column 3 for O for comp
checkColumn3_O_comp:
lw $t0, 8($s3)
lw $t1, 20($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkColumn3_2_O_comp
jr $ra
checkColumn3_2_O_comp:
beq $t1, 'O', checkColumn3_3_O_comp
jr $ra
checkColumn3_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# diagonal1 for O for comp
checkDiagonal1_O_comp:
lw $t0, 0($s3)
lw $t1, 16($s3)
lw $t2, 32($s3)
beq $t0, 'O', checkDiagonal1_2_O_comp
jr $ra
checkDiagonal1_2_O_comp:
beq $t1, 'O', checkDiagonal1_3_O_comp
jr $ra
checkDiagonal1_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# diagonal2 for O for comp
checkDiagonal2_O_comp:
lw $t0, 8($s3)
lw $t1, 16($s3)
lw $t2, 24($s3)
beq $t0, 'O', checkDiagonal2_2_O_comp
jr $ra
checkDiagonal2_2_O_comp:
beq $t1, 'O', checkDiagonal2_3_O_comp
jr $ra
checkDiagonal2_3_O_comp:
beq $t2, 'O', checkWinComp
jr $ra

# display winner for user
checkWinUser:
beq $t0, 'X', turnEndUser
beq $t0, 'O', turnEndUser
j end

# display winner for comp
checkWinComp:
beq $t0, 'X', turnEndComp
beq $t0, 'O', turnEndComp
j end

# prints the final board for user winning move and ends game
turnEndUser:

# call board display function
jal boardDisplay

#displays user win
j userWin

# prints the final board for computer winning move and ends game
turnEndComp:

# call board display function
jal boardDisplay

#displays user win
j compWin

# Displaying the message that user is the winner
userWin:
li $v0, 4
la $a0, message3
syscall
j end

# Displaying the message that computer is the winner
compWin:
li $v0, 4
la $a0, message4
syscall
j end

# Displaying the message that the game is a tie
tie:
li $v0, 4
la $a0, message5
syscall
j end

# Terminate the program
end:
li $v0, 10    
syscall
