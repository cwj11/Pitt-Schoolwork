.data
	prompt:		.asciiz	"Enter a filename: "
	firstTwo:	.asciiz	"The first two characters: "
	size:		.asciiz	"The size of the BMP file (bytes): "
	startAddr:	.asciiz	"The starting address of image data: "
	width:		.asciiz	"Image width (pixels): "
	height:		.asciiz	"Image height (pixels): "
	colorPlanes:	.asciiz	"The number of color planes: "
	numBits:	.asciiz	"The number of bits per pixel: "
	compression:	.asciiz	"Compression method: "
	rawSize:	.asciiz	"The size of raw bitmap data (bytes): "
	horizontalRes:	.asciiz	"The horizontal resolution (pixels/meter): "
	verticalRes:	.asciiz	"The vertical resolution (pixels/meter): "
	numColorsPal:	.asciiz	"The number of colors in the color palette: "
	numColors:	.asciiz	"The number of important colors used: "
	colorIndex0:	.asciiz	"The color at index 0 (B G R): "
	colorIndex1:	.asciiz	"The color at index 1 (B G R): "
	line:		.asciiz	"\n"
	.align 2
	fileName: 	.space 100
	.align 2
	firstTwoChar:	.space 3
	.align 2
	bmpSize:	.space 4
	address:	.space 8
	DIBHeader:	.space 4
.text
	la $a0, prompt		#Load prompt address into a0
	jal printMessage	#Print the prompt
	addi $v0, $0, 8		#Syscall 8: Read string
	la $a0, fileName	#Load address of buffer input
	add $a1, $0, 100
	syscall
	
	add $s0, $0, 1
remove:
	lb $a3, fileName($s0)
	addi $s0, $s0, 1
	bnez $a3, remove
	beq $a1, $s0, skip
	subiu $s0, $s0, 2
	sb $zero, fileName($s0)
skip:
	
	addi $v0, $0, 13	#Syscall 13: Open file
	add $a1, $0, $0		#Set a1 to zero
	add $a2, $0, $0		#Set s2 to zero
	syscall			#Open file
	add $s0, $0, $v0	#Move file descriptor to s0
	add $a0, $0, $s0	#Copy file descriptor to a0
	jal printFirstTwoChar	#Call function to print the first two characters
	la $a0, size		#Load message for bmp size
	jal printMessage	#Call the prinMessage function
	add $a0, $0, $s0	#Move file descriptor to a0
	jal printBMPSize	#Call printBMPSize function
	la $a0, startAddr	#Load message for starting address
	jal printMessage	#Call the printMessage function
	add $a0, $0, $s0	#Move file descriptor to a0
	jal printStartAddr	#Cal function to print the starting address
	jal printLine		#Call function to print a line
	add $a0, $0, $s0	#Move file descriptor to a0
	jal getDIBHeaderSize	#Call function to get DIBHeader
	add $a0, $v0, $0	#Move the DIB Header size to a0
	add $s2, $a0, $0	#Move the header size to s2
	subi $a0, $a0, 4	#Subtract four from the header size
	addi $v0, $0, 9		#Syscall 9: Allocate heap memory
	syscall
	add $s1, $v0, $0	#Move the allocated memory address to s1
	add $a2, $s2, $s2	#Move the size of the DIB Header to a2
	add $a1, $s1, $0	#Move the allocated memory address to a1
	add $a0, $s0, $0	#Move the file descriptor to a0
	jal readDIBHeader	#Call function
	add $a0, $s1, $0	#Move the allocated memory address to a0
	jal printWidthHeight
	jal printLine
	add $a0, $s1, $0	#Move the heap address to a0
	jal printColorPlane	#Call function to print color planes
	jal printLine
	add $a0, $s1, $0	#Move the heap address to a0
	jal printNumBits	#Call function to print color planes
	jal printLine
	add $a0, $s1, $0
	jal printCompression
	jal printLine
	add $a0, $s1, $0
	jal printRawSize
	add $s3, $v0, $0
	jal printLine
	add $a0, $s1, $0
	jal printHorizontalRes
	jal printLine
	add $a0, $s1, $0
	jal printVerticalRes
	jal printLine
	add $a0, $s1, $0
	jal printNumColorsPal
	jal printLine
	add $a0, $s1, $0
	jal printNumColors
	jal printLine
	add $a0, $s1, $0
	jal printColorIndexes
	jal printLine
	addi $v0, $0, 14	#Syscall 14: Read file
	add $a0, $s0, $0	#Move file descriptor to a0
	add $a1, $s1, $s2
	add $a2, $s3, $0
	add $a2, $a2, $s2
	syscall
	add $s1, $s1, $s2
	subi $s1, $s1, 12
	add $s1, $s1, $s3
	sub $s1, $s1, $s2
	add $v0, $0, 32
	add $a0, $0, 10000
	syscall
	jal printImage
	
	
	
	
	addi $v0, $0, 10	#Syscall 10: Terminate program
	syscall			#Program end

