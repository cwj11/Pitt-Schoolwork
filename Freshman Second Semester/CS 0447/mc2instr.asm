.data
	prompt:		.asciiz	"Please enter a machine code(hexadecimal): "
	addString:	.asciiz	"add "
	addiString:	.asciiz "addi "
	andString:	.asciiz	"and "
	andiString:	.asciiz	"andi "
	subString:	.asciiz	"sub "
	orString:	.asciiz	"or "
	oriString:	.asciiz	"ori "
	norString:	.asciiz	"nor "
	sltString:	.asciiz	"slt "
	sltiString:	.asciiz	"slti "
	sllString:	.asciiz	"sll "
	srlString:	.asciiz	"srl "
	beqString:	.asciiz	"beq "
	bneString:	.asciiz	"bne "
	jString:	.asciiz	"j Label"
	jalString:	.asciiz	"jal Label"
	jrString:	.asciiz	"jr "
	lwString:	.asciiz	"lw "
	swString:	.asciiz	"sw "
	lhString:	.asciiz	"lh "
	shString:	.asciiz	"sh "
	lbString:	.asciiz	"lb "
	sbString:	.asciiz	"sb "
	comma:		.asciiz	", "
	label:		.asciiz	"Label"
	zeroReg:	.asciiz	"$zero"
	atReg:		.asciiz	"$at"
	vReg:		.asciiz "$v"
	aReg:		.asciiz	"$a"
	tReg:		.asciiz	"$t"
	sReg:		.asciiz	"$s"
	kReg:		.asciiz	"$k"
	gpReg:		.asciiz	"$gp"
	spReg:		.asciiz	"$sp"
	fpReg:		.asciiz	"$fp"
	raReg:		.asciiz	"$ra"
	leftParen:	.asciiz	"("
	rightParen:	.asciiz	")"
	buffer:		.space 100
	line:		.asciiz "\n"
	invalid:	.asciiz	"Invalid input."
.text
	addi $v0, $0, 4		#Syscall 4: Print String
	la $a0, prompt		#Load prompt address
	syscall
	addi $v0, $0, 8		#Syscall 8: Read String
	la $a0, buffer		#Load address of input buffer
	addi $a1, $0, 9		#String is a maximum of 8 characters long
	syscall			#Read the string
	addi $v0, $0, 4		#Syscall 4: Print String
	la $a0, line		#Load address for line string
	syscall			#Print a line
	addi $t1, $0, 0		#Set counter to zero
	la $t0, buffer		#Load address of stored string
loadNumbers:
	sll $s0, $s0, 4		#Shift 32 bit address left 4 times
	lb $t2, 0($t0)		#Load a byte into t2
	slti $t9, $t2, 97	#Set t9 to 1 if t2 is a digit
	beq $t9, $0, char	#Check if t2 is a digit or char
	subi $t2, $t2, 48	#Change the ascii character to a number
	j digit
char:
	subi $t2, $t2, 87	#Change the ascii char to a number
digit:
	add $s0, $s0, $t2	#Add t2 to s0
	addi $t1, $t1, 1	#Increase counter by 1 
	slti $t9, $t1, 8	#Set t9 to 1 if t1 is less than 9
	addi $t0, $t0, 1
	bne $t9, $0, loadNumbers	#Branch if counter is less than 9
	srl $t3, $s0, 26	#Store the first 6 bits of the address in t3
	beqz $t3, RType	#Jump to R type if opcode is zero
	slti $t9, $t3, 4	# Jump to J type if opcode is less than 4
	bne $t9, $0, JType	#Jump to J Type
	j IType		#Jump to I type
	
RType:
	sll $a0, $s0, 26	#Put the last 6 bits of the address into a0
	srl $a0, $a0, 26
	jal RprintCommand	#Call the print command function for R-Types
	bne $s7, $0, printRS	#Onlt print one register if it is jr command
	sll $a0, $s0, 16	#Put rd into a0
	srl $a0, $a0, 27
	jal printRegister
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
	bne $s6, $0, skipRS	#Branch if there is a shift amount
printRS:
	sll $a0, $s0, 6		#Put rs into a0
	srl $a0, $a0, 27
	jal printRegister
	bne $s7, $0, skip	#Onlt print one register if it is jr command
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
skipRS:
	sll $a0, $s0, 11	#Put rt into a0
	srl $a0, $a0, 27
	jal printRegister
	beq $s6, $0, skip
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
	sll $a0, $s0, 21	#Put the last 16 bits of address into a0
	srl $a0, $a0, 27
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
skip:	
	addi $v0, $0, 10	#Syscall 10: Terminate
	syscall			#Program end
