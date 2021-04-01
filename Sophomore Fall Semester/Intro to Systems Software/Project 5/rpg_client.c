/*
 * rpg_client.c
 *
 *  Created on: Dec 14, 2018
 *      Author: Connor
 */
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

struct Weapon weapon[5];
struct Armor armor[5];

void printPlayerInfo(struct Player *p);

void printLandOfMordor(struct Player *npcs, struct Player *player);

int main(int argc, char **argv){
	int sfd;
	struct sockaddr_in addr;

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

	sfd = socket(PF_INET, SOCK_STREAM, 0);

	if(sfd < -1){
		printf("Invalid scoket error.");
		exit(EXIT_FAILURE);
	}

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(atoi(argv[1]));
	addr.sin_addr.s_addr = INADDR_ANY;
	connect(sfd, (struct sockaddr *) &addr, sizeof(addr));

	char buffer[100];
	strcpy(buffer, "New player connected");
	send(sfd, buffer, strlen(buffer), 0);

	printf("What's your name?");
	fflush(stdout);
	char name[100];
	//	while(1) {
	//		char v;
	//		do { scanf("%c", &v); } while(v != '\n');
	scanf("%s", name);
	//	printf("%s", name);
	send(sfd, name, strlen(name), 0);

	int newPlayer;
	recv(sfd, &newPlayer, sizeof(int), 0);
	if(newPlayer){
		printf("List of available armors:\n");
		int i = 0;
		for(;i < 5; i++){
			printf("%d: %s (AC=%d)\n", i, armor[i].name, armor[i].protection);
		}
		int playerArmor;
		printf("\nChoose Player armor:");
		fflush(stdout);
		char v;
		do { scanf("%c", &v); } while(v != '\n');
		scanf("%d", &playerArmor);
		printf("Sending armor...\n");
		fflush(stdout);
		send(sfd, &playerArmor, sizeof(int), 0);
		printf("Armor sent\n");
		fflush(stdout);

		printf("List of available weapons:\n");
		i = 0;
		for(;i < 5; i++){
			printf("%d: %s (damage=%dd%d)\n", i, weapon[i].name, weapon[i].damage, weapon[i].rolls);
		}
		int playerWeapon;
		printf("\nChoose Player weapon:");
		fflush(stdout);
		do { scanf("%c", &v); } while(v != '\n');
		pickWeapon:
		scanf("%d", &playerWeapon);
		printf("%d\n", playerWeapon);
		fflush(stdout);
		if(playerWeapon == 52)
			goto pickWeapon;
		//		printf("%d", playerWeapon);
		send(sfd, &playerWeapon, sizeof(int), 0);
	}
	struct Player player;
	struct Player npc[10];
	printf("\nPlayer Setting Complete:\n");
	fflush(stdout);
	recv(sfd, &player, sizeof(struct Player), 0);
	printPlayerInfo(&player);
	printf("\n");
	recv(sfd, npc, sizeof(npc), 0);
	printLandOfMordor(npc, &player);
	while(1){
		start:
		fflush(stdout);
		printf("command >>");
		fflush(stdout);
		char command[100];
		scanf("%s", command);
		int commandNum;
		if(strcmp(command, "quit") == 0){
			commandNum = 4;
			send(sfd, &commandNum, sizeof(int), 0);
			break;
		}
		if(strcmp(command, "stat") == 0){
			commandNum = 3;
			send(sfd, &commandNum, sizeof(int), 0);
			recv(sfd, &player, sizeof(struct Player), 0);
			printPlayerInfo(&player);
			goto start;
		}
		if(strcmp(command, "look") == 0){
			commandNum = 2;
			send(sfd, &commandNum, sizeof(int), 0);
			recv(sfd, &player, sizeof(struct Player), 0);
			recv(sfd, npc, sizeof(npc), 0);
			printLandOfMordor(npc, &player);
			goto start;
		}
		char subcommand[10];
		memcpy(subcommand, command, 5);
		subcommand[5] = '\0';
		if(strcmp(subcommand, "fight") == 0){
			int num;
			scanf("%d", &num);
			if(num < 0 || num > 9){
				printf("Invalid Command!");
				goto start;
			}
			commandNum = 1;
			send(sfd, &commandNum, sizeof(int), 0);
			send(sfd, &num, sizeof(int), 0);
			char attackString[100];
			char * done = "Done attacking";
			while(1){
				recv(sfd, &attackString, 100, 0);
				if(strcmp(attackString, done) == 0)
					break;
				printf("%s", attackString);
			}
			char killString[200];
			recv(sfd, killString, 200, 0);
			printf("%s", killString);
			int kill;
			recv(sfd, &kill, sizeof(int), 0);
			if(kill){
				recv(sfd, killString, 200, 0);
				printf("%s", killString);
				char c;
				armor:
				do { scanf("%c", &c); } while(c != '\n');
				scanf("%c", &c);
				if(c != 'y' && c != 'n'){
					printf("Invalid input. Try again:");
					goto armor;
				}
				send(sfd, &c, sizeof(c), 0);

				recv(sfd, killString, 200, 0);
				printf("%s", killString);
				weapon:
				do { scanf("%c", &c); } while(c != '\n');
				scanf("%c", &c);
				if(c != 'y' && c != 'n'){
					printf("Invalid input. Try again:");
					goto weapon;
				}
				send(sfd, &c, sizeof(c), 0);
				recv(sfd, killString, 200, 0);
				printf("%s", killString);
			}


		}else{
			printf("Invalid command");
		}
	}
	printf("Saving and exiting...");
	close(sfd);

}

void printPlayerInfo(struct Player *p){
	printf("[%s: hp=%d, armor=%s, weapon=%s, level=%d, xp=%d]\n", p->name, p->health, armor[p->armor].name, weapon[p->weapon].name, p->level, p->xp);
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
