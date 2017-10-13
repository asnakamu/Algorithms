import java.io.*;
import java.util.*;
import java.util.PriorityQueue;

/**
 * Final Project: Knapsack
 * 
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */
public class Knapsack
{
    Scanner in = null;
    //begins at index 1
    int weight[];
    int value[];
    ArrayList<Item> items;
    int sol[];
    int capacity;
    int num;
    ArrayList<Double> vw;
    
    private class Item {
        int weight;
        int value;
        double ratio;
        int index;
    }
    
    public class ItemComparator implements Comparator<Item> {
        @Override
        public int compare(Item x, Item y) {
            return (int)(y.ratio - x.ratio);
        }
    }
    
    private class Node {
        int level;
        int currVal;
        int currWeight;
        double upper;
        ArrayList<Item> sol;
    }
    
    public class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node x, Node y) {
            if (x.upper < y.upper) return -1;
            if (x.upper > y.upper) return 1;
            return 0;
        }
    }

    public void setup(String file) throws IOException {
        try { //Open file and get total # of items
            in = new Scanner(new File(file));
            num = in.nextInt();
            weight = new int[num+1];
            value = new int[num+1];
            sol = new int[num+1];
            items = new ArrayList<Item>();
            //Storage begins at index 1
            for(int j = 0; j < num; j++) {
                int index = in.nextInt();
                int v = in.nextInt();
                int w = in.nextInt();
                value[index] = v;
                weight[index] = w;
                Item in = new Item();
                in.value = v;
                in.weight = w;
                in.ratio = (double)v/w;
                in.index = index;
                items.add(in);
            }
            capacity = in.nextInt();
            
            Collections.sort(items, new ItemComparator());
        } 
        finally {
            if(in != null) {
                in.close(); 
            }
        }
    }
    
    public void bruteForce() {
        ArrayList<String> binary = generateGrayCode(num);
        int size = binary.size();
        int maxVal = 0, maxWeight = 0, ans = 0; //Go through each possible string and evaluate value/weight
        for (int i = 0; i < size; i++) {
            String current = binary.get(i);
            int totalWeight = 0, totalVal = 0, k = 0;
            for (int j = 0; j < num; j++) {
                char item = current.charAt(j);
                if (item == '1') { // Item is in the knapsack
                    totalWeight += weight[j+1];
                    totalVal += value[j+1];
                }
            }            
            if (totalWeight <= capacity && totalVal > maxVal) { //Update best solution
                ans = i;
                maxVal = totalVal;
                maxWeight = totalWeight;
            }
        }
        
        System.out.printf("Using Brute force the best feasible solution found:  " + maxVal + " " + maxWeight + "\n");
        for (int i = 0; i < num; i++) {
            String result = binary.get(ans);
            if (result.charAt(i) == '1') {
                System.out.printf("" + (i+1) + " ");
            }
        }
    }
    
    public void greedy() {
        int totalWeight = 0, totalVal = 0;
        ArrayList<Double> vw = new ArrayList<Double>(), orig = new ArrayList<Double>();
        ArrayList<Integer> ans = new ArrayList<Integer>();
        //Get value to weight ratios
        for (int i = 1; i <= num; i++) {
            vw.add((double)value[i]/weight[i]);
            orig.add((double)value[i]/weight[i]);
        }
        //Sort by highest ratio, pick the item with the greatest ratio
        Collections.sort(vw, Collections.reverseOrder());
        while (totalWeight < capacity && !vw.isEmpty()) {    //if not done yet, take item and remove it from list
            double ratio = vw.get(0);
            int index = orig.indexOf(ratio);
            orig.remove(index);
            orig.add(index, -1.0);
            vw.remove(0);
            //Update solution
            if (totalWeight + weight[index+1] <= capacity) { //Update best solution
                totalWeight += weight[index+1];
                totalVal += value[index+1];
                ans.add(index+1);
            }
        }
        Collections.sort(ans);
        
        System.out.printf("Greedy solution (not necessarily optimal):  " + totalVal + " " + totalWeight + "\n");
        for (int i = 0; i < ans.size(); i++) {
            System.out.printf("" + ans.get(i) + " ");
        }
    }
    
    public void dynamic() {   //initialize the table & fill the table
        int arr[][] = new int[num+1][capacity+1], maxWeight = 0;
        for (int i = 0; i <= num; i++) {
            arr[i][0] = 0;
        }
        for (int i = 0; i <= capacity; i++) {
            arr[0][i] = 0;
        }        
        for (int j = 1; j <= capacity; j++) {
            for (int i = 1; i <= num; i++) {               
                int currWeight = weight[i];
                if (currWeight <= j) {
                    arr[i][j] = Math.max(arr[i - 1][j - currWeight] + value[i], arr[i - 1][j]);
                }
                else {
                    arr[i][j] = arr[i - 1][j];
                }
            }
        }
        
        // trace back to find the items in the knapsack
        ArrayList<Integer> ans = new ArrayList<Integer>();
        int i = num, j = capacity;
        while (i > 0 && j > 0) {
            if (arr[i][j] != arr[i - 1][j]) { // the ith item was taken, update accordingly
                ans.add(i);
                maxWeight += weight[i];
                j -= weight[i];
                i--;
            }
            else {
                i--;
            }
        }
        Collections.sort(ans);
        
        System.out.printf("Dynamic Programming solution:  " + arr[num][capacity] + " " + maxWeight + "\n");
        for (i = 0; i < ans.size(); i++) {
            System.out.printf("" + ans.get(i) + " ");
        }
    }
    
    public void bb_attempt() { //create the queue & root node
        Comparator<Node> comparator = new NodeComparator();
        PriorityQueue<Node> pq = new PriorityQueue<Node>(num, comparator);
        Node root = new Node(), best = new Node();
        int maxProfit = 0, maxWeight = 0;
        root.level = -1;
        root.currVal = 0;
        root.currWeight = 0;        
        root.sol = new ArrayList<Item>();
        root.upper = getBound(root);
        pq.offer(root);
        
        long start = System.currentTimeMillis();
        while (!pq.isEmpty()) {
            long cont = System.currentTimeMillis();
            if (cont - start > 40000) {
                System.out.printf("\nTIMEOUT due to memory issues\n");
                break;
            }
            Node parent = pq.poll();
            Node child = new Node();
            int size = parent.sol.size();
            if (parent.upper > maxProfit) { //make the child that takes the next item
                child.level = parent.level + 1;
                child.currWeight = parent.currWeight + items.get(child.level).weight;
                child.currVal = parent.currVal + items.get(child.level).value;                
                child.sol = new ArrayList<Item>(size);
                child.sol.addAll(parent.sol);
                child.sol.add(items.get(child.level));
                child.upper = getBound(child);
            }
            
            if (child.currWeight <= capacity && child.currVal > maxProfit) { //if better, update the maxes
                maxProfit = child.currVal;
                maxWeight = child.currWeight;
                best = child;
            }
            if (child.upper > maxProfit) {
                pq.offer(child);
            }
            
            //make the child that does NOT take the next item
            Node child2 = new Node();
            child2.level = parent.level + 1;
            child2.currWeight = parent.currWeight;
            child2.currVal = parent.currVal;
            child2.sol = new ArrayList<Item>(size);            
            child2.sol.addAll(parent.sol);
            child2.upper = getBound(child2);
            if (child2.upper > maxProfit) {
                pq.offer(child2);
            }
        }
        long end = System.currentTimeMillis();
        
        System.out.printf("Using Branch and Bound the best feasible solution found: " + maxProfit + " " + maxWeight + "\n");
        ArrayList<Integer> ans = new ArrayList<Integer>();
        for (int i = 0; i < best.sol.size(); i++) {
            ans.add(best.sol.get(i).index);
        }
        Collections.sort(ans);
        for (int i = 0; i < ans.size(); i++) {
            System.out.printf("" + ans.get(i) + " ");
        }
    }
        
    private double getBound(Node c) {
        int i, j, totalWeight;
        double result;
        
        if (c.currWeight >= capacity) {
            return 0;
        }
        else { //continue adding item value/weights to the upper bound until capacity is reached, then add fractional next item
            result = c.currVal;
            i = c.level + 1;
            totalWeight = c.currWeight;            
            while (i < num && totalWeight + items.get(i).weight <= capacity) {
                totalWeight += items.get(i).weight;
                result += items.get(i).value;   
                i++;
            }
            if (i < num) {
                result += (capacity - totalWeight) * items.get(i).ratio;
            }            
            return result;
        }
    }

    private ArrayList<String> generateGrayCode(int element) {
        if (element == 0) {
            ArrayList<String> result = new ArrayList<String>();
            result.add("");
            return result;
        }
        else { // recursively call this method and get the return, use the return size as your looping count
            ArrayList<String> call = generateGrayCode(element - 1);
            ArrayList<String> fin = new ArrayList<String>();
            int total = call.size(); // alternatively prepend 0 and 1 to the list of subsets
            for (int i = 0; i < total; i++) {
                fin.add("0" + call.get(i));
            }
            for (int i = total - 1; i >= 0; i--) {
                fin.add("1" + call.get(i));
            }
            return fin;
        }
    }

    public static void main(String args[]) throws IOException
    {
        Knapsack ks = new Knapsack();
        ks.setup("README.TXT");
        //Method testing
        /* long start = System.currentTimeMillis();
        ks.bruteForce();
        long end = System.currentTimeMillis();
        System.out.printf("\nTime: " + (end - start) + " ms\n"); */
        
        //long start2 = System.nanoTime();        
        ks.greedy();        
        System.out.printf("\n");
        ks.dynamic();
        System.out.printf("\n");
        //ks.branchBound();
        ks.bb_attempt();
        //long end2 = System.nanoTime();
        //System.out.printf("\nTime: " + (end2 - start2) + " ns\n");
    }
}
