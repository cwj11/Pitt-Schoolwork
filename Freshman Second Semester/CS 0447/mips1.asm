.text
	addi $v0, $zero, 5
	syscall
	addi $a0, $v0, 0
	addi $v0, $zero, 1
	nor $a0, $a0, $zero
	addi $a0, $a0, 1
	syscall