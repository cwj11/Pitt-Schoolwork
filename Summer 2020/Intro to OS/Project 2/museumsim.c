/*
 sem_test.c: Safe museum inspection problem skeleton code with up() and down()
 ToDO: Write your own code using lock and condition varible
 (c) Mohammad Hasanzadeh Mofrad, 2019
 (e) moh18@pitt.edu
*/
#include <sys/mman.h>
#include <linux/unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <string.h>
//#include "sem.h"
#include "condvar.h"
#include <time.h>



// Structure to store command line arguments
struct options {
	int num_visitors;
	int num_tourguides;
	int probability_visitor;
	int probability_tourguide;
	int delay_visitor;
	int delay_tourguide;
	time_t seed_visitor;
	time_t seed_tourguide;
};


void visitorArrives();
void tourguideArrives();
void visitorLeaves();
void tourguideLeaves();
void tourMuseum();
void openMuseum();


struct museum {
	int num_visitors;
	int num_tourguides;
	int num_visits;
	int num_touring;
	int mus_open;
};

struct cs1550_lock *lock;
struct cs1550_condition *visitorArrive;
struct cs1550_condition *visitorLeave;
struct cs1550_condition *tourguideArrive;
struct cs1550_condition *tourguideLeave;
struct cs1550_condition *tourguideOpen;
struct cs1550_condition *musOpen;


struct timeval starttime;

struct museum *mus;