IType:
	srl $a0, $s0, 26	#Put the first six bits of the address into a0
	jal printCommand	#Call the print command function
	bne $s5, $0, storeCommand
	sll $a0, $s0, 6		#Put rs into a0
	srl $a0, $a0, 27
	jal printRegister
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
	sll $a0, $s0, 11	#Put rt into a0
	srl $a0, $a0, 27
	jal printRegister
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
	beqz $s7, printImm	#Jump to printImm if there is one
	addi $v0, $0, 4		#Print Label
	la $a0, label
	syscall
	j skip1
printImm:
	sll $a0, $s0, 16	#Put the last 16 bits of address into a0
	sra $a0, $a0, 16
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
skip1:
	addi $v0, $0, 10	#Syscall 10: Terminate
	syscall			#Program end
storeCommand:
	sll $a0, $s0, 11	#Put rt into a0
	srl $a0, $a0, 27
	jal printRegister
	addi $v0, $0, 4		#Print a comma
	la $a0, comma
	syscall
	sll $a0, $s0, 16	#Put the last 16 bits of address into a0
	sra $a0, $a0, 16
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 4
	la $a0, leftParen
	syscall
	sll $a0, $s0, 6		#Put rs into a0
	srl $a0, $a0, 27
	jal printRegister
	addi $v0, $0, 4
	la $a0, rightParen
	syscall
	addi $v0, $0, 10	#Syscall 10: Terminate
	syscall
	
	

JType:
	srl $a0, $s0, 26	#Put the first six bits of the address into a0
	jal printCommand	#Call the print command function
	
	
	addi $v0, $0, 10	#Syscall 10: Terminate
	syscall			#Program end

#Print a register
#Argument: a0 - the value of the register

printRegister:
	add $t0, $0, $a0	#Move a0 to t0
	beqz $a0, zero		#Check if it is register 0
	slti $t9, $t0, 2
	bne $t9, $0, at		#Check if it is register at
	slti $t9, $t0, 4
	bne $t9, $0, v		#Check if it is a v register
	slti $t9, $t0, 8
	bne $t9, $0, a		#Check if it is an a register
	slti $t9, $t0, 16
	bne $t9, $0, t		#Check if it is a t register
	slti $t9, $t0, 24
	bne $t9, $0, s		#Check if it is an s register
	slti $t9, $t0, 26
	bne $t9, $0, t89	#Check if it is register t8 or t9
	slti $t9, $t0, 28
	bne $t9, $0, k		#Check if it is a k register
	slti $t9, $t0, 29
	bne $t9, $0, gp		#Check if it is register gp
	slti $t9, $t0, 30
	bne $t9, $0, sp		#Check if it is register sp
	slti $t9, $t0, 31
	bne $t9, $0, fp		#Check if it is register fp
	slti $t9, $t0, 32
	bne $t9, $0, ra		#Check if it is register ra
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, invalid		#Load message for invalid input
	syscall
	addi $v0, $0, 10	#Syscall 10: Terminate program
	syscall			#End program
	
	zero:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, zeroReg
		syscall
		
		jr $ra
	at:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, atReg
		syscall
		
		jr $ra
	v:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, vReg
		syscall
		subi $t0, $t0, 2
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	a:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, aReg
		syscall
		subi $t0, $t0, 4
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	t:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, tReg
		syscall
		subi $t0, $t0, 8
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	s:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, sReg
		syscall
		subi $t0, $t0, 16
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	t89:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, tReg
		syscall
		subi $t0, $t0, 24
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	k:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, kReg
		syscall
		subi $t0, $t0, 26
		addi $v0, $0, 1		#Syscall 1: Print integer
		add $a0, $t0, $0	#Load integer to a0 to print
		syscall			#Print the integer
		
		jr $ra
	gp:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, gpReg
		syscall
		
		jr $ra
	sp:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, spReg
		syscall
		
		jr $ra
	fp:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, fpReg
		syscall
		
		jr $ra
	ra:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, raReg
		syscall
		
		jr $ra

