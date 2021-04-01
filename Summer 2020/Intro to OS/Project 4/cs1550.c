/*
	FUSE: Filesystem in Userspace
	Copyright (C) 2001-2007  Miklos Szeredi <miklos@szeredi.hu>

	This program can be distributed under the terms of the GNU GPL.
	See the file COPYING.
*/

#define	FUSE_USE_VERSION 26

#include <fuse.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <stdlib.h>

//size of a disk block
#define	BLOCK_SIZE 512

//we'll use 8.3 filenames
#define	MAX_FILENAME 8
#define	MAX_EXTENSION 3

//How many files can there be in one directory?
#define MAX_FILES_IN_DIR (BLOCK_SIZE - sizeof(int)) / ((MAX_FILENAME + 1) + (MAX_EXTENSION + 1) + sizeof(size_t) + sizeof(long))

//The attribute packed means to not align these things
struct cs1550_directory_entry
{
	int nFiles;	//How many files are in this directory.
				//Needs to be less than MAX_FILES_IN_DIR

	struct cs1550_file_directory
	{
		char fname[MAX_FILENAME + 1];	//filename (plus space for nul)
		char fext[MAX_EXTENSION + 1];	//extension (plus space for nul)
		size_t fsize;					//file size
		long nIndexBlock;				//where the index block is on disk
	} __attribute__((packed)) files[MAX_FILES_IN_DIR];	//There is an array of these

	//This is some space to get this to be exactly the size of the disk block.
	//Don't use it for anything.
	char padding[BLOCK_SIZE - MAX_FILES_IN_DIR * sizeof(struct cs1550_file_directory) - sizeof(int)];
} ;

typedef struct cs1550_root_directory cs1550_root_directory;

#define MAX_DIRS_IN_ROOT (BLOCK_SIZE - sizeof(int) - sizeof(long)) / ((MAX_FILENAME + 1) + sizeof(long))

struct cs1550_root_directory
{
	long lastAllocatedBlock; //The number of the last allocated block
	int nDirectories;	//How many subdirectories are in the root
						//Needs to be less than MAX_DIRS_IN_ROOT
	struct cs1550_directory
	{
		char dname[MAX_FILENAME + 1];	//directory name (plus space for nul)
		long nStartBlock;				//where the directory block is on disk
	} __attribute__((packed)) directories[MAX_DIRS_IN_ROOT];	//There is an array of these

	//This is some space to get this to be exactly the size of the disk block.
	//Don't use it for anything.
	char padding[BLOCK_SIZE - MAX_DIRS_IN_ROOT * sizeof(struct cs1550_directory) - sizeof(int) - sizeof(long)];
} ;


typedef struct cs1550_directory_entry cs1550_directory_entry;

//How many entries can one index block hold?
#define	MAX_ENTRIES_IN_INDEX_BLOCK (BLOCK_SIZE/sizeof(long))

struct cs1550_index_block
{
      //All the space in the index block can be used for index entries.
			// Each index entry is a data block number.
      long entries[MAX_ENTRIES_IN_INDEX_BLOCK];
};

typedef struct cs1550_index_block cs1550_index_block;

//How much data can one block hold?
#define	MAX_DATA_IN_BLOCK (BLOCK_SIZE)

struct cs1550_disk_block
{
	//All of the space in the block can be used for actual data
	//storage.
	char data[MAX_DATA_IN_BLOCK];
};

typedef struct cs1550_disk_block cs1550_disk_block;

static void read_path(const char* path, char* directory, char* filename, char* extension){
	
	sscanf(path, "/%[^/]/%[^.].%s", directory, filename, extension);
}

static void* get_block(long num, FILE* disk){
	void *block = malloc(BLOCK_SIZE);
	fseek(disk, num*BLOCK_SIZE, SEEK_SET);
	fread(block, BLOCK_SIZE, 1, disk);
	return block;
}

static cs1550_root_directory* get_root(FILE *disk){
	return (cs1550_root_directory*) get_block(0, disk);
}

