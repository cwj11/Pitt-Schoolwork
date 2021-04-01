/*
 * Project1.c
 *
 *  Created on: Oct 2, 2018
 *      Author: Connor
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include "common.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>


struct Weapon weapon[5];
struct Armor armor[5];


int r;

struct Player npc[10];

//void alarm_handler(int sig);

//void earthquake(int sig);

void respawnPlayer(struct Player * p);

void printPlayerInfo(struct Player *p);

char *attack(struct Player *attacker, struct Player *defender);

void printLandOfMordor(struct Player * npcs, struct Player * player);

void respawnNPC(struct Player * orc, int playerLevel, int num);

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *clientRPG(void * clientS);



int main(int argc, char **argv){
	//	signal(SIGINT, alarm_handler);
	//	signal(SIGTERM, alarm_handler);
	//	signal(SIGRTMIN, earthquake);



	srand(time(NULL));

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

	int i = 1;
	for(;i < 9; i++){
		sprintf(npc[i].name, "Orc%d", i);
		int randomNum = 4;
		npc[i].health = 20;
		npc[i].level = 1;
		npc[i].xp = 2000;
		//		read(r, &randomNum, 1);
		randomNum = rand()%20;
		npc[i].weapon = randomNum%5;
		//		read(r, &randomNum, 1);
		randomNum = rand()%20;
		npc[i].armor = randomNum%5;
	}
	sprintf(npc[0].name, "Sauron");
	npc[0].health = 115;
	npc[0].level = 20;
	npc[0].xp = 1048576000;
	npc[0].armor = 4;
	npc[0].weapon = 4;

	sprintf(npc[9].name, "Gollum");
	npc[9].health = 10;
	npc[9].level = 1;
	npc[9].xp = 2000;
	npc[9].armor = 0;
	npc[9].weapon = 0;


	int sfd;
	struct sockaddr_in addr;

	sfd = socket(PF_INET, SOCK_STREAM, 0);

	if(sfd < -1){
		printf("Invalid scoket error.");
		exit(EXIT_FAILURE);
	}

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(atoi(argv[1]));
	addr.sin_addr.s_addr = INADDR_ANY;

	bind(sfd, (struct sockaddr *)&addr, sizeof(addr));

	if(listen(sfd, 10) < 0){
		printf("Server port error.");
		exit(EXIT_FAILURE);
	}

	pthread_t clients[100];
	int clientCount = 0;
	int clientSocket;
	printf("Ready");
	fflush(stdout);
	while((clientSocket = accept(sfd, NULL, NULL)) != -1)
	{
		pthread_create(&clients[clientCount],NULL,clientRPG,&clientSocket);
		clientCount++;

	}
}



void *clientRPG(void *clientS){

	struct Player player;
	char playerName[100];
	int clientSocket = *((int *)clientS);
	char buffer[100];
	recv(clientSocket, buffer, 100, 0);
	printf("%s", buffer);
//	printf("hey");
//	fflush(stdout);
	recv(clientSocket, playerName, 100, 0);
	FILE *f = fopen("rpg.save", "rb");

	if(f != NULL){
		nextRead:
		if(feof(f))
			goto nofile;
		fread(&player, 1, sizeof(player), f);
		fread(npc, 1, sizeof(npc), f);
		if(strcmp(player.name, playerName) != 0){
			goto nextRead;
		}
		fclose(f);
		goto fileskip;
	}


	nofile:
	printf("hey");
	fflush(stdout);
	int a = 1;
	send(clientSocket, &a, sizeof(int), 0);
	strcpy(player.name, playerName);
	int playerArmor;
	recv(clientSocket, &playerArmor, sizeof(int), 0);
	int playerWeapon;
	recv(clientSocket, &playerWeapon, sizeof(int), 0);
	player.weapon = playerWeapon;
	player.armor = playerWeapon;
	player.level = 1;
	player.xp = 2000;
	player.nextLevelxp = player.xp * 2;
	goto playerSet;


	fileskip:
	a = 0;
	send(clientSocket, &a, sizeof(int), 0);
	playerSet:

	send(clientSocket, &player, sizeof(struct Player), 0);
	send(clientSocket, &npc, sizeof(npc), 0);

	while(1){
		start:
		fflush(stdout);
		int commandNum;
		recv(clientSocket, &commandNum, sizeof(int), 0);
		if(commandNum == 4)
			break;
		if(commandNum == 3){
			send(clientSocket, &player, sizeof(struct Player), 0);
			goto start;
		}
		if(commandNum == 2){
			send(clientSocket, &player, sizeof(struct Player), 0);
			send(clientSocket, npc, sizeof(npc), 0);
			goto start;
		}

		if(commandNum == 1){
			int num;
			recv(clientSocket, &num, sizeof(int), 0);
			char attackString[100];
			while(player.health > 0 && npc[num].health > 0){
				sprintf(attackString, "%s\n", attack(&player, &npc[num]));
				send(clientSocket, attackString, 100, 0);
				sprintf(attackString, "%s\n", attack(&npc[num], &player));\
				send(clientSocket, attackString, 100, 0);
			}
			sprintf(attackString, "Done attacking");
			send(clientSocket, attackString, 100, 0);
			if(player.health <= 0){
				char killString[200];
				if(npc[num].health <= 0){
					sprintf(killString, "%s and %s kill each other.\nRespawning %s...\nRespawning %s...\n", player.name, npc[num].name, player.name, npc[num].name);
					respawnPlayer(&player);
					respawnNPC(&npc[num], player.level, num);
					send(clientSocket, &killString, 200, 0);
				}else{
					sprintf(killString, "%s is killed by %s\nRespawning %s...\n", player.name, npc[num].name, player.name);
					respawnPlayer(&player);
					send(clientSocket, &killString, 200, 0);
				}
				int kill = 0;
				send(clientSocket, &kill, sizeof(int), 0);
				//				char v;
				//				do{
				//					scanf("%c", &v);
				//				}while(v != '\n');
				goto start;
			}else{
				char killString[200];
				char c;
				sprintf(killString, "%s is killed by %s\n", npc[num].name, player.name);
				send(clientSocket, &killString, 200, 0);
				int kill = 1;
				send(clientSocket, &kill, sizeof(int), 0);

				sprintf(killString, "Got %s's %s, exchanging %s's current armor %s (y/n)? ", npc[num].name, armor[npc[num].armor].name, player.name, armor[player.armor].name);
				send(clientSocket, killString, 200, 0);
				recv(clientSocket, &c, sizeof(char), 0);
				if(c == 'y')
					player.armor = npc[num].armor;
				sprintf(killString, "Got %s's %s, exchanging %s's current weapon %s (y/n)? ", npc[num].name, weapon[npc[num].weapon].name, player.name, weapon[player.weapon].name);
				send(clientSocket, &killString, 200, 0);
				recv(clientSocket, &c, sizeof(char), 0);
				if(c == 'y')
					player.weapon = npc[num].weapon;
				player.xp += npc[num].xp;
				char last[200];
				if(player.xp >= player.nextLevelxp){
					player.nextLevelxp *= 2;
					player.level += 1;
					sprintf(last, "%s levels up to level %d!\n", player.name, player.level);
					int len = strlen(last);
					char *levelUp = &last[len];
					sprintf(levelUp, "Respawning %s...\n", npc[num].name);
				}else{
					sprintf(last, "Respawning %s...\n", npc[num].name);
				}
				send(clientSocket, &last, 200, 0);
				respawnNPC(&npc[num], player.level, num);
				goto start;
			}
		}

	}
	pthread_mutex_lock(&mutex);
	FILE *fw = fopen("rpg.save", "rb");
	if(fw == NULL){
		fw = fopen("rpg.save", "ab");
		fclose(fw);
		goto fileWrite;
	}
	int length = 0;
	char playerNameRead[100];
	do{
		fflush(stdout);
		length = 100;
		if(fread(playerNameRead, 1, length, fw) != length){
			if(feof(fw)){
				printf("End of file");
				goto fileWrite;
			}
			if(ferror(fw)){
				printf("Error");
				exit(0);
			}
		}
		fseek(fw, sizeof(player) + sizeof(npc) - 100, SEEK_CUR);
		fflush(stdout);
	}while(strcmp(playerNameRead, player.name) != 0);
	fseek(fw, 0 - (sizeof(player) + sizeof(npc)), SEEK_CUR);
	int loc = ftell(fw);
	fseek(fw, 0, SEEK_END);
	printf("%d\n", loc);
	fclose(fw);
	fw = fopen("rpg.save", "r+b");
	fseek(fw, loc, SEEK_CUR);
	fwrite(&player, 1, sizeof(struct Player), fw);
	fwrite(npc, 1, sizeof(npc), fw);
	fseek(fw, 0, SEEK_END);
	fclose(fw);
	pthread_mutex_unlock(&mutex);
	close(clientSocket);
	return;

	fileWrite:
	fclose(fw);
	fw = fopen("rpg.save", "ab");
	fwrite(&player, 1, sizeof(struct Player), fw);
	fwrite(npc, 1, sizeof(npc), fw);
	fclose(fw);
	pthread_mutex_unlock(&mutex);
	close(clientSocket);
	return;

}
void printPlayerInfo(struct Player *p){
	printf("[%s: hp=%d, armor=%s, weapon=%s, level=%d, xp=%d]\n", p->name, p->health, armor[p->armor].name, weapon[p->weapon].name, p->level, p->xp);
	return;
}

/*
 * Method for a player attacking another player
 */
