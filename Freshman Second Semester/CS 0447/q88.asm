#By: Connor Johnson
#CS 0447 Q8.8
.text
wait: beq $t9, $zero, wait # Wait until $t9 is not zero (1)
# Get operands A and B from $a0 and $a1
add $t0, $zero, $a0	#Move a0 to t0
add $t1, $zero, $a1	#Move a1 to t1
# Calculate A + B and put the result in the lower 16-bit of $v0
add $v0, $t0, $t1
sll $v0, $v0, 16
srl $v0, $v0, 16
# Calculate A - B and put the result in the higher 16-bit of $v0
sub $s0, $t0, $t1
sll $s0, $s0, 16
or $v0, $v0, $s0
addi $s0, $zero, 0
# Calculate A * B and put the result in the lower 16-bit of $v1
	srl $s2, $t0, 31
	beq $s2, $zero, skip1
	addi $t7, $t7, 1
	sub $t0, $zero, $t0
skip1:
	srl $s2, $t1, 31
	beq $s2, $zero, skip2
	addi $t7, $t7, 1
	sub $t1, $zero, $t1
skip2:
	add $t2, $zero, $t1	#Move a copy of the first integer to $t2
	addi $t3, $zero, 1	#Set t3 to 1
multi:
	and $t4, $t0, $t3
	beq $t4, $zero, skip	#Check if next digit is a 1 or 0
	add $t5, $t5, $t2	#Add t2 to t5
skip:
	sll $t2, $t2, 1
	sll $t3, $t3, 1
	slt $s0, $t0, $t3	#Check if t1 is less than t3
	beq $s0, $zero, multi	#Loop if t3 is less than or equal to t1
	bne $t7, 1, pos		#Turn answer to negative if answer is negative
	sub $t5, $zero, $t5
pos:
	sll $t5, $t5, 8		#Remove upper half of t5
	srl $t5, $t5, 16	#Remove lower half of t5
	
	add $v1, $zero, $t5
	addi $t5, $zero, 0	#Set t5 to 0

# Calculate A / B and put the result in the higher 16-bit of $v1
addi $t4, $zero, 0		#Set t4 back to 0
srl $t3, $t3, 1
divLoop:
	sll $t5, $t5, 1		#Shift the answer left 1
	sll $t4, $t4, 1		#Shift the remainder left 1
	and $t8, $t3, $t0	#Check if next digit is a 1 or 0
	beq $t8, $zero, divi	#Check if next digit is a 1 or 0
	addi $t4, $t4, 1	#Add 1 to t4 if next digit is a 1
divi:
	slt $s0, $t4, $t1	#Check if divisor is less than or equal to t4
	bne $s0, $zero, divide	
	addi $t5, $t5, 1	#Add 1 to t5, answer
	sub $t4, $t4, $t1	#Subtract the divisor from the remainder
divide:
	srl $t3, $t3, 1
	bne $t3, $zero, divLoop	#Loop if t3 isn't 0
	
	add $t2, $zero, $t4	#Move remainder to t2
	sll $t2, $t2, 8		#Shift the remainder left 8 times
	
	addi $t4, $zero, 0	#Set remainder to 0
	addi $t3, $zero, 1	#Set t3 to 1
remLoop:
	slt $s0, $t2, $t3
	bne $s0, $zero, remDiv
	sll $t3, $t3, 1
	j remLoop
remDiv:
	srl $t3, $t3, 1		#Shift once to the right
divLoop2:
	sll $t6, $t6, 1		#Shift the answer left 1
	sll $t4, $t4, 1		#Shift the remainder left 1
	and $t8, $t3, $t2	#Check if next digit is a 1 or 0
	beq $t8, $zero, divi2	#Check if next digit is a 1 or 0
	addi $t4, $t4, 1	#Add 1 to t4  if next digit is a 1
divi2:
	slt $s0, $t4, $t1	#Check if divisor is less than or equal to t4
	bne $s0, $zero, divide2	
	addi $t6, $t6, 1	#Add 1 to t5, answer
	sub $t4, $t4, $t1	#Subtract the divisor from the remainder
divide2:
	srl $t3, $t3, 1
	bne $t3, $zero, divLoop2#Loop if t3 isn't 0

	sll $t5, $t5, 8		#Shift answer left 8 times
	add $t5, $t5, $t6	#Add decimal to answer
	bne $t7, 1, pos2	#Turn answer to negative if answer is negative
	sub $t5, $zero, $t5
pos2:	
	sll $t5, $t5, 16	#Shift t5 16 times
	or $v1, $v1, $t5
	addi $t5, $zero, 0	#Set t5 back to zero
	addi $t4, $zero, 0	#Set t4 to zero
# Calculate sqrt(|A|) and put the result in the lower 16-bit of $a2
	addi $t3, $zero, 49152	#Set t3 equal to 11 00 00 00 00 00 00 00
	addi $t8, $zero, 16	#Set t8 to 14, number of shifts needed
	addi $s2, $zero, 0	#Set s2 to zero, counter
	addi $s3, $zero, 5	#Set s3 to 4, counter goal
sqrtLoop:
	#Step 1
	subi $t8, $t8, 2	#Subtract two from t8
	sll $t4, $t4, 2		#Shift the remainder to the left twice
	ble $t8, $zero, counter	#Dont add anything to remainder if t8 is zero
continue:
	and $t6, $t3, $t0	#Get the first two digits of A
	srlv $t6, $t6, $t8	#Shift two digits so they are the right most bits
	add $t4, $t4, $t6	#Add the two digits to the remainder

	
	#Step 2
	sll $t2, $t5, 2		#Multiply current result by 4, temp
	#Step 3
	addi $s1, $t2, 1	#temp + x
	sll $t5, $t5, 1		#Multiply result by 2	
	ble $s1, $t4, sqrt345	#Do steps 3, 4, and 5 if temp + x is less than current_remainder
back:

	srl $t3, $t3, 2
	blt $s2, $s3, sqrtLoop
#	sll $t5, $t5, 1		#Multiply the answer by 2
	add $a2, $t5, $zero
	
	
	
	
	addi $t5, $zero, 0	#Set t5 back to zero
	addi $t6, $zero, 0 	#Set t6 back to zero
	addi $t7, $zero, 0	#Set t7 back to zero
add $t9, $zero, $zero # Set $t9 back to 0
j wait # Go back to wait


sqrt345:
	sub $t4, $t4, $s1 	#Subtract temp + x from remainder
	addi $t5, $t5, 1	#Add one to result
	j back			#Jump back to loop
	
counter:
	addi $s2, $s2, 1	#Increase counter by 1
	j continue		#Jump back to code
	
	
	
	
	