static int find_directory(char* directory, struct cs1550_directory_entry* entry){
	FILE *disk;
	disk = fopen(".disk", "rb");
	cs1550_root_directory *root = get_root(disk);
	int ret = -1;
	int i;
	for(i=0; i<root->nDirectories; i++){
		if(strcmp(root->directories[i].dname, directory) == 0){
			ret = i;
			void* block = get_block(root->directories[i].nStartBlock, disk);
			memcpy(entry, block, BLOCK_SIZE);
			free(block);
			break;
		}
	}
	fclose(disk);
	return ret;
}

static int find_file(cs1550_directory_entry* directory, char* filename, char* extension){
	int i = 0;
	while(strcmp(directory->files[i].fname, filename) != 0 || strcmp(directory->files[i].fext, extension) != 0){
		i++;
		if(i == directory->nFiles)
			return -1;
	}
	return i;
}

/*
 * Called whenever the system wants to know the file attributes, including
 * simply whether the file exists or not.
 *
 * man -s 2 stat will show the fields of a stat structure
 */
static int cs1550_getattr(const char *path, struct stat *stbuf)
{
	int res = 0;

	memset(stbuf, 0, sizeof(struct stat));

	//is path the root dir?
	if (strcmp(path, "/") == 0) {
		stbuf->st_mode = S_IFDIR | 0755;
		stbuf->st_nlink = 2;
	} else {

		char filename[MAX_FILENAME + 1];
		char directory[MAX_FILENAME + 1];
		char extension[MAX_EXTENSION + 1];
		
		read_path(path, directory, filename, extension);
		if(strlen(filename) > MAX_FILENAME || strlen(directory) > MAX_FILENAME || strlen(extension) > MAX_EXTENSION){
			return -ENAMETOOLONG;
		}
		struct cs1550_directory_entry* entry = (cs1550_directory_entry*) malloc(BLOCK_SIZE);
		int directory_loc = find_directory(directory, entry);
		
		if(directory_loc > -1){
			if(strlen(filename) == 0){
				//Might want to return a structure with these fields
				stbuf->st_mode = S_IFDIR | 0755;
				stbuf->st_nlink = 2;
				res = 0; //no error
			}else{
				int file_location = find_file(entry, filename, extension);
				if(file_location > -1){
					//regular file, probably want to be read and write
					stbuf->st_mode = S_IFREG | 0666;
					stbuf->st_nlink = 1; //file links
					stbuf->st_size = entry->files[file_location].fsize; //file size - make sure you replace with real size!
					res = 0; // no error
					
				}else{
					res = -ENOENT;
				}
			}
		} else{
		//Else return that path doesn't exist
			res = -ENOENT;
		}
		free(entry);
	}
	return res;
}


/*
 * Called whenever the contents of a directory are desired. Could be from an 'ls'
 * or could even be when a user hits TAB to do autocompletion
 */
static int cs1550_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
			 off_t offset, struct fuse_file_info *fi)
{
	//Since we're building with -Wall (all warnings reported) we need
	//to "use" every parameter, so let's just cast them to void to
	//satisfy the compiler
	(void) offset;
	(void) fi;
	
	
	char filename[MAX_FILENAME + 1];
	char directory[MAX_FILENAME + 1];
	char extension[MAX_EXTENSION + 1];
	
	filename[0] = '\0';
	directory[0] = '\0';
	extension[0] = '\0';
	read_path(path, directory, filename, extension);
	if(strlen(filename) > 0){
		return -ENOENT;
	}
	
	FILE *disk = fopen(".disk", "rb+");
	cs1550_root_directory *root = get_root(disk);

	//the filler function allows us to add entries to the listing
	//read the fuse.h file for a description (in the ../include dir)
	filler(buf, ".", NULL, 0);
	filler(buf, "..", NULL, 0);
	
	if(strcmp(path, "/") == 0){
		int i;
		for(i=0; i<root->nDirectories; i++){
			filler(buf, root->directories[i].dname, NULL, 0);
		}
	} else if(strcmp(directory, "") != 0){
		cs1550_directory_entry* entry = (cs1550_directory_entry*) malloc(BLOCK_SIZE);
		int dirLoc = find_directory(directory, entry);
		printf("%d", dirLoc);
		if(dirLoc < 0){
			fclose(disk);
			free(root);
			return -ENOENT;
		}
		int i;
		for(i=0; i<entry->nFiles; i++){
			filler(buf, strcat(strcat(entry->files[i].fname, "."), entry->files[i].fext), NULL, 0);
		}
		free(entry);
	}

	/*
	//add the user stuff (subdirs or files)
	//the +1 skips the leading '/' on the filenames
	filler(buf, newpath + 1, NULL, 0);
	*/
	free(root);
	fclose(disk);
	return 0;
}

