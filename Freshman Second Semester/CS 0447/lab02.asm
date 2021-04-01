#Author: Connor Johnson
#Lab 2: Higher Lower Game
.data
	prompt:		.asciiz "Enter a number between 0 and 9: "
	tooLowMsg: 	.asciiz	"Your guess is too low.\n"
	tooHighMsg:	.asciiz "Tour guess is too high.\n"
	winMsg:		.asciiz "Congratualtions! You win!"
	loseMsg:	.asciiz "You lose. The number was "
	period:		.asciiz "."
.text
	#Randon int generator
	addi $v0, $zero, 30	#Syscall 30: Get System Time
	syscall			#Get System Time
	move $a1, $a0		#Set lower half of system time to RNG seed
	addi $v0, $zero, 40	#Syscall 40: Set seed
	addi $a0, $zero, 0	#Set RNG ID to zero
	syscall			#Set seed numbers for random number generator
	addi $v0, $zero, 42	#Syscall 42: Random int range
	addi $a1, $zero, 10	#Set upper bound for RNG range
	syscall			#Get random int
	move $t0, $a0		#Move random int to $t0
	addi $t4, $zero, 3	#Set target value for counter to $t4
	
	#Guess and check
	loop:
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, prompt		#Load prompt address
		syscall			#Print prompt message
		addi $v0, $zero, 5	#Syscall 5: Read integer
		syscall			#Read integer
		add $t1, $zero, $v0	#Store user guess into $t1
		beq $t0, $t1, win	#Check if players guess is equal to random int
		slt $t2, $t0, $t1	#Check if player guess is less than random int
		addi $t3, $t3, 1	#Add one to counter to keep track of guesses
		beq $t3, $t4, lose	#Jump to lose code after three guesses
		beq $t2, $zero, lessThan	#Go to lessThan if player guess is less than random int
		j moreThan		#Go to moreThan if player guess is more than random int
		
	#Output when user guess is too low
	lessThan:
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, tooLowMsg	#Load tooLow address
		syscall			#Print message
		j loop			#loop back to take another guess
		
	#Output when user guess is too high
	moreThan:
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, tooHighMsg	#Load toooHigh address
		syscall			#Print message
		j loop			#loop back to take another guess
		
	#Output when user wins
	win: 
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, winMsg		#Load winMsg address
		syscall			#Print the win message
		addi $v0, $zero, 10	#Syscall 10: Terminate
		syscall			#Terminate the program
		
	#Output when user loses	
	lose:
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, loseMsg		#Load loseMsg address
		syscall			#Print the lose message
		addi $v0, $zero, 1	#Syscall 1: Print integer
		add $a0, $zero, $t0	#Load the random int to print into $a0
		syscall			#Print the random int
		addi $v0, $zero, 4	#Syscall 4: Print String
		la $a0, period		#Load the period address
		syscall			#Print the period
		addi $v0, $zero, 10	#Syscall 10: Terminate
		syscall			#Terminate the program
