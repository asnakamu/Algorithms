/**
 * CPE 349 Assignment: Permutations
 *
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */

// Add any other important imports you may need
import java.util.ArrayList;
import java.util.ArrayList;

public class Permutations 
{
   public static ArrayList<String> lexPermGenerator(String s) {
      // TODO: Implement Lexicographic Permutation here
      int size = s.length();
      
      if (size == 1)
      {
          // if string is a single character, return itself in an array
          ArrayList<String> A = new ArrayList<String>();
          A.add(s);
          return A;
      }
      else
      {      
          ArrayList<String> A = new ArrayList<String>();
          ArrayList<String> temp = new ArrayList<String>();
          
          // loop through every char in the string
          for (int i = 0; i < size; i++)
          {
              // create a simpler word by getting rid of that char
              String store = s.substring(0, i) + s.substring(i + 1, size);
              // recursively generate permutations with the new word
              temp = lexPermGenerator(store);
              
              // add the char you got rid of to the front of every permutation
              for (int j = 0; j < temp.size(); j++)
              {
                  A.add(s.charAt(i) + temp.get(j));
              }
          }
          return A;
      }
   }

   public static ArrayList<String> minChangePermGenerator(String s) {
      // TODO: Implement Minimum Change Permutation here
      int size = s.length();
      
      if (size == 1)
      {
          // if string is a single character, return itself in an array
          ArrayList<String> A = new ArrayList<String>();
          A.add(s);
          return A;
      }
      else
      {      
          ArrayList<String> A = new ArrayList<String>();
          ArrayList<String> temp = new ArrayList<String>();
          
          // recursively generate permutations with n - 1 length
          temp = minChangePermGenerator(s.substring(0, size - 1));
          int count = 2;
          
          for (int index = 0; index < temp.size(); index++)
          {
              String current = temp.get(index);
              int length = current.length();
              
              // Alternate from right to left and left to right
              // if permutation is even, go RIGHT TO LEFT
              if (count%2 == 0)
              {
                  A.add(current + s.charAt(size - 1));
                  //swap RIGHT TO LEFT
                  for (int i = length - 1; i >= 0; i--)
                  {
                      // insert the nth character 
                      StringBuilder keep = new StringBuilder(current).insert(i, s.charAt(size - 1));
                      String store = keep.toString();
                      A.add(store);
                  }
                  count++;
              }
              // if permutation is odd, go LEFT TO RIGHT
              else
              {
                  //swap LEFT TO RIGHT
                  for (int i = 0; i <= length - 1; i++)
                  {
                      // insert the nth character
                      StringBuilder keep = new StringBuilder(current).insert(i, s.charAt(size - 1));
                      String store = keep.toString();
                      A.add(store);
                  }
                  A.add(current + s.charAt(size - 1));
                  count++;
              }
          }
          
          return A;
      }
   }
}