#Print a commandusing the function number
#a0 - function number
RprintCommand:
	add $t0, $a0, $0	#Move a0 to t0
	addi $t1, $0, 0x20	#Set t1 to 0x20
	beq $t0, $t1, printAdd	#Jump to print add if function code is 0x20
	addi $t1, $0, 0x24	#Set t1 to 0x24
	beq $t0, $t1, printAnd	#Jump to print and if function code is 0x24
	addi $t1, $0, 0x22	#Set t1 to 0x22
	beq $t0, $t1, printSub	#Jump to print sub if function code is 0x22
	addi $t1, $0, 0x25	#Set t1 to 0x25
	beq $t0, $t1, printOr	#Jump to print or if function code is 0x25
	addi $t1, $0, 0x27	#Set t1 to 0x27
	beq $t0, $t1, printNor	#Jump to print nor if function code is 0x27
	addi $t1, $0, 0x2A	#Set t1 to 0x2A
	beq $t0, $t1, printSlt	#Jump to print slt if function code is 0x2A
	addi $t1, $0, 0x00	#Set t1 to 0x00
	beq $t0, $t1, printSll	#Jump to print sll if function code is 0x00
	addi $t1, $0, 0x02	#Set t1 to 0x02
	beq $t0, $t1, printSrl	#Jump to print srl if function code is 0x02
	addi $t1, $0, 0x08	#Set t1 to 0x08
	beq $t0, $t1, printJr	#Jump to print jr if function code is 0x08
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, invalid		#Load message for invalid input
	syscall
	addi $v0, $0, 10	#Syscall 10: Terminate program
	syscall			#End program
	
	
	printAdd:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, addString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printAnd:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, andString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printSub:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, subString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printOr:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, orString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printNor:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, norString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printSlt:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, sltString	#Load address for command string
		syscall			#Print the string
		addi $s6, $0, 1		#Set s6 to 1
		
		jr $ra			#End
	printSll:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, sllString	#Load address for command string
		syscall			#Print the string
		addi $s6, $0, 1		#Set s6 to 1
		
		jr $ra			#End
	printSrl:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, srlString	#Load address for command string
		syscall			#Print the string
		addi $s6, $0, 1		#Set s6 to 1
		
		jr $ra			#End
	printJr:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, jrString	#Load address for command string
		syscall			#Print the string
		addi $s7, $0, 1		#Set s7 to 1
		
		jr $ra			#End
		
#Print a command name for I and J Types		
#a0 - opcode for command
printCommand:
	add $t0, $a0, $0	#Move a0 to t0
	addi $t1, $0, 0x08	#Set t1 to 0x08
	beq $t0, $t1, printAddi	#Jump to print addi if opcode is equal to 0x08
	addi $t1, $0, 0x0C	#Set t1 to 0x0C
	beq $t0, $t1, printAndi	#Jump to print andi if opcode is equal to 0x0C
	addi $t1, $0, 0x0D	#Set t1 to 0x0D
	beq $t0, $t1, printOri	#Jump to print ori if opcode is equal to 0x0D
	addi $t1, $0, 0x0A	#Set t1 to 0x0A
	beq $t0, $t1, printSlti	#Jump to print slti if opcode is equal to 0x0A
	addi $t1, $0, 0x04	#Set t1 to 0x04
	beq $t0, $t1, printBeq	#Jump to print beq if opcode is equal to 0x04
	addi $t1, $0, 0x05	#Set t1 to 0x05
	beq $t0, $t1, printBne	#Jump to print bne if opcode is equal to 0x05
	addi $t1, $0, 0x02	#Set t1 to 0x02
	beq $t0, $t1, printJ	#Jump to print j if opcode is equal to 0x02
	addi $t1, $0, 0x03	#Set t1 to 0x03
	beq $t0, $t1, printJal	#Jump to print jal if opcode is equal to 0x03
	addi $t1, $0, 0x23	#Set t1 to 0x23
	beq $t0, $t1, printLw	#Jump to print lw if opcode is equal to 0x23
	addi $t1, $0, 0x2B	#Set t1 to 0x2B
	beq $t0, $t1, printSw	#Jump to print sw if opcode is equal to 0x2B
	addi $t1, $0, 0x21	#Set t1 to 0x21
	beq $t0, $t1, printLh	#Jump to print lh if opcode is equal to 0x21
	addi $t1, $0, 0x29	#Set t1 to 0x29
	beq $t0, $t1, printSh	#Jump to print sh if opcode is equal to 0x29
	addi $t1, $0, 0x20	#Set t1 to 0x20
	beq $t0, $t1, printLb	#Jump to print lb if opcode is equal to 0x20
	addi $t1, $0, 0x28	#Set t1 to 0x28
	beq $t0, $t1, printSb	#Jump to print sb if opcode is equal to 0x28
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, invalid		#Load message for invalid input
	syscall
	addi $v0, $0, 10	#Syscall 10: Terminate program
	syscall			#End program
	
	
	
	printAddi:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, addiString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printAndi:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, andiString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printOri:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, oriString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printSlti:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, sltiString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printBeq:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, beqString	#Load address for command string
		syscall			#Print the string
		addi $s7, $0, 1		#Set s7 to 1
		
		jr $ra			#End
	printBne:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, bneString	#Load address for command string
		syscall			#Print the string
		addi $s7, $0, 1		#Set s7 to 1
		
		jr $ra			#End
	printJ:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, jString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printJal:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, jalString	#Load address for command string
		syscall			#Print the string
		
		jr $ra			#End
	printLw:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, lwString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	printSw:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, swString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	printLh:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, lhString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	printSh:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, shString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	printLb:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, lbString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	printSb:
		addi $v0, $0, 4		#Syscall 4: Print string
		la $a0, sbString	#Load address for command string
		syscall			#Print the string
		addi $s5, $0, 1		#Set s5 to 1
		
		jr $ra			#End
	