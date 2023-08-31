import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.ArrayedBinaryTree280;

/**
 * CMPT 280 ASSIGNMENT 4 Q1
 *
 * NAME             :           JEET AGRAWAL
 * NSID              :           jea316
 * STUDENT ID       :           11269096
 */

//import lib280.base.Dispenser280;
//import lib280.exception.ContainerFull280Exception;
//import lib280.exception.DuplicateItems280Exception;
//import lib280.exception.NoCurrentItem280Exception;
//import lib280.tree.ArrayedBinaryTree280;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    /**
     * CONSTRUCTOR WHICH CALLS THE CONSTRUCTOR FROM THE EXTENDED CLASS ArrayedBinaryTree280.
     *The constructor makes the items array of type comparable.
     * @param cap THE CAPACITY OF THE ARRAYED HEAP.
     */

    public ArrayedHeap280(int cap) {
        super(cap);
        this.items = (I[]) new Comparable[capacity + 1];
    }



    /**
     * This method inserts an element in the heap following the heap property.
     * @param x item to be inserted into the data structure
     * @throws ContainerFull280Exception
     * @throws DuplicateItems280Exception
     */
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {

        if (isFull()) throw new ContainerFull280Exception("The Heap is Full.");

        this.items[this.count() + 1] = x;
        this.count++;

        int cursor = count;
        int index = this.findParent(this.count());

        while (cursor > 1 && x.compareTo(items[index]) > 0) {
            items[cursor] = items[index];
            items[index] = x;

            cursor = cursor / 2;
            index = this.findParent(cursor);
        }
        this.currentNode = 1;
}

    /**
     * This method deletes the top item from the heap and keeps the heap property intact.
     * @throws NoCurrentItem280Exception
     */
    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        if (!itemExists()) throw new NoCurrentItem280Exception("There is no item at the cursor.");

        I x = items[this.count];
        items[currentNode] = items[this.count()];
        items[this.count()] = null;
        this.count--;

        int index = 1;

        while (index <= this.count()/2) {
            int leftChild = findLeftChild(index);
            int rightChild = findRightChild(index);

            if (rightChild > this.count()){
                if (x.compareTo(items[leftChild]) < 0){
                    items[index] = items[leftChild];
                    items[leftChild] = x;
                    index = leftChild;
                }
                else break;
            }
            else if (items[leftChild].compareTo(items[rightChild]) > 0 && x.compareTo(items[leftChild]) < 0) {
                items[index] = items[leftChild];
                items[leftChild] = x;
                index = leftChild;
            } else if (items[leftChild].compareTo(items[rightChild]) < 0 && x.compareTo(items[rightChild]) < 0) {
                items[index] = items[rightChild];
                items[rightChild] = x;
                index = rightChild;
            }
        }
        if (this.count() == 0)this.currentNode = 0;
    }

    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty() {
        for (int i = 1; i <= count; i++) {
            if (findRightChild(i) <= count) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if (items[i].compareTo(items[findRightChild(i)]) < 0) return false;
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else if (findLeftChild(i) <= count) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main (String[]args){

        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for (int i = 1; i <= 10; i++) {
            H.insert(i);
            if (H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }
        // Remove the elements 10 through 1 from the heap, chekcing
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for (int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if (i == 1) {
                if (!H.isEmpty()) System.out.println("Expected the heap to be empty, but it wasn't.");
            } else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if (H.item() != i - 1)
                    System.out.println("Expected current item to be " + i + ", got " + H.item());
                if (!H.hasHeapProperty()) {
                    System.out.println("Does not have heap property.");
                    System.out.println("For " + i);
                }
            }
        }
        System.out.println("Regression Test Complete.");
    }
}