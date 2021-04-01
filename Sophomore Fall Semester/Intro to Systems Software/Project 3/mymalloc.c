/*
 * cwj11-project3.c
 *
 *  Created on: Nov 12, 2018
 *      Author: Connor
 */


#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <assert.h>
#include "mymalloc.h"

struct Block {
	int occ;              // whether block is occupied
	int size;             // size of block (including header)
	struct Block *prev;   // pointer to previous block
	struct Block *next;   // pointer to next block
};

static struct Block *head = NULL;       // head of list

void *my_malloc(int size){
	size = size + sizeof(struct Block);
	if(head == NULL){
		head = sbrk(size);
		head->occ = 1;
		head->size = size;
		head->prev = NULL;
		head->next = NULL;
		return (void *) ((char *)head + sizeof(struct Block));
	}
	struct Block *bestFit = NULL;
	struct Block *currentBlock = head;
	if(currentBlock->occ == 0 && currentBlock->size >= size){
		bestFit = currentBlock;
		if(bestFit->size == size)
			goto bestFitFound;
	}
	while(currentBlock->next != NULL){
		currentBlock = currentBlock->next;
		if(currentBlock->occ == 0 && currentBlock->size >= size){
			if(bestFit == NULL){
				bestFit = currentBlock;
				if(bestFit->size == size)
					break;
			}else{
				if(currentBlock->size < bestFit->size){
					bestFit = currentBlock;
					if(bestFit->size == size){
						break;
					}
				}
			}
		}
	}
	bestFitFound:
	if(bestFit == NULL){
		currentBlock->next = sbrk(size);
		currentBlock->next->prev = currentBlock;
		currentBlock->next->next = NULL;
		currentBlock->next->size = size;
		currentBlock->next->occ = 1;
		return (void *) ((char *)currentBlock->next + sizeof(struct Block));
	}else{
		bestFit->occ = 1;
		if(bestFit->size - size > sizeof(struct Block)){
			struct Block *nextBlock = bestFit->next;
			struct Block *newBlock = (struct Block *) ((char *)bestFit + size);
			newBlock->occ = 0;
			newBlock->size = bestFit->size - size;
			newBlock->prev = bestFit;
			newBlock->next = nextBlock;
			bestFit->next = newBlock;
			bestFit->size = size;
			nextBlock->prev = newBlock;
		}
		return (void *) ((char *)bestFit + sizeof(struct Block));
	}



}


void my_free(void *ptr){
	struct Block *block = (struct Block *) ((char *)ptr - (char *)sizeof(struct Block));
	//	printf("%d %d", block, head);
	//	fflush(stdout);
	if(block->next == NULL){
		if(block == head){
			head = NULL;
			//			printf("size: %d\n", block->size);
			sbrk(0 - block->size);
			return;
		}else{
			if(block->prev == head){
				if(head->occ == 0){
					//					printf("size: %d\n", block->size);
					sbrk(0 - block->size - head->size);
					head = NULL;
					return;
				}else{
					head->next = NULL;
					//					printf("size: %d\n", block->size);
					sbrk(0 - block->size);
					return;
				}
			}else{
				if(block->prev->occ == 0){
					block->prev->prev->next = NULL;
					//					printf("size: %d\n", block->size);
					sbrk(0 - block->size - block->prev->size);
					return;
				}else{
					block->prev->next = NULL;
					//					printf("size: %d\n", block->size);
					sbrk(0 - block->size);
					return;
				}
			}
		}

	}
	//	printf("hey");
	//	fflush(stdout);
	block->occ = 0;
	if(block == head)
		goto skip;
	if(block->prev->occ == 0){
		block->prev->size += block->size;
		block->prev->next = block->next;
		block->next->prev = block->prev;
		block = block->prev;
	}
	skip:
	if(block->next->occ == 0){
		block->size += block->next->size;
		block->next = block->next->next;
		block->next->prev = block;
	}
	return;


}






/** @brief Dump the contents of the heap.
 *
 *  Traverse the heap starting from the head of the list and print
 *  each block.  While traversing, check the integrity of the heap
 *  through various assertions.
 *
 *  @return Void.
 */
void dump_heap()
{
	struct Block *cur;
	printf("brk: %p\n", sbrk(0));
	printf("head->");
	for(cur = head; cur != NULL; cur = cur->next) {
		printf("[%d:%d:%d]->", cur->occ, (char*)cur - (char*)head, cur->size);
		fflush(stdout);
		assert((char*)cur >= (char*)head && (char*)cur + cur->size <= (char*)sbrk(0)); // check that block is within bounds of the heap
		if(cur->next != NULL) {
			assert(cur->next->prev == cur); // if not last block, check that forward/backward links are consistent
			assert((char*)cur + cur->size == (char*)cur->next); // check that the block size is correctly set
		}
	}
	printf("NULL\n");
}

