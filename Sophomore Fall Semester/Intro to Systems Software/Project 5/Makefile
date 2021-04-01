all: rpg_client rpg_server

rpg_client: rpg_client.o
	gcc -lpthread -o rpg_client rpg_client.o
	
rpg_client.o: rpg_client.c common.h
	gcc -o rpg_client.o -c rpg_client.c
	
rpg_server: rpg_server.o
	gcc -lpthread -o rpg_server rpg_server.o 
	
rpg_server.o: rpg_server.c common.h
	gcc -o rpg_server.o -c rpg_server.c