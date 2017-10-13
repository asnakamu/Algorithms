/**
 * DestTesterClass for CPE 349 Lab 1 Destructive Testing
 * 
 * @author Amy Nakamura
 * CalPoly UserId: asnakamu
 * 
 */

import java.util.ArrayList;

public class DestTesterClass {
   /**
    * @return An ArrayList<Integer> that has these in the following order:
    * 0 : height of the ladder (input parameter)
    * 1 : actual highest safe run (input parameter)
    * 2 : Highest safe rung determined by this algorithm
    * 3 : Rung where the first test device broke
    * 4 : Rung where the second test device broke
    * 5 : Total number of drops required to find the highest safe rung*
    */
   public static ArrayList<Integer> findHighestSafeRung(int height, int safe) {
      ArrayList<Integer> result = new ArrayList<Integer>();
      
      result.add(height); // Adding Height of Ladder
      result.add(safe);   // Adding Safe Rung of Ladder
      
      int first = 0, second = 0, drop = 0;
      int interval = (int)(Math.sqrt(height));  // Get the interval you'll be using to test the rungs1
      //Drop the first test device at multiples of the interval
      for (int i = interval; i < height + interval; i += interval)
      {
          drop++;  // Increment the drop counter
          if (i > safe)
          {
              first = i;  // The first test device has broken
              //if the increment results in a rung higher than the size, we'll drop it off the top
              if (first > height)
              {
                  first = height;
              }
              i = height + interval;
          }
      }
      
      // We now know the highest safe rung is between first - interval and first
      // Drop the second test device at every rung between those numbers
      for (int i = first - interval; i <= first; i++)
      {
          drop++;  // Increment the drop counter
          if (i > safe)
          {
              second = i;  // The second test device has broken
              i = first + 1;
          }
      }
      
      // Add results to the array in proper order
      result.add(second - 1);  // Highest safe rung determined by algorithm
      result.add(first);       // Rung the first test device broke at
      result.add(second);      // Rung the second test device broke at
      result.add(drop);        // Number of drops it took      

      return result;
   }
}
