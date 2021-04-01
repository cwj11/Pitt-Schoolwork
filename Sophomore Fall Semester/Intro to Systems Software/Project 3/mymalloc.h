/*
 * mymalloc.h
 *
 *  Created on: Nov 12, 2018
 *      Author: Connor
 */

#ifndef MYMALLOC_H_
#define MYMALLOC_H_

void *my_malloc(int size);

void my_free(void *ptr);

void dump_heap();

#endif /* MYMALLOC_H_ */
