/*
 * tester.c
 *
 *  Created on: Nov 12, 2018
 *      Author: Connor
 */

#include "mymalloc.h"
#include <stdio.h>

struct Block {
	int occ;              // whether block is occupied
	int size;             // size of block (including header)
	struct Block *prev;   // pointer to previous block
	struct Block *next;   // pointer to next block
};

int main(){
	int *num1 = (int *) my_malloc(sizeof(int));
	int *num2 = (int *) my_malloc(sizeof(int));
	int *num3 = (int *) my_malloc(sizeof(int));
	struct Block *n1 = (struct Block *)(num1 - (char *)sizeof(struct Block));
	struct Block *n2 = (struct Block *)(num2 - (char *)sizeof(struct Block));
	struct Block *n3 = (struct Block *)(num3 - (char *)sizeof(struct Block));

	printf("Num 1: [%d, %d, %d]", n1->size, n1->prev, n1->next);
	printf("Num 2: [%d, %d, %d]", n2->size, n2->prev, n2->next);
	printf("Num 3: [%d, %d, %d]", n3->size, n3->prev, n3->next);

//	printf("%d %d\n", num1, num2);
//	dump_heap();
	my_free(num1);
//	dump_heap();
	my_free(num2);
//	dump_heap();
	my_free(num3);
//	dump_heap();
}


