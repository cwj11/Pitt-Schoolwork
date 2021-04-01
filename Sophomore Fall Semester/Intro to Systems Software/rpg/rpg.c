/*
 * Project1.c
 *
 *  Created on: Oct 2, 2018
 *      Author: Connor
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

struct Weapon{
	char *name;
	int damage;
	int rolls;
};

struct Armor{
	char *name;
	int protection;
};

struct Player{
	struct Weapon *weapon;
	struct Armor *armor;
	int health;
	char *name;
};

void printPlayerInfo(const struct Player *p);

char *attack(struct Player *attacker, struct Player *defender);


int main(){
	struct Weapon weapon[5];
	struct Armor armor[5];
	struct Player player1;
	struct Player player2;

	srand((unsigned int)time(NULL));

	weapon[0].name = "Dagger";
	weapon[0].damage = 4;
	weapon[0].rolls = 1;

	weapon[1].name = "Short sword";
	weapon[1].damage = 6;
	weapon[1].rolls = 1;

	weapon[2].name = "Long sword";
	weapon[2].damage = 8;
	weapon[2].rolls = 1;

	weapon[3].name = "Great sword";
	weapon[3].damage = 6;
	weapon[3].rolls = 2;

	weapon[4].name = "Great axe";
	weapon[4].damage = 12;
	weapon[4].rolls = 1;

	armor[0].name = "cloth";
	armor[0].protection = 10;

	armor[1].name = "studded leather";
	armor[1].protection = 12;

	armor[2].name = "ring mail";
	armor[2].protection = 14;

	armor[3].name = "chain mail";
	armor[3].protection = 16;

	armor[4].name = "plate";
	armor[4].protection = 18;

	player1.health = 20;
	player2.health = 20;

	player1.name = "Player 1";
	player2.name = "Player 2";

	printf("List of available armors:\n");
	int i = 0;
	for(;i < 5; i++){
		printf("%d: %s (AC=%d)\n", i, armor[i].name, armor[i].protection);
	}
	int playerArmor;
	printf("\nChoose Player 1 armor:");
	fflush(stdout);
	scanf("%d", &playerArmor);
	player1.armor = &armor[playerArmor];
	printf("Choose Player 2 armor:");
	fflush(stdout);
	scanf("%d", &playerArmor);
	player2.armor = &armor[playerArmor];
	printf("\nList of available weapons:\n");
	for(i = 0; i < 5; i++){
		printf("%d: %s (damage = %dd%d)\n", i, weapon[i].name, weapon[i].rolls, weapon[i].damage);
	}
	int playerWeapon;
	printf("\nChoose Player 1 weapon:");
	fflush(stdout);
	scanf("%d", &playerWeapon);
	player1.weapon = &weapon[playerWeapon];
	printf("Choose Player 2 weapon:");
	fflush(stdout);
	scanf("%d", &playerWeapon);
	player2.weapon = &weapon[playerWeapon];

	printf("\nPlayer Setting Complete:\n");
	printPlayerInfo(&player1);
	printPlayerInfo(&player2);
	printf("\n");

	while(1){
		char c = NULL;
		fight:
		printf("Fight? (y/n): ");
		fflush(stdout);
		scanf("%c", &c);
		scanf("%c", &c);
		if(c == 'n')
			break;
		if (c != 'y'){
			printf("Invalid input\n");
			goto fight;
		}

		int round = 1;
		while(player1.health > 0 && player2.health > 0){
			printf("----- Round %d -----\n", round);
			printf("%s\n", attack(&player1, &player2));
			printf("%s\n", attack(&player2, &player1));
			printPlayerInfo(&player1);
			printPlayerInfo(&player2);
			round++;
			fflush(stdout);
		}
		if(player1.health <= 0){
			if(player2.health <= 0){
				printf("\nIt's a DRAW\n");
			}else{
				printf("\nPlayer 2 WINS\n");
			}
		}else
			printf("\nPlayer 1 WINS\n");
		player1.health = 20;
		player2.health = 20;
	}

	return 0;
}


void printPlayerInfo(const struct Player *p){
	printf("[%s: hp=%d, armor=%s, weapon=%s]\n", p->name, p->health, p->armor->name, p->weapon->name);
	return;
}

/*
 * Method for a player attacking another player
 */
char * attack(struct Player *attacker, struct Player *defender){
	int value = rand() % (20) + 1;
	static char ans[100];
	if(value < defender->armor->protection){
		sprintf(ans, "%s misses %s (attack roll %d)", attacker->name, defender->name, value);
		return ans;
	}
	int attack = rand() % (attacker->weapon->damage * attacker->weapon->rolls - 1) + attacker->weapon->rolls;
	defender->health -= attack;
	sprintf(ans, "%s hits %s for %d damage (attack roll %d)", attacker->name, defender->name, attack, value);
	return ans;
}



