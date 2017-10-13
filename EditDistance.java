import java.io.*;
import java.util.*;

/**
 * Sequence Alignment project
 *      reads two strings of characters from a text file
 *      then computes and prints the edit distance between the two strings
 *      lastly, recovers the optimal alignment 
 *      and prints it out along with the individual penalties
 *      
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */
public class EditDistance
{
    private int table[][];
    
    /**
     * Gap Penalty: 2
     * Mismatch Penalty: 1
     * Matching Penalty: 0
     */    
    public void getEditDist(String filename, boolean printOut) throws IOException
    {
        /**
         * SET UP SECTION
         */
        // get the input
        Scanner input = new Scanner(new File(filename));
        String s1 = input.nextLine();
        String s2 = input.nextLine();
        int n = s1.length();
        int m = s2.length();
        input.close();
        
        // initialize table
        table = new int[n + 1][m + 1];
        table[0][0] = 0;
        for (int i = 1; i < n + 1; i++)
        {
            table[i][0] = table[i - 1][0] + 2;
        }
        for (int i = 1; i < m + 1; i++)
        {
            table[0][i] = table[0][i - 1] + 2;
        }
        
        /**
         * TABLE CONSTRUCTION SECTION
         */
        // create a 2D table
        // get the minimum edit distance
        for (int i = 0; i < n; i++)
        {
            int penalty;
            for (int j = 0; j < m; j++)
            {
                // find whether to add mismatch penalty or match penalty
                if (s1.charAt(i) == s2.charAt(j))
                {
                    penalty = 0;
                }
                else
                {
                    penalty = 1;
                }
                
                // put into the current spot the minimum of the top + gap penalty,
                // the left + gap penalty, and the diagonal + mismatch/match penalty
                table[i + 1][j + 1] = Math.min(table[i][j + 1] + 2, table[i + 1][j] + 2);
                table[i + 1][j + 1] = Math.min(table[i + 1][j + 1], table[i][j] + penalty);
            }
        }
        System.out.printf("Edit distance = " + table[n][m] + "\n");
        
        /**
         * TRACE BACK SECTION
         */
        // starting from the bottom right corner of the 2D table
        ArrayList<String> result = new ArrayList<String>();
        int i = n - 1, j = m - 1, penalty;
        while (i >= 0 && j >= 0)
        {
            // find whether to add mismatch penalty or match penalty
            if (s1.charAt(i) == s2.charAt(j))
            {
                penalty = 0;
            }
            else
            {
                penalty = 1;
            } 
            
            // check if the current value came from adding a gap in the 2nd string
            //      which would be a VERTICAL MOVE
            if (table[i + 1][j + 1] == table[i][j + 1] + 2)
            {
                result.add(s1.charAt(i) + " " + "-" + " " + 2 + "\n");
                i--;
            }
            // or adding a gap in the 1st string
            //      which would be  HORIZONTAL MOVE
            else if (table[i + 1][j + 1] == table[i + 1][j] + 2)
            {
                result.add("-" + " " + s2.charAt(j) + " " + 2 + "\n");
                j--;
            }
            // or leaving the mismatch/match as is
            //      which would be a LEFT DIAGONAL MOVE
            else if (table[i + 1][j + 1] == table[i][j] + penalty)
            {
                result.add(s1.charAt(i) + " " + s2.charAt(j) + " " + penalty + "\n");
                i--;
                j--;
            }
        }
        
        // check for gaps made by unequal string lengths
        // add them to the optimal alignment list
        while (i >= 0)
        {
            result.add(s1.charAt(i) + " " + "-" + " " + 2 + "\n");
            i--;
        }
        while (j >= 0)
        {
            result.add("-" + " " + s2.charAt(j) + " " + 2 + "\n");
            j--;
        }
        
        // print out the optimal alignment
        if (printOut)
        {
            int size = result.size();
            for (i = size - 1; i >= 0; i--)
            {
                System.out.printf(result.get(i));
            }
        }
    }
    
    public static void main(String[] args) throws IOException
    {        
        EditDistance ed = new EditDistance();
        ed.getEditDist("testEcoli2500.txt", false);
    }
}