#Function to print out a message
#a0 - address of message to print	
printMessage:
	addi $v0, $0, 4		#Syscall 4: Print string
	syscall			#Print the message
	jr $ra			#Return to callee
	
	
#Print the first two characters of the bmp file
#a0 - file descriptor	
printFirstTwoChar:
	addi $v0, $0, 14	#Syscall 14: Read from file
	la $a1, firstTwoChar	#Load address of input buffer
	addi $a2, $0, 2		#Load 2 into a3 to read two bytes from file
	syscall
	addi $t0, $0, 10	#Load 10 into t0
	lb $t0, firstTwoChar+2
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, firstTwo	#Load address for first two message
	syscall
	la $a0, firstTwoChar	#Load address of characters to print
	syscall
	la $a0, line
	syscall			#Print a line
	jr $ra
	
printBMPSize:
	addi $v0, $0, 14 	#Syscall 14: Read from file
	la $a1, bmpSize		#Load address of input buffer
	addi $a2, $0, 4		#Load number of bytes to read in a2
	syscall
	addi $v0, $0, 1		#Syscall 1: Print integer
	lw $a0, bmpSize		#Load data from file
	syscall			#Print size of the bmp file
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, line
	syscall			#Print a line
	jr $ra			#Jump back to callee
	
printStartAddr:
	addi $v0, $0, 14	#Syscall 14: Read from file
	la $a1, address		#Load address of input buffer
	addi $a2, $0, 8		#Read 8 bytes from the file
	syscall
	addi $v0, $0, 1		#Syscall 1: Print integer
	lw $a0, address+4	#Load the starting address into a0
	syscall
	jr $ra

#Get the DIB header size
#a0 - file descriptor
#return v0 - size of the DIB header		
getDIBHeaderSize:
	addi $v0, $0, 14	#Syscall 14: Read from file
	la $a1, DIBHeader	#Load address of input buffer
	addi $a2, $0, 4		#Read 4 bytes from the file
	syscall
	lw $v0, DIBHeader	#Load the size of the DIB Header into v0
	jr $ra			#Jump back to callee with return in v0
	
#Print a line
printLine:
	addi $v0, $0, 4		#Syscall 4: Print a string
	la $a0, line		#Load address for a line
	syscall
	jr $ra
	
#Load all DIB Header data into heap
#a0 - file descriptor
#a1 - heap address
#a2 - DIB header size	
readDIBHeader:
	addi $v0, $0, 14	#Syscall 14: Read from file
	syscall			#Read the file
	jr $ra
	