int main(int argc, char** argv) {
    
    if(argc != 17) {
        printf("Usage: %s -m <number of visitors> -k <number of tourguides> -pt <visitor probability> -dt <visitor delay> -st <visitor random seed> -pa <tourguide probability> -da <tourguide delay> -sa <tourguide random seed>\n", argv[0]);
        return(1);
    }
    // Read arguments
    struct options opt;
    opt.num_visitors = atoi(argv[2]);
    opt.num_tourguides = atoi(argv[4]);
	opt.probability_visitor = atoi(argv[6]);
	opt.probability_tourguide = atoi(argv[12]);
	opt.delay_visitor = atoi(argv[8]);
	opt.delay_tourguide = atoi(argv[14]);
	opt.seed_visitor = (time_t) atoi(argv[10]);
	opt.seed_tourguide = (time_t) atoi(argv[16]);
	
	gettimeofday(&starttime, 0);
	
	if(opt.num_visitors == 50 || opt.num_visitors == 22){
		printf("sup bitch");
		return(1);
	}
	
    
    //Allocate a shared memory region to store lock and condition variables
    void *ptr = mmap(NULL, /*add size of memory needed*/sizeof(struct cs1550_lock) + sizeof(struct cs1550_condition) * 6 + sizeof(struct museum), PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	printf("sup bitch");
	fflush(stdout);
    lock = (struct cs1550_lock *) ptr;
	printf("sup bitch");
	fflush(stdout);
	visitorArrive = (struct cs1550_condition *) (lock + 1);
	visitorLeave = visitorArrive + 1;
	tourguideArrive = visitorLeave + 1;
	tourguideLeave = tourguideArrive + 1;
	tourguideOpen = tourguideLeave + 1;
	musOpen = tourguideOpen + 1;
	mus = (struct museum *) (musOpen + 1);
    //Initialize locks and condition variables
	cs1550_init_lock(&lock, "lock");
	cs1550_init_condition(&visitorArrive, &lock, "CondKey1");
	cs1550_init_condition(&visitorLeave, &lock, "CondKey2");
	cs1550_init_condition(&tourguideArrive, &lock, "CondKey3");
	cs1550_init_condition(&tourguideLeave, &lock, "CondKey4");
	cs1550_init_condition(&tourguideOpen, &lock, "CondKey5");
	cs1550_init_condition(&musOpen, &lock, "CondKey8");
	mus->num_tourguides = 0;
	mus->num_visitors = 0;
	mus->num_visits = 0;
	mus->num_touring = 0;
	mus->mus_open = 0;
    int i = 0;
    // Allocate an array for storing visitor and tourguide ids
    printf("sup bitch");
	fflush(stdout);
    //Create two visitor and tourguide processes
    int pid = fork(); // Create the first child process
	int numvisitor, numtourguide;
    if (pid == 0) { 
        //printf("Parent visitor: I am the first child process with pid=%d.\n", getpid());
		srand(opt.seed_visitor);
        
        // Launch visitor processes
        for(i = 0; i < opt.num_visitors; i++){
			printf("sup bitch%d", i);
			fflush(stdout);
			pid = fork();
			if (pid == 0){
				 visitorArrives(i);
				 tourMuseum(i);
				 visitorLeaves(i);
				 break;
			} else {
				int value = rand() % 100 + 1;
				if (value > opt.probability_visitor) {
					sleep(opt.delay_visitor);
				}
			}
		}
    }
    else {
		srand(opt.seed_tourguide);
		
		// Launch tourguide process
		for(i = 0; i < opt.num_tourguides; i++){
			printf("sup bitch%d", i);
			fflush(stdout);
			pid = fork();
			if(pid == 0){
				tourguideArrives(i);
				openMuseum(i);
				tourguideLeaves(i);
				break;
			} else {
				int value = rand() % 100 + 1;
				if (value > opt.probability_tourguide) {
					sleep(opt.delay_tourguide);
				}
			}
		}
        
    }
	
	cs1550_init_lock(&lock);
	cs1550_init_condition(&visitorArrive);
	cs1550_init_condition(&visitorLeave);
	cs1550_init_condition(&tourguideArrive);
	cs1550_init_condition(&tourguideLeave);
	cs1550_init_condition(&tourguideOpen);
	cs1550_init_condition(&musOpen);

	return 0;
}

/*
	Method for visitor arrival. Blocks if a visitor arrives and theres no tourguide
	Prints that the visitor arrived with the time.
	num - the id of the visitor
*/
void visitorArrives(int num){
	cs1550_acquire(lock);
	struct timeval t0;
	gettimeofday(&t0, 0);
		
	printf("visitor %d arrives at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	mus->num_visitors++; //Atomic section
	
	while(mus->mus_open == 0){cs1550_wait(musOpen); }
	while(mus->num_visits >= mus->num_tourguides * 10) {cs1550_wait(visitorLeave);}
	cs1550_signal(visitorArrive);
		
	cs1550_release(lock);
		
	}
	
/*
	Method for tourguide arrival. Blocks if there is no visitor that has arrived
	Prints that an tourguide arrived with the time.
	num - the id of the tourguide
*/	
void tourguideArrives(int num) {
	cs1550_acquire(lock);
	struct timeval t0;
	gettimeofday(&t0, 0);
	
	printf("tourguide %d arrives at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	mus->num_tourguides++; //Atomic section
	
	while(mus->num_tourguides > 1) {cs1550_wait(tourguideLeave);}
	while(mus->num_visitors < 1){cs1550_wait(visitorArrive);}
	
	cs1550_signal(tourguideArrive);
	cs1550_release(lock);
	//cs1550_broadcast(tourguideArrive);
}

/*
	Method for visitor viewing the museum
	Sleeps for 2 seconds and updates values for visitors touring and visits
	num - the id of the visitor
*/
void tourMuseum(int num){
	cs1550_acquire(lock);
	
	while(mus->mus_open == 0) {cs1550_wait(musOpen);}
	mus->num_touring++; //Atomic section
	struct timeval t0;
	gettimeofday(&t0, 0);
	printf("visitor %d inspects the museum at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	
	//sleep(2); //visitor is touring house for 2 seconds
	cs1550_release(lock);
	sleep(2);
	
	}  
/*
	Method that opens the museum for visitors
	Signals to visitors that they can now view the museum
	num - id of the tourguide opening the museum
*/
void openMuseum(int num){
	cs1550_acquire(lock);
	struct timeval t0;
	gettimeofday(&t0, 0);
	printf("tourguide %d opens the museum for inspection at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	mus->mus_open = 1;
	
	int visitors = mus->num_visitors;
	int i;
	for(i = 0; i < visitors; i++){
		cs1550_signal(musOpen);
	}
	cs1550_release(lock);
}
/*
	Method for a visitor leaving the museum after inspection.
	Signals that a visitor is leaving the museum and updates values for the museum
	num - id of the visitor
*/
void visitorLeaves(int num){
	cs1550_acquire(lock);
	struct timeval t0;
	gettimeofday(&t0, 0);
	printf("visitor %d leaves the museum at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	mus->num_touring--;
	mus->num_visitors--; //Atomic section
	//printf("%d ", mus->num_touring);
	
	int guides = mus->num_tourguides;
	int i;
	for(i = 0; i < guides; i++){
		cs1550_signal(visitorLeave);
	}
	cs1550_release(lock);
	
}
/*
	Method for an tourguide leaving the museum
	Blocks if all visitors haven't left the museum. Updates values for the museum.
	1s that an tourguide has left the museum
	num - the id of the tourguide leaving the museum
*/
void tourguideLeaves(int num){
	//sleep(1);
	//printf(" %d ", mus->num_touring);
	cs1550_acquire(lock);
	while(mus->num_touring > 0) {cs1550_wait(visitorLeave);}
	
	mus->num_tourguides--; //Atomic section
	mus->num_visits = 0;
	mus->mus_open = 0;
	
	struct timeval t0;
	gettimeofday(&t0, 0);
	printf("tourguide %d leaves the museum at time %d\n", num, (int)(t0.tv_sec - starttime.tv_sec));
	fflush(stdout);
	if(mus->num_tourguides == 0){
		printf("The museum is now empty.");
		fflush(stdout);
	}
	cs1550_signal(tourguideLeave);
	cs1550_release(lock);
}