/**
 * NAME         :       JEET AGRAWAL
 * NSID         :       jea316
 * STUDENT ID    :       11269096
  */
package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

 import lib280.base.Pair280;
 import lib280.exception.InvalidArgument280Exception;
 import lib280.list.LinkedList280;

 import java.util.InputMismatchException;
 import java.util.Scanner;


 public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
         WeightedGraphAdjListRep280<V> {

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
                                                  String vertexTypeName) {
         super(cap, d, vertexTypeName);
     }

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
         super(cap, d);
     }

     /**
      * Replaces the current graph with a graph read from a data file.
      *
      * File format is a sequence of integers. The first integer is the total
      * number of nodes which will be numbered between 1 and n.
      *
      * Remaining integers are treated as ordered pairs of (source, destination)
      * indicies defining graph edges.
      *
      * @param fileName
      *            Name of the file from which to read the graph.
      * @precond The weights on the edges in the data file fileName are non negative.
      * @throws RuntimeException
      *             if the file format is incorrect, or an edge appears more than
      *             once in the input.
      */


     @Override
     public void setEdgeWeight(V v1, V v2, double weight) {
         // Overriding this method to throw an exception if a weight is negative will cause
         // super.initGraphFromFile to throw an exception when it tries to set a weight to
         // something negative.

         // Verify that the weight is non-negative
         if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

         // If it is, then just set the edge weight using the superclass method.
         super.setEdgeWeight(v1, v2, weight);
     }

     @Override
     public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
         // Get the vertex objects associated with each index and pass off to the
         // version of setEdgeWEight that accepts vertex objects.
         this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
     }


     /**
      * Implementation of Dijkstra's algorithm.
      * @param startVertex Start vertex for the single-source shortest paths.
      * @return An array of size G.numVertices()+1 in which offset k contains the shortest
      *         path from startVertex to k.  Offset 0 is unused since vertex indices start
      *         at 1.
      */
     public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
         // TODO Implement this method

         boolean visited[] = new boolean[this.numVertices()+1];
         double tentativeDistance[] = new double[this.numVertices()+1];
         int predecessorNode[] = new int[this.numVertices()+1];

         for (int i = 0; i < numVertices()+1; i++){
             tentativeDistance[i] = 2e100;
             visited[i] = false;
             predecessorNode[i] = -1;
         }

         tentativeDistance[startVertex] = 0;

         // To find the minimum index of the unvisited vertex with the smallest tentative distance.

         int i = visited.length;
         while ( i > 0) {

             int index = minIndex(tentativeDistance,visited);
             if (index == 0){
                 break;
             }
             visited[index] = true;

             this.eGoFirst(this.vertex(index));
             while (this.eItemExists()) {
                 if (!visited[this.eItem().secondItem().index()] && tentativeDistance[this.eItem().secondItem().index()] > (tentativeDistance[index] + getEdgeWeight(index, this.eItem().secondItem().index()))) {
                     tentativeDistance[this.eItem().secondItem().index()] = tentativeDistance[index] + getEdgeWeight(index, this.eItem().secondItem().index());
                     predecessorNode[this.eItem().secondItem().index()] = index;
                 }
                 this.eGoForth();
             }
             i--;
         }

         // Remove this return statement when you're ready -- it's a placeholder to prevent a compiler error.
         return new Pair280<>(tentativeDistance, predecessorNode);
     }
     private int minIndex(double[] arr,boolean[] arr1){

         int index = 0;
         for (int i = 1; i < arr.length; i++){
             if (!arr1[i]){
                 if (index == 0 || arr[index] > arr[i]){
                     index = i;
                 }
             }
         }
         return index;
     }

     // Given a predecessors array output from this.shortestPathDijkatra, return a string
     // that represents a path from the start node to the given destination vertex 'destVertex'.
     private static String extractPath(int[] predecessors, int destVertex) {
         // TODO Implement this method

         LinkedList280<String> temp = new LinkedList280<>();
         temp.insert(Integer.toString(destVertex));
         int index = predecessors[destVertex];

         if (index == -1){
             return "Not reachable.";
         }

         while (index != -1){
             temp.insert(Integer.toString(index));
             index = predecessors[index];
         }

         return temp.toString();  // Remove this when you're ready -- this is a placeholder to prevent a compiler error.
     }

     // Regression Test
     public static void main(String args[]) {
         NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

         if( args.length == 0)
             G.initGraphFromFile("/Users/jeetagrawal/Desktop/U OF S/WINTER 2020/CMPT 280/ASSIGNMENTS/ASSIGNMENT 8/src/lib280/graph/weightedtestgraph.gra");
         else
             G.initGraphFromFile(args[0]);

         System.out.println("Enter the number of the start vertex: ");
         Scanner in = new Scanner(System.in);
         int startVertex;
         try {
             startVertex = in.nextInt();
         }
         catch(InputMismatchException e) {
             in.close();
             System.out.println("That's not an integer!");
             return;
         }

         if( startVertex < 1 || startVertex > G.numVertices() ) {
             in.close();
             System.out.println("That's not a valid vertex number for this graph.");
             return;
         }
         in.close();


         Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
         double[] finalDistances = dijkstraResult.firstItem();
         //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
         int[] predecessors = dijkstraResult.secondItem();

         for(int i=1; i < G.numVertices() +1; i++) {
             System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
 //			if( correctDistances[i] != finalDistances[i] )
 //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
 //			else {
                 System.out.println(extractPath(predecessors, i));
 //			}
         }
     }

 }