#Print the width and length
#a0 - heap address
printWidthHeight:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, width		#Load address for width message
	syscall
	lw $a0, 0($t0)		#Load the width
	add $s4, $a0, $0	#Move the width to s4
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 4		#Syscall 4: Print a string
	la $a0, line		#Load address for a line
	syscall
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, height		#Load address for width message
	syscall
	lw $a0, 4($t0)		#Load the height
	add $s5, $a0, $0	#Move the height to s5
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the colorPlane
#a0 - heap address
printColorPlane:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, colorPlanes	#Load the message
	syscall
	lh $a0, 8($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the number of bits per pixel
#a0 - heap address		
printNumBits:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, numBits		#Load the message
	syscall
	lh $a0, 10($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the compression method
#a0 - heap address		
printCompression:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, compression	#Load the message
	syscall
	lw $a0, 12($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the raw size
#a0 - heap address
#v0 - return the raw size of the data		
printRawSize:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, rawSize		#Load the message
	syscall
	lw $a0, 16($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	add $v0, $a0, $0	#Return the raw size
	jr $ra
	
#Print the horizontal resolution
#a0 - heap address		
printHorizontalRes:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, horizontalRes	#Load the message
	syscall
	lw $a0, 20($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the vertical resolution
#a0 - heap address		
printVerticalRes:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, verticalRes	#Load the message
	syscall
	lw $a0, 24($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the number of colors in the pallette
#a0 - heap address		
printNumColorsPal:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, numColorsPal	#Load the message
	syscall
	lw $a0, 28($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra

#Print the number of colors
#a0 - heap address		
printNumColors:
	add $t0, $a0, $0	#Move the heap address to t0
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, numColors	#Load the message
	syscall
	lw $a0, 32($t0)		#Load the number of color planes
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra
	
#Print the both the colors
#a0 - heap address	
printColorIndexes:
	add $t0, $a0, $0	#Move the heap address to t0
	add $t0, $t0, $s2	#Add the size of DIB Header
	subi $t0, $t0, 4	#Subtract 4 from address because it makes it work for some reason
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, colorIndex0	#Load address for width message
	syscall
	lbu $a0, 0($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 11	#Syscall 11: Print a character
	addi $a0, $0, 32	#Load a space into a0
	syscall
	lbu $a0, 1($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 11	#Syscall 11: Print a character
	addi $a0, $0, 32	#Load a space into a0
	syscall
	lbu $a0, 2($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 4		#Syscall 4: Print a string
	la $a0, line		#Load address for a line
	syscall
	addi $v0, $0, 4		#Syscall 4: Print string
	la $a0, colorIndex1		#Load address for width message
	syscall
	lbu $a0, 4($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 11	#Syscall 11: Print a character
	addi $a0, $0, 32	#Load a space into a0
	syscall
	lbu $a0, 5($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	addi $v0, $0, 11	#Syscall 11: Print a character
	addi $a0, $0, 32	#Load a space into a0
	syscall
	lbu $a0, 6($t0)		#Load the width
	addi $v0, $0, 1		#Syscall 1: Print integer
	syscall
	jr $ra

#Print the image in the bmp file
#a0 - address of the last 4 bytes stored in data
printImage:
	addi $s6, $s4, 31
	srl $s6, $s6, 5
	sll $s6, $s6, 2
	
	addi $t5, $0, 0 	#Set height counter to 0
	srl $s6, $s4, 2		#Divide the width by 4
addOne:
	andi $t7, $s6, 0x3
	beqz $t7, done
	addi $s6, $s6, 1
	j addOne
done:
	
	srl $t6, $s5, 3		#Divide the height by 8 and put it into t6
	sll $t7, $s5, 29	
	beqz $t7, outerLoop
	addi $t6, $t6, 1
outerLoop:
	addi $s3, $0, 0
	addi $t4, $0, 0		#Set width counter to 0
	andi $t7, $t5, 0x1
innerLoop:
	slt $t7, $t4, $s4
	beq $t7, $0, blank


	lhu $t0, 0($s1)		#Load data into t registers
	andi $t7, $t0, 0xff	#Change to big endian
	srl $t0, $t0, 8
	sll $t7, $t7, 8
	add $t0, $t7, $t0
	sll $t0, $t0, 16	#Shift the numbers into upper half of t0
	sub $s7, $s1, $s6
	lhu $t7, ($s7)
	andi $a3, $t7, 0xff	#Change to big endian
	srl $t7, $t7, 8
	sll $a3, $a3, 8
	add $t7, $a3, $t7
	
	add $t0, $t7, $t0
	sub $s7, $s7, $s6
	
	lhu $t1, 0($s7)		#Load data into t registers
	andi $t7, $t1, 0xff	#Change to big endian
	srl $t1, $t1, 8
	sll $t7, $t7, 8
	add $t1, $t7, $t1
	sll $t1, $t1, 16	#Shift the numbers into upper half of t1
	sub $s7, $s7, $s6
	lhu $t7, ($s7)
	andi $a3, $t7, 0xff	#Change to big endian
	srl $t7, $t7, 8
	sll $a3, $a3, 8
	add $t7, $a3, $t7
	add $t1, $t7, $t1
	sub $s7, $s7, $s6
	
	lhu $t2, 0($s7)		#Load data into t registers
	andi $t7, $t2, 0xff	#Change to big endian
	srl $t2, $t2, 8
	sll $t7, $t7, 8
	add $t2, $t7, $t2
	sll $t2, $t2, 16	#Shift the numbers into upper half of t2
	sub $s7, $s7, $s6
	lhu $t7, ($s7)
	andi $a3, $t7, 0xff	#Change to big endian
	srl $t7, $t7, 8
	sll $a3, $a3, 8
	add $t7, $a3, $t7
	add $t2, $t7, $t2
	sub $s7, $s7, $s6
	
	lhu $t3, 0($s7)		#Load data into t registers
	andi $t7, $t3, 0xff	#Change to big endian
	srl $t3, $t3, 8
	sll $t7, $t7, 8
	add $t3, $t7, $t3
	sll $t3, $t3, 16	#Shift the numbers into upper half of t3
	sub $s7, $s7, $s6
	lhu $t7, ($s7)
	andi $a3, $t7, 0xff	#Change to big endian
	srl $t7, $t7, 8
	sll $a3, $a3, 8
	add $t7, $a3, $t7
	add $t3, $t7, $t3
	subi $s1, $s1, 4
	
	addi $t6, $0, -1
	addi $a2, $0, 0x80008000
printLineLoop:
	
	
	addi $t6, $t6, 1	#Increase counter
	and $t8, $a2, $t0
	sllv $t8, $t8, $t6
	add $a0, $t8, $0
	srl $t8, $t8, 24
	srl $a0, $a0, 9
	add $t8, $a0, $t8
	
	and $a0, $t1, $a2
	sllv $a0, $a0, $t6
	srl $a1, $a0, 26		#Add the first bit to t8
	add $t8, $a1, $t8
	srl $a0, $a0, 11
	add $t8, $a0, $t8
	
	and $a0, $t2, $a2
	sllv $a0, $a0, $t6
	srl $a1, $a0, 28		#Add the first bit to t8
	add $t8, $a1, $t8
	srl $a0, $a0, 13
	add $t8, $a0, $t8
	
	and $a0, $t3, $a2
	sllv $a0, $a0, $t6
	srl $a1, $a0, 30		#Add the first bit to t8
	add $t8, $a1, $t8
	srl $a0, $a0, 15
	add $t8, $a0, $t8
	
	addi $t4, $t4, 1	#Increase width counter by one
	srl $a2, $a2, 1
	j notBlank

blank:	
	add $t8, $0, $0
	addi $t4, $t4, 1	#Increase width counter by one

notBlank:	
	addi $v0, $0, 34	#Syscall 1: Print integer in hex
	addi $a0, $t8, 0
	#syscall
	addi $v0, $0, 4		#Syscall 4: Print a string
	la $a0, line		#Load address for a line
	#syscall
	addi $t9, $0, 1		#Set t9 to 1
wait:	bne $t9, $0, wait	#Wait until t9 is zero
	addi $s3, $s3, 1
	slti $t7, $t6, 15
	bne $t7, $0, printLineLoop	#Loop if data from registers still needs to be read
	
	addi $t7, $0, 480
	slt $t7, $t4, $t7
	bne $t7, $0, innerLoop	#Branch to inner loop if width is less than 480
	
	slti $t7, $s4, 17
	bne $t7, $0, lessThan16
	andi $t7, $t5, 0x8
	beqz $t7, addTwo
	j subWidth
addTwo:
	addi $s3, $s4, 31
	srl $s3, $s3, 5
	sll $s3, $s3, 2
	sll $t7, $t7, 5
	add $s1, $s1, $s3
	addi $s1, $s1, 2
subWidth:
	
	addi $v0, $0, 32
	addi $a0, $0, 5000
	#syscall
	
	addi $v0, $0, 32
	addi $a0, $0, 10000
	#syscall

	addi $t5, $t5, 8	#Increase the height counter by 1
	slt $t7, $t5, $s5
	bne $t7, $0, outerLoop

	jr $ra
	
	
lessThan16:
	subi $s1, $s1, 32
	j subWidth
