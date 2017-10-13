import java.io.*;
import java.util.*;

public class dyn {
    Scanner in = null;
    ArrayList<Integer> maxtot = new ArrayList<Integer>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    int num = 0;
    int use = 0;

    public void getNumbers(String file) throws IOException {
        int k = 1;
        try
        {
            in = new Scanner(new File(file)); //Start scanning given file

            num = in.nextInt(); //get total number of sets
            for(int j = 0; j < num; j++) 
            {
                use = in.nextInt(); //get items in this set
                price.add(0);
                for(int i = 0; i < use; i++) 
                {
                    price.add(in.nextInt()); //Store items into array in order read in
                }

                System.out.printf("Case" + k++ + ":\n");
                maxTotal(use);
                maxtot.clear();
                price.clear();
                System.out.printf("\n");
            }
        } 
        finally 
        {
            if(in != null)
            {
                in.close(); //close the file
            }
        }
    }

    public void maxTotal(int use) 
    {
        // add the base case values
        maxtot.add(0);
        maxtot.add(price.get(1));
        int n = price.size();

        // starting from the first non-base case, loop through the price array
        // compare the sale price at length i to the total sale price of
        // all possible cuts in between length 1 and length i
        for (int i = 2; i < n; i++)
        {   
            int max = price.get(i);
            // go through prices until i/2, because then there will be repeats
            // the price of cutting length 1 and 4 is the same as cutting length 4 and 1
            for (int j = 1; j <= i/2; j++)
            {
                max = Math.max(max, maxtot.get(j) + maxtot.get(i - j));
            }
            maxtot.add(max);
        }

        //print out the resulting cuts
        int m = maxtot.size();
        for (int i = 1; i < m; i++)
        {
            System.out.printf("total for length " + i + "\t= " +  maxtot.get(i) + "\n");
        }

        //trace back
        int trace[];
        trace = new int[n];
        int leftover = n - 1;
        int total = maxtot.get(n - 1);
        boolean done = false;
        for (int i = n - 1; i >= 1; i--)
        {
            //assume a cut if the max total sale price of rod length i 
            //equals the price for a single rod of length i
            if (maxtot.get(i) == price.get(i))
            {
                trace[i]++;
                //decrement the remaining total sale price we must find 
                //and the leftover rod size we have
                total = total - price.get(i);
                leftover -= i;
                int save = i;

                // if the leftover is 0, you're done
                if (leftover == 0)
                {
                    done = true;
                    i = 0;
                }              
                //otherwise check all possible the smaller rod lengths
                for (int j = 1; j <= leftover; j++)
                {
                    if (total == price.get(j))
                    {
                        trace[j]++;
                        done = true;
                        i = 0;
                    }
                }

                //if our leftover is going to be zero once we start the loop again
                //but we don't get the max total sale price that way,
                //we've got the wrong index, try again
                if (!done && leftover - i == 0 && total - price.get(i) != 0)
                {
                    trace[i]--;
                    leftover += i;
                    i = save;
                }
                //otherwise we need another rod of the same length cut
                else if (!done)
                {
                    trace[i]++;
                    leftover = leftover - i;
                    total = total - price.get(i);
                    i = leftover + 1;
                }
            }
        }

        //print out the resulting cuts
        System.out.printf("\n");
        for (int i = 1; i < n; i++)
        {
            if (trace[i] > 0)
            {
                System.out.printf("# of rods of length " + i + "\t= " + trace[i] + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        dyn mine = new dyn();
        mine.getNumbers("input.txt");
    }
}
