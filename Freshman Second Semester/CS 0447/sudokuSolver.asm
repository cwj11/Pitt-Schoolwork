.text
	addi $a2, $0, 0xffff8000
	jal _solveSudoku

	jal _printSudokuGrid

	addi $v0, $0, 10	#Syscall 10: Terminate program
	syscall

#Recursive method to solve the sudoku puzzle
#a0 - the row
#a1 - the column
#a2 - beginning memory address of sudoku puzzle
#first call for row and column should always be zero
#return v0 - true if sudoku puzzle is solved, false otherwise
_solveSudoku:
	addi $sp, $sp, -16	#Allocate activation frame
	sw $s0, 8($sp)
	sw $s1, 4($sp)
	sw $s2, 12($sp)
	sw $ra, 0($sp)
	add $s0, $a0, $0	#s0 is the row
	add $s1, $a1, $0	#s1 is the column
	add $s2, $a2, $0	#s2 is the address
	subi $t8, $s1, 9	#Subtract 9 from s1 to check if its equal to zero
	beqz $t8, maxColumn	#Branch if column is equal to nine
	check:
	lb $t7, ($s2)		#Load current number intoi t7
	bnez $t7, recursiveCall	#Skip if there is already a number in this location
	add $s3, $0, 1		#Set s3 to 1, counter
	
	loop:
	add $a0, $s3, $0	#Move s3 to a0
	add $a1, $s0, $0	#Move s0 to a1
	jal _checkRow		#Call checkRow
	beqz $v0, skipCheck	#Skip the rest of the checks if the number doesn't work
	
	add $a0, $s3, $0	#Move s3 to a0
	add $a1, $s1, $0	#Move s1 to a1
	jal _checkColumn		#Call checkCol
	beqz $v0, skipCheck	#Skip the rest of the checks if the number doesn't work
	
	add $a0, $s3, $0	#Move s3 to a0
	add $a1, $s2, $0	#Move s2 to a1
	addi $t8, $0, 3		#Set t8 to 3
	div $t3, $s1, 3		#Divide the column by 3
	div $s0, $t8		#Divide the row by 3
	mfhi $t5		#Move the remainder to t5
	sub $a1, $s0, $t5	#Subtract the remainder from the row
	add $a1, $a1, $t3	#Put the number of the box to be checked into s2
	jal _checkBox		#Call checkBox
	beqz $v0, skipCheck	#Skip the rest of the checks if the number doesn't work
	
	sb $s3, ($s2)		#Store the value in the sudoku memory location
	
	add $a0, $s0, $0	#Move s0 to a0
	addi $a1, $s1, 1	#Move s1 to a1 and increment by one, col+1
	addi $a2, $s2, 1	#Move s2 to a2 and add one
	jal _solveSudoku	#Make a recursive call
	lb $s3, ($s2)		#Load counter from memory
	bnez $v0, return
skipCheck:
	addi $s3, $s3, 1	#Increase counter by one	
	slti $t8, $s3, 10
	bnez $t8, loop

	sb $0, ($s2)		#Store zero in memory	
returnFalse:
	addi $v0, $0, 0		#Set v0 to 0, false
	j return	
	
	maxColumn:
	subi $t8, $s0, 8	#Subtract 8 from s1 to check if done
	beqz $t8, returnTrue	#Branch to return true if all spots checked
	addi $s0, $s0, 1	#Add one to the row
	add $s1, $0, $0		#Set s1 to zero
	j check			#Jump back after values are corrected
	
	returnTrue:
	addi $v0, $0, 1		#Set v0 to 1, true
	return:
	lw $s2, 12($sp)
	lw $s0, 8($sp)
	lw $s1, 4($sp)
	lw $ra, 0($sp)
	addi $sp, $sp, 16
	jr   $ra
	
recursiveCall:
	add $a0, $s0, $0	#Move s0 to a0
	addi $a1, $s1, 1	#Move s1 to a1 and increment by one, col+1
	addi $a2, $s2, 1	#Move s2 to a2 and add one
	jal _solveSudoku	#Make a recursive call
	beqz $v0, returnFalse	#Return false
	j returnTrue
	
#Method to check if you can add a number to a row
#a0 - the number to be added
#a1 - the number of the row
#return v0 - true if the number can be added, false if it can't
_checkRow:
	add $t0, $a0, $0	#Move the value of a0 to t0
	add $t1, $a1, $0	#Move the value of a1 to t1
	addi $t2, $0, 0xffff8000	#Set t2 to the first memory location
	addi $t8, $0, 9		#Add 9 to t8 to get the needed memory location
	mult $t1, $t8		#Multiply the row by 9
	mflo $t1		#Move the product to t1
	add $t2, $t2, $t1	#Add t1 to t2 to get the starting address of the row
	addi $t3, $0, 0		#Set t3 to 0, counter
