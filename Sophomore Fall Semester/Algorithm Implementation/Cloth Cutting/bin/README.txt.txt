Connor Johnson
Cloth Cutting Project

List of all files that should be in this folder: Cloth.java, ClothCutter.java, ClothPiece.java, Cut.java, Garment.java, Pattern.java, TestClothCutter.java

The main method is loacted in TestClothCutter.java; this is the file that should be compiled and executed

Paragraph on Project:
My program uses a memoized recursive function to traverse all the possible ways that a cloth could be cut up. It uses this to find
the maximum value that can be produced by cutting up the cloth into n products. I created a new class called ClothPiece to keep track 
of the pieces of cloth. This class contained the cuts and garments that made up the cloth, as well as the size and its overall value
I used this object as the return value of my optimize function. This made it easier to keep track of which garments and cuts would be 
used in the final product. I also had the memo stored in the ClothCutter class. The memo is a two-dimensional array. The location of each
cloth size found and its value can be found in memo[width][height]. Using this, I was able to create an algoritm that can handle input 
sizes in the hundreds and complete correctly in a reasonable amount of time.

Known Problems: There are no known problems my program has. All my tests were exectued correctly.