/*
 * Creates a directory. We can ignore mode since we're not dealing with
 * permissions, as long as getattr returns appropriate ones for us.
 */
static int cs1550_mkdir(const char *path, mode_t mode)
{
	(void) path;
	(void) mode;
	

	
	char filename[MAX_FILENAME + 1];
	char directory[MAX_FILENAME + 1];
	char extension[MAX_EXTENSION + 1];
	
	filename[0] = '\0';
	directory[0] = '\0';
	extension[0] = '\0';
	read_path(path, directory, filename, extension);
	
	
	
	if(strlen(filename) > 0 || strlen(directory) < 1)
		return -EPERM;
	if(strlen(directory) > 8)
		return -ENAMETOOLONG;
	
	cs1550_directory_entry *entry = (cs1550_directory_entry*) malloc(BLOCK_SIZE);
	if(find_directory(directory, entry) > -1){
		free(entry);
		return -EEXIST;
	}
	
	entry = (cs1550_directory_entry*) malloc(BLOCK_SIZE);
	FILE *disk = fopen(".disk", "rb+");
	cs1550_root_directory *root = get_root(disk);
	if(root->nDirectories == MAX_DIRS_IN_ROOT){
		printf("Max directories reached");
		free(entry);
		free(root);
		return -EMLINK;
	}
	int numDir = root->nDirectories;
	strcpy(root->directories[numDir].dname, directory);
	root->directories[numDir].nStartBlock = root->lastAllocatedBlock+1;
	root->lastAllocatedBlock++;
	root->nDirectories++;
	fseek(disk, 0, SEEK_SET);
	fwrite(root, BLOCK_SIZE, 1, disk);
	
	entry->nFiles = 0;
	fseek(disk, root->directories[numDir].nStartBlock * BLOCK_SIZE, SEEK_SET);
	fwrite(entry, BLOCK_SIZE, 1, disk);
	
	fclose(disk);
	free(root);
	free(entry);
	
	return 0;
}

/*
 * Removes a directory.
 */
static int cs1550_rmdir(const char *path)
{
	(void) path;
    return 0;
}

/*
 * Does the actual creation of a file. Mode and dev can be ignored.
 *
 */