rowLoop:
	lb $t4, ($t2)
	beq $t4, $t0, rowFalse	#Return false if a number is found in the row that is equal to a0
	addi $t2, $t2, 1	#Increment the address
	addi $t3, $t3, 1	#Increase the counter by one
	slti $t8, $t3, 9	#Check if the counter is less than 9
	bnez $t8, rowLoop	#Branch if the counter is less than 9
	
	addi $v0, $0, 1		#Set v0 to 1
	jr $ra			#Return
	
rowFalse:
	addi $v0, $0, 0		#Set v0 to 0, false
	jr $ra			#Return


#Method to check if you can add a number to a column
#a0 - the number to be added
#a1 - the number of the column
#return v0 - true if the number can be added, false if it can't
_checkColumn:
	add $t0, $a0, $0	#Move the value of a0 to t0
	add $t1, $a1, $0	#Move the value of a1 to t1
	addi $t2, $0, 0xffff8000	#Set t2 to the first memory location
	add $t2, $t2, $t1	#Add t1 to t2 to get the starting address of the row
	addi $t3, $0, 0		#Set t3 to 0, counter
colLoop:
	lb $t4, ($t2)
	beq $t4, $t0, colFalse	#Return false if a number is found in the row that is equal to a0
	addi $t2, $t2, 9	#Increase the address by 9
	addi $t3, $t3, 1	#Increase the counter by one
	slti $t8, $t3, 9	#Check if the counter is less than 9
	bnez $t8, colLoop	#Branch if the counter is less than 9
	
	addi $v0, $0, 1		#Set v0 to 1
	jr $ra			#Return
	
colFalse:
	addi $v0, $0, 0		#Set v0 to 0, false
	jr $ra			#Return

#Method to check if you can add a number to a 3x3 grid
#a0 - the number to be added
#a1 - the number of the grid
#return v0 - true if the number can be added, false if it can't
_checkBox:
	addi $t0, $a0, 0	#Move a0 to t0
	addi $t1, $a1, 0	#Move a1 to t1
	beqz $t1, box0		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box1		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box2		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box3		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box4		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box5		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box6		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box7		#Jump to set starting address
	subi $t1, $t1, 1	#Subtract 1 from t1
	beqz $t1, box8		#Jump to set starting address
back:
	addi $t3, $0, 0		#Set t3 to 0, outer counter
boxOuter:
	
	addi $t4, $0, 0		#Set t4 to 0, inner counter
	boxInner:
		lb $t5, ($t2)
		beq $t5, $t0, boxFalse	#Return false if a number is found in the row that is equal to a0
		
		addi $t4, $t4, 1	#Increment inner counter
		addi $t2, $t2, 1	#Increase the address by 1
		slti $t8, $t4, 3	#Check if the inner counter is less than 3
		bnez $t8, boxInner
		
	addi $t2, $t2, 6	#Increase the address by 7
	addi $t3, $t3, 1	#Increment outer counter
	slti $t8, $t3, 3	#Check if the outer counter is less than 3
	bnez $t8, boxOuter	#Loop if counter is less than 3

	addi $v0, $0, 1		#Set v0 to 1, true
	jr $ra			#Return
	
boxFalse:
	addi $v0, $0, 0		#Set v0 to 0, false
	jr $ra			#Return

box0:
	addi $t2, $0, 0xffff8000
	j back
box1:
	addi $t2, $0, 0xffff8003
	j back
box2:
	addi $t2, $0, 0xffff8006
	j back
box3:
	addi $t2, $0, 0xffff801b
	j back
box4:
	addi $t2, $0, 0xffff801e
	j back
box5:
	addi $t2, $0, 0xffff8021
	j back
box6:
	addi $t2, $0, 0xffff8036
	j back
box7:
	addi $t2, $0, 0xffff8039
	j back
box8:
	addi $t2, $0, 0xffff803c
	j back

#Print out the sudoku grid stored in memory
_printSudokuGrid:
	addi $t0, $0, 0		#Set t0 to 0
	addi $t1, $0, 0		#Set t1 to 0
	addi $t2, $0, 0xffff8000	#Set t2 to the first address of the sudoku 
	
	row:
	addi $t1, $0, 0		#Reset counter to 0
	
		col:
		lb $a0, ($t2)		#Load the integer stored at the current memory address into a0 to be printed
		addi $v0, $0, 1		#Syscall 1: Print integer
		syscall
		addi $t1, $t1, 1	#Increase counter by 1
		addi $t2, $t2, 1	#Increse memory address by 1
		slti $t7, $t1, 9
		bnez $t7, col		#Loop if counter is less than nine
		#End of column loop
	
	addi $v0, $0, 11	#Syscall 11: Print character
	addi $a0, $0, 10	#Load the ascii character for a line into a0
	syscall			#Print a line
	addi $t0, $t0, 1
	slti $t7, $t0, 9	
	bnez $t7, row		#Loop if counter is less than nine
	#End of row loop
	
	jr $ra