char * attack(struct Player *attacker, struct Player *defender){
	int randomNum = rand();
	//	read(r, &randomNum, 1);
	int value = (int)(randomNum % (20) + 1);
	static char ans[100];
	if(value < armor[defender->armor].protection){
		sprintf(ans, "%s misses %s (attack roll %d)", attacker->name, defender->name, value);
		return ans;
	}
	//	read(r, &randomNum, 1);
	randomNum = rand();
	int attack = (int)(randomNum % (weapon[attacker->weapon].damage * weapon[attacker->weapon].rolls - 1) + weapon[attacker->weapon].rolls);
	defender->health -= attack;
	sprintf(ans, "%s hits %s for %d damage (attack roll %d)", attacker->name, defender->name, attack, value);
	return ans;
}

void respawnNPC(struct Player * orc, int playerLevel, int num){
	if(num == 0){
		orc->health = 115;
		return;
	}
	if(num == 0){
		orc->health = 10;
		return;
	}

	//	unsigned char n = (unsigned char) playerLevel;
	//	write(r, &n, 1);
	int randomNum;
	//	read(r, &randomNum, 1);
	randomNum = rand() % playerLevel;
	orc->level = randomNum;
	orc->health = 20 + ((orc->level - 1) * 5);
	orc->xp = 2000;
	int i;
	for(i = 1; i < orc->level; i++){
		orc->xp *= 2;
	}
	//	n = 20;
	//	write(r, &n, 1);
	//	read(r, &randomNum, 1);
	randomNum = rand()%20;
	orc->armor = randomNum%5;
	read(r, &randomNum, 1);
	orc->weapon = randomNum%5;
	return;
}

