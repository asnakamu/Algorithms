
/**
 * CPE349 Counting Inversions Assignment
 * 
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;

public class InversionCounter
{
    
    public InversionCounter()
    {
    }
    
    /**
     *  Reads in the file with the given file name
     *  Returns an ArrayList containing the number of inversions for each set of rankings in the file
     */
    public ArrayList<Integer> countInversions(String filename) throws IOException
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        String sets = scan.next();
        int count = 0;
        while (scan.hasNextLine())
        {
            int ranks = scan.nextInt();
            for (int i = 0; i < ranks; i++)
            {
                arr.add(scan.nextInt());
            }
            count = findInversions(arr);
            result.add(count);
            arr.clear();
        }
        
        scan.close();
        return result;
    }
    
    public int findInversions(ArrayList<Integer> arr)
    {
        int count = 0, left = 0, right = 0;
        int n = arr.size();
        // If the array is big enough to split
        // Divide and conquer the lower + upper half
        if (n > 1)
        {
            ArrayList<Integer> high = new ArrayList<Integer>(arr.subList(n / 2, n));
            ArrayList<Integer> low = new ArrayList<Integer>(arr.subList(0, n / 2));
            
            // find the # of inversions in both halves
            left = findInversions(low);
            right = findInversions(high);
            // find the # of inversions across both halves
            count = findCross(low, high);
        }
        return count + left + right;
    }
    
    public int findCross(ArrayList<Integer> a, ArrayList<Integer> b)
    {
        int i = 0, j = 0, count = 0;
        int p = a.size();
        int q = b.size();
        // see if there are any inversions, using each element in a (the lower half)
        while (i < p)
        {
            // if there is an inversion, update the count and move to the next ranking
            if ((j < q) && (a.get(i) > b.get(j)))
            {
                count++;
                j++;
            }
            // if there isn't an inversion and there is still another ranking in b to check
            else if (j < q)
            {
                j++;
            }
            // if there isn't an inversion and we've checked everything in b
            // get a new value from a and start over
            else
            {
                i++;
                j = 0;
            }
        }
        
        return count;
    }
}
