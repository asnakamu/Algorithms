import java.util.*;

public class SubsetSum   { 

	static private boolean findsubset(int index, int sum, int[] a, int goal, boolean[] ans)  {
	// returns false if too big - true if hit goal - keeps going if less than goal and items left
		boolean hitGoal; 

		// see if can hit the goal by including the integer at a[index]
		// since a is in increasing order this means later elements will fail
		int tempSum = sum + a[index];
		if (tempSum > goal) {
                        ans[index] = false;
			return false;
		}
		else if (tempSum == goal){
                        ans[index] = true;
			return true;
		}
		else if (index < a.length-1) {
		// adding a[index] is less than goal and numbers remain 
		// so try both adding and not adding it
			if (findsubset(index+1, tempSum,a, goal, ans))  { 
				// try by adding it (tempSum)
				ans[index] = true;
                                return true;
				
			}
			else if(findsubset(index+1, sum, a, goal, ans)) {
				//missing code
				ans[index] = false;
                                return true;
			}
			else {
				ans[index] = false;
                                ans[index+1] = false;
			}
		}
		return false;
	}	







	static public void main(String[] args)	{
  	
   //    DATA SETS   
   //    int numNums = 4;
	//    int[] a = {3, 5, 6, 7};
   //    int goal = 15;
   
   //    int numNums = 4;
	//    int[] a = {1, 3, 4, 5};
   //    int goal = 11;
   
   int numNums = 8;
	int[] a = {8,9,10,11,12,13,43,201};
	int goal = 28;
   /*
   int numNums = 40;
   int goal = 43;
   int[] a = new int[numNums];
   for (int itemp = 0;itemp<numNums; itemp++){
         a[itemp] = itemp + 1;
      }
   */
      boolean[] ans = new boolean[numNums];  // store what integers are in/out of the subset
               System.out.println("The target sum is " + goal);
	       System.out.println("The list of numbers is: ");
      for(int i=0;i<numNums;i++)   {
      		System.out.print(" " + a[i]);
		}
      System.out.println();
	 if (findsubset (0,0, a, goal, ans)) {
			System.out.println("the goal is achieved " + goal);
			for(int i=0;i<numNums;i++)   {
				System.out.print(" " + ans[i]);
			}	
		}
		else
		   System.out.println("The target sum is " + goal + " and could not find solution");
                   System.out.println();
	}	
}
