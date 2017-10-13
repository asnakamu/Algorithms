/**
 * CPE 349 Lab 3: Heap Sort
 *
 * @author Amy Nakamura
 * @CalPoly ID: asnakamu
 */

import java.util.ArrayList;

public class Heap 
{
    private ArrayList<Integer> into;
    private ArrayList<HeapDriver> orig, keep, change;

    public Heap(ArrayList<HeapDriver> list)
    {
        // Make a copy of the passed in list
        // Create another array that will be the one changed to be heap compliant
        keep = new ArrayList<HeapDriver>();
        orig = new ArrayList<HeapDriver>();
        for(int i = 0; i < list.size(); i++) 
        {
            orig.add(new HeapDriver(list.get(i).getName(), list.get(i).getPri()));
            keep.add(new HeapDriver(list.get(i).getName(), list.get(i).getPri()));
        }
    }

    public ArrayList<HeapDriver> heapSort(ArrayList<HeapDriver> list)
    {
        // Build the heap
        list = buildHeap(list);

        int size = list.size();
        int i = size - 1;
        // Swap the max with the last unswapped object in a loop
        // perform drift down
        while (i > 1)
        {
            HeapDriver max = list.get(1);
            list.set(1, list.get(i));
            list.set(i, max);

            i--;            
            list = driftDown(list, 1, i);
        }

        return list;
    }

    public ArrayList<HeapDriver> changePriority(int pos, int pri)
    {
        // Create the heap from the original array
        // Get the index of the object to change from the into array
        change = buildHeap(keep);
        makeInto(change);
        int intoPos = into.get(pos);

        System.out.printf("Into Array (Before)\n");
        for (Integer h : into)
        {
           System.out.printf(h + " ");
        }
        System.out.printf("\n");

        // Change the priority
        HeapDriver obj = keep.get(intoPos);
        obj.changePri(pri);

        // Check each parent and if their priority < new priority, swap them
        int parent = intoPos / 2;
        while (parent > 0 && keep.get(parent).getPri() < obj.getPri())
        {
           // Update into array
            int update = into.indexOf(parent);
            into.set(pos, parent);
            into.set(update, intoPos);

            // Swap
            change.set(intoPos, change.get(parent));
            change.set(parent, obj);
            intoPos = parent;
            parent = parent / 2;
        }

        System.out.printf("Into Array (After)\n");
        for (Integer h : into)
        {
           System.out.printf(h + " ");
        }
        System.out.printf("\n");

        // Update into array
        return change;
    }

    /** Helper Functions */
    public ArrayList<HeapDriver> buildHeap(ArrayList<HeapDriver> list)
    {
        // If the current node does not satisfy the heap condition
        // perform drift down in a loop
        int size = list.size();        
        for (int i = size / 2; i >= 1; i--)
        {
            list = driftDown(list, i, size - 1);
        }

        return list;
    }

    private void makeInto(ArrayList<HeapDriver> list)
    {
        into = new ArrayList<Integer>();
        int index = 0;
        
        // Create the lookup array containing the heap index positions of the original array
        for (int i = 1; i < orig.size(); i++)
        {
            for (int j = 1; j < list.size(); j++)
            {
                if (list.get(j).getName().equals(orig.get(i).getName()))
                {
                    index = j;
                    j = list.size();
                }
            }
            into.add(index);
        }
    }

    private ArrayList<HeapDriver> driftDown(ArrayList<HeapDriver> list, int index, int stop)
    {
        HeapDriver greater, temp;
        int pos = index;
        //int size = list.size() - 1;

        HeapDriver parent = list.get(index);
        // While current object has a child
        while (index * 2 <= stop)
        {
            HeapDriver leftChild = list.get(2 * index);
            greater = parent;
            // Compare the priority of the left child and the parent
            // if the left child is greater, save it and it's index
            if (greater.getPri() < leftChild.getPri())
            {
                greater = leftChild;
                pos = 2 * index;
            }

            // Compare the priority of the right child and the greater of the left/parent
            // if the right child is greater, save it and it's index
            if ((index * 2) + 1 <= stop)
            {
                HeapDriver rightChild = list.get((index * 2) + 1);
                if (greater.getPri() < rightChild.getPri())
                {
                    greater = rightChild;
                    pos = (2 * index) + 1;
                }
            }

            // If the greatest priority is not the parent, swap positions
            // Repeat until reaching the end of the array
            if (greater.getPri() != parent.getPri())
            {
                temp = parent;
                list.set(index, greater);
                list.set(pos, parent);
                index = pos;
            }
            else
            {
                return list;
            }
        }
        return list;
    }
}
