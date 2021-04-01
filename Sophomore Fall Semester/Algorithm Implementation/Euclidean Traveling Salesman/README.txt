Connor Johnson
Algorithm Implementation Project 3: Euclidean Traveling Salesman

Java File: City.java, DemonstrateEuclideanTSP.java, Edge.java, EdgeWeightedGraph.java, EuclideanTSP.java, IndexMinPQ, Map.java

Compile and Run: the main is in DemonstrateEuclideanTSP.java Compile and run this file

Description:
I used Prim's alogrithm to find the minimum spanning tree and a bfs to find the preorder walk. For Prim's algorithm, I used a
index min priority queue from the textbook so I could do the eager form of the algorithm. I would change the list of cities
into a graph in EdgeWeightedGraph and then use this for the algorithm. The variables were stored as static variables in
EuclideanTSP.java. Once the MST was found, I used the list of edges to create a new graoh and did a bfs on this to find the 
preorder walk tree. I tested the program with inputs of 1000+ plus in size and it worked in a reasonable time.

Errors: I found no errors when testing my program. It worked in a reasonable amount of time in all of my tests.