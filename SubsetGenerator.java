/**
 * CPE 349 Assignment: Permutations
 *
 * @author Amy Nakamura
 * @Calpoly ID: asnakamu
 */

// Add any other important imports you may need
import java.util.ArrayList;

public class SubsetGenerator {

   public static ArrayList<String> generateSubsets(String s) {
      
      // TODO: Implement Lexicographic Permutation here
      ArrayList<String> A = new ArrayList<String>();
      ArrayList<String> temp = new ArrayList<String>();
      int size = s.length();

      if (size > 0)
      {
         temp = generateSubsets(s.substring(0, size - 1));
         // now loop over tepm and create the subsets with and
         // without the last character of the original string
         for (int i = 0; i < temp.size(); i++)
         {
            A.add(temp.get(i));
            A.add(temp.get(i) + " " + s.charAt(size - 1));
         }
         return A;
      }
      else
      {
         A.add("");
         return A;
      }
   }

   public static ArrayList<String> generateGrayCode(int element) {
      
      // TODO: Implement Minimum Change Permutation here
      if (element == 0)
      {
         ArrayList<String> result = new ArrayList<String>();
         result.add("");
         return result;
      }
      else
      {
      
         ArrayList<String> call = generateGrayCode(element - 1);
         ArrayList<String> fin = new ArrayList<String>();
         int total = call.size();
         for (int i = 0; i < total; i++)
         {
            fin.add("0" + call.get(i));
         }
         for (int i = total - 1; i >= 0; i--)
         {
            fin.add("1" + call.get(i));
         }

         return fin;
      }
   }
}
