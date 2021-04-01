.data
	prompt:		.asciiz	"Please enter a positive integer: "
	secondPrompt:	.asciiz	"Please enter another positive integer: "
	negIntMsg:	.asciiz	"A negative number is not allowed.\n"
	asterisk:	.asciiz	" * "
	exponent:	.asciiz	"^"
	equals:		.asciiz	" = "
	line:		.asciiz	"\n"
.text
	start:
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, prompt		#Load prompt address
		syscall			#Print the prompt String
		addi $v0, $zero, 5	#Syscall 5: Read integer
		syscall			#Read the integer
		slt $s0, $zero, $v0	#Check if integer is positive
		addi $t3, $zero, 1	#Set $t3 to 1, checks if digit in $t1 is 0 or 1
		bne $s0, $zero, positive #Skip negative prompt if integer is positive
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, negIntMsg	#Print that number is negative
		syscall			#Print message
		j start			#Jump to start to get another input
	positive:
		add $t0, $zero, $v0	#Move first integer to $t0
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, secondPrompt	#Load prompt address
		syscall			#Print the prompt string
		addi $v0, $zero, 5	#Syscall 5: Read integer
		syscall			#Read the integer
		slt $s0, $zero, $v0	#Check if integer is positive
		bne $s0, $zero, pos	#Skip negative prompt if integer is positive
		addi $v0, $zero, 4	#Syscall 4: Print string
		la $a0, negIntMsg	#Print that number is negative
		syscall			#Print message
		j positive
	pos:
		add $t1, $zero, $v0	#Move the second integer to $t1
		add $t2, $zero, $t0	#Move a copy of the first integer to $t2
		add $t6, $zero, $t1	#Move a copy of the second integer to t6
	multi:
		and $t4, $t1, $t3
		beq $t4, $zero, skip	#Check if next digit is a 1 or 0
		add $t5, $t5, $t2	#Add t0 to t5
	skip:
		sll $t2, $t2, 1
		sll $t3, $t3, 1
		slt $s0, $t1, $t3	#Check if t1 is less than t3
		beq $s0, $zero, multi	#Loop if t3 is less than or equal to t1
		add $t2, $zero, $t0	#Move a copy of the first integer to $t2
		bne $s1, $zero, loop	#Loop for exponents
		
		addi $v0, $zero, 1 	#Syscall 1: Print integer
		add $a0, $zero, $t0	#Move first integer to a0 to print
		syscall			#Print the integer
		addi $v0, $zero, 4	#Syscall: Print string
		la $a0, asterisk	#Load asterisk to a0
		syscall			#Print the asterisk
		addi $v0, $zero, 1	#Syscall 1: Print integer
		add $a0, $zero, $t1	#Move second integer to a0 to print
		syscall			#Print the second integer
		addi $v0, $zero, 4	#Syscall 5: Print string
		la $a0, equals		#Load equals sign address
		syscall			#Print the equals sign
		addi $v0, $zero, 1	#Syscall 1: Print integer
		add $a0, $zero, $t5	#Move answer to a0 to print
		syscall			#Print the answer
		addi $v0, $zero, 4	#Syscall 5: Print string
		la $a0, line		#Load equals sign address
		syscall			#Print the equals sign
		
	expon:
		add $t1, $zero, $t0	#Set t1 to t0
		addi $s1, $zero, 0	#Set s1 to 0, the counter
		addi $s2 , $zero, 1	#Set s1 to 1 so it loops for exponent
		add $t5, $zero, $t1	#Set t5 to t1
	loop:
		addi $t3, $zero, 1	#Set t3 to 1
		add $t1, $zero, $t5	#Set $t1 to t5
		addi $t5 $zero, 0	#Set t5 to 0
		addi $s1, $s1, 1	#Increase counter by 1
		slt $s0, $s1, $t6	#Check if the counter is less than the second integer
		bne $s0, $zero, multi	#Loop again if counter is less than t6
		
		addi $v0, $zero, 1 	#Syscall 1: Print integer
		add $a0, $zero, $t0	#Move first integer to a0 to print
		syscall			#Print the integer
		addi $v0, $zero, 4	#Syscall: Print string
		la $a0, exponent	#Load asterisk to a0
		syscall			#Print the asterisk
		addi $v0, $zero, 1	#Syscall 1: Print integer
		add $a0, $zero, $t6	#Move second integer to a0 to print
		syscall			#Print the second integer
		addi $v0, $zero, 4	#Syscall 5: Print string
		la $a0, equals		#Load equals sign address
		syscall			#Print the equals sign
		addi $v0, $zero, 1	#Syscall 1: Print integer
		add $a0, $zero, $t1	#Move answer to a0 to print
		syscall			#Print the answer
		addi $v0, $zero, 10	#Syscall 10: Terminate program
		syscall			#Terminate the program
		
		
		
		 
		
