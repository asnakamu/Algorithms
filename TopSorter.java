/**
 * CPE 349 Assignment: Source Removal Topological Sort
 *
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */
// Do not use "package"
// Add any other important imports you may need
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.io.*;

public class TopSorter 
{

   public static ArrayList<Integer> topSortGenerator(String s) throws FileNotFoundException
   {
      // s is the filename for the input graph
      // TODO: Implement topological sorting

      ArrayList<Integer> sorted = new ArrayList<Integer>();      
      GraphStart myGraph = new GraphStart();
      myGraph.readfile_graph(s);
      // Use a priority queue so, in case of multiple 0's, natural ordering
      PriorityQueue<Integer> queue = new PriorityQueue<Integer>(myGraph.nvertices);
      ArrayList<Integer> allv = myGraph.verts;
      
      // while there are still vertices in the graph to check
      while (!allv.isEmpty())
      {
          // find all vertices with IN DEGREE of 0
          // aka, find the sources to remove and put them in a queue
          // change their IN DEGREE so that they won't show up again
          for (int i = 0; i < allv.size(); i++)
          {
              int c = allv.get(i);
              if (myGraph.degree[c] == 0)
              {
                  queue.offer(c);
                  myGraph.degree[c] = -1;
              }
          }
    
          // go through the queue and poll the sources
          // remove the current source from the list of unchecked vertices
          // add it to the topological sort array
          while (!queue.isEmpty())
          {
              int current = queue.poll();
              allv.remove((Integer)current);
              sorted.add(current);
              // remove all out going edges from the current source
              for (int i = 0; i < allv.size(); i++)
              {
                  int c = allv.get(i);
                  if (myGraph.edges[current].indexOf(c) != -1)
                  {
                      myGraph.remove_edge(current, c);
                      System.out.printf("Removed edge " + current + "->" + c + "\n");
                  }
              }
              System.out.printf("Now " + current + " has in-degree " + myGraph.degree[current] +
                  " and out-degree " + myGraph.outdegree[current] + "\n");
          }
      }
      
      // if the graph can be only partially sorted, insert -1's for missing vertices
      while (sorted.size() != myGraph.nvertices)
      {
          sorted.add(-1);
      }
      return sorted;
   }

}
