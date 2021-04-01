/*
 * common.h
 *
 *  Created on: Dec 15, 2018
 *      Author: Connor
 */

#ifndef COMMON_H_
#define COMMON_H_


struct Player{
	char name[100];
	int weapon;
	int armor;
	int health;
	int xp;
	int nextLevelxp;
	int level;
};

struct Weapon{
	char *name;
	int damage;
	int rolls;
};

struct Armor{
	char *name;
	int protection;
};




#endif /* COMMON_H_ */
