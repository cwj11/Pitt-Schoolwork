Connor Johnson
Agorithm Implementation Project 2: Anagrams

Java Files: AnagramDecoder.java, StringTable.java

The main is in AnagramDecoder.java so use javac AnagramDecoder.java to compile the entire program

Description:
In StringTable, I have the dictionary stored in a hash tbale of length 100. The hash table is an array of linked lists where all the words
are stored. The hash function to find the storage location is the summation of all the letters as number(a=0, b=1, c=2, etc.) modulo 100.
To find the anagrams, I start off by using the recursive function getAllWords(). This function finds all the anagrams that could be made
from the input. These are including words are smaller than the input. Once this list is found, another recursive function is used to find
put together these words and to find all the solutions that have spces. Once a group of words is found, it is added to the list of solutions.
I found my program worked correctly in less than a minute for inputs up to 17 characters. Inputs 18 characters and over took around 2-3 minutes each.

Known errors:
My program didn't return any errors in all of the test cases I used.