static int cs1550_mknod(const char *path, mode_t mode, dev_t dev)
{
	(void) mode;
	(void) dev;
	(void) path;
	
	
	char filename[MAX_FILENAME + 1];
	char directory[MAX_FILENAME + 1];
	char extension[MAX_EXTENSION + 1];
	
	filename[0] = '\0';
	directory[0] = '\0';
	extension[0] = '\0';
	read_path(path, directory, filename, extension);
	
	
	if(strlen(filename) > 8 || strlen(extension) > 3)
		return -ENAMETOOLONG;
	
	cs1550_directory_entry *entry = (cs1550_directory_entry*) malloc(BLOCK_SIZE);;
	int dirLoc = find_directory(directory, entry);
	if(dirLoc < 0){
		free(entry);
		return -EPERM;
	}
	int fileLoc = find_file(entry, filename, extension);
	if(fileLoc > -1)
		return -EEXIST;
	
	FILE *disk = fopen(".disk", "rb+");
	cs1550_root_directory *root = get_root(disk);
	
	int numBlock = root->lastAllocatedBlock;
	int dirBlock = root->directories[dirLoc].nStartBlock;
	root->lastAllocatedBlock++;
	fseek(disk, 0, SEEK_SET);
	fwrite(root, BLOCK_SIZE, 1, disk);
	
	if(entry->nFiles == MAX_FILES_IN_DIR){
		printf("Directory is full");
		return -EMLINK;
	}
	int index = entry->nFiles;
	entry->nFiles++;
	strcpy(entry->files[index].fname, filename);
	strcpy(entry->files[index].fext, extension);
	entry->files[index].fsize = 0;
	entry->files[index].nIndexBlock = numBlock;
	
	fseek(disk, dirBlock * BLOCK_SIZE, SEEK_SET);
	fwrite(entry, BLOCK_SIZE, 1, disk);
	
	free(root);
	free(entry);
	fclose(disk);
	
	
	
	
	return 0;
}

/*
 * Deletes a file
 */
static int cs1550_unlink(const char *path)
{
    (void) path;

    return 0;
}

/*
 * Read size bytes from file into buf starting from offset
 *
 */
static int cs1550_read(const char *path, char *buf, size_t size, off_t offset,
			  struct fuse_file_info *fi)
{
	(void) buf;
	(void) offset;
	(void) fi;
	(void) path;

	size = 0;

	return size;
}

/*
 * Write size bytes from buf into file starting from offset
 *
 */
static int cs1550_write(const char *path, const char *buf, size_t size,
			  off_t offset, struct fuse_file_info *fi)
{
	(void) buf;
	(void) offset;
	(void) fi;
	(void) path;

	return size;
}

/*
 * truncate is called when a new file is created (with a 0 size) or when an
 * existing file is made shorter. We're not handling deleting files or
 * truncating existing ones, so all we need to do here is to initialize
 * the appropriate directory entry.
 *
 */
static int cs1550_truncate(const char *path, off_t size)
{
	(void) path;
	(void) size;

    return 0;
}


/*
 * Called when we open a file
 *
 */
static int cs1550_open(const char *path, struct fuse_file_info *fi)
{
	(void) path;
	(void) fi;
    /*
        //if we can't find the desired file, return an error
        return -ENOENT;
    */

    //It's not really necessary for this project to anything in open

    /* We're not going to worry about permissions for this project, but
	   if we were and we don't have them to the file we should return an error

        return -EACCES;
    */

    return 0; //success!
}

/*
 * Called when close is called on a file descriptor, but because it might
 * have been dup'ed, this isn't a guarantee we won't ever need the file
 * again. For us, return success simply to avoid the unimplemented error
 * in the debug log.
 */
static int cs1550_flush (const char *path , struct fuse_file_info *fi)
{
	(void) path;
	(void) fi;

	return 0; //success!
}

/* Thanks to Mohammad Hasanzadeh Mofrad (@moh18) for these
   two functions */
static void * cs1550_init(struct fuse_conn_info* fi)
{
	  (void) fi;
    printf("We're all gonna live from here ....\n");
		return NULL;
}

static void cs1550_destroy(void* args)
{
		(void) args;
    printf("... and die like a boss here\n");
}


//register our new functions as the implementations of the syscalls
static struct fuse_operations hello_oper = {
    .getattr	= cs1550_getattr,
    .readdir	= cs1550_readdir,
    .mkdir	= cs1550_mkdir,
		.rmdir = cs1550_rmdir,
    .read	= cs1550_read,
    .write	= cs1550_write,
		.mknod	= cs1550_mknod,
		.unlink = cs1550_unlink,
		.truncate = cs1550_truncate,
		.flush = cs1550_flush,
		.open	= cs1550_open,
		.init = cs1550_init,
    .destroy = cs1550_destroy,
};

//Don't change this.
int main(int argc, char *argv[])
{
	return fuse_main(argc, argv, &hello_oper, NULL);
}