void printLandOfMordor(struct Player *npcs, struct Player *player){
	printf("All is peaceful in the land of Mordor.\nSauron and his minions are blissfully going about their business.\n");
	int i;
	for(i = 0; i < 10; i++){
		printf("%d; ", i);
		printPlayerInfo(&npcs[i]);
	}
	printf("Also at the scene are some adventurers looking for trouble:\n");
	printf("0: ");
	printPlayerInfo(player);
	return;
}
void respawnPlayer(struct Player * p){
	p->health = 20 + ((p->level - 1) * 5);
	p->xp = p->nextLevelxp / 2;
	return;
}


//void alarm_handler(int sig){
//	printf("Saving and exiting...");
//	FILE *fw = fopen("rpg.save", "rb");
//	if(fw == NULL){
//		fw = fopen("rpg.save", "ab");
//		fclose(fw);
//		goto fileWrite;
//	}
//	int length = 0;
//	char playerNameRead[100];
//	do{
//		fflush(stdout);
//		length = 100;
//		if(fread(playerNameRead, 1, length, fw) != length){
//			if(feof(fw)){
//				printf("End of file");
//				goto fileWrite;
//			}
//			if(ferror(fw)){
//				printf("Error");
//				exit(0);
//			}
//		}
//		fseek(fw, sizeof(struct Player) + sizeof(npc) - 100, SEEK_CUR);
//		fflush(stdout);
//	}while(strcmp(playerNameRead, player->name) != 0);
//	fseek(fw, 0 - (sizeof(struct Player) + sizeof(npc)), SEEK_CUR);
//	int loc = ftell(fw);
//	fseek(fw, 0, SEEK_END);
//	printf("%d\n", loc);
//	fclose(fw);
//	fw = fopen("rpg.save", "r+b");
//	fseek(fw, loc, SEEK_CUR);
//	fwrite(player, 1, sizeof(struct Player), fw);
//	fwrite(npc, 1, sizeof(npc), fw);
//	fseek(fw, 0, SEEK_END);
//	fclose(fw);
//	exit(1);
//
//	fileWrite:
//	fclose(fw);
//	fw = fopen("rpg.save", "ab");
//	fwrite(player, 1, sizeof(struct Player), fw);
//	fwrite(npc, 1, sizeof(npc), fw);
//	fclose(fw);
//	exit(1);
//}
//
//void earthquake(int sig){
//	int i;
//	for(i = 0; i < 10; i++){
//		npc[i].health-= 20;
//		if(npc[i].health > 0){
//			printf("%s suffers -20 damage, but survives", npc[i].name);
//		}else{
//			printf("%s suffers -20 damage and dies. Respawning...", npc[i].name);
//			respawnNPC(&npc[i], player.level, i);
//		}
//	}
//	player.health -= 20;
//	if(player.health > 0){
//		printf("%s suffers -20 damage, but survives", player.name);
//	}else{
//		printf("%s suffers -20 damage and dies. Respawning...", npc[i].name);
//		respawnPlayer(&player);
//	}
//	return;
//}
