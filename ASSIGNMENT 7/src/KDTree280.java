/**
 * NAME		:		JEET AGRAWAL
 * NSID		:		jea316
 * STUDENT ID:		11269096
 */

import lib280.base.NDPoint280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.tree.BinaryNode280;
import lib280.tree.LinkedSimpleTree280;

import java.util.HashSet;

public class KDTree280<I extends Comparable<? super I>> implements Cloneable{

    protected KDNode280<I> rootNode;        // THE ROOTNODE
    private int dimension;                  // THE DIMENSION OF THE k-D TREE

    /**
     * A CONSTRUCTOR WHICH SETS THE ROOT TO NULL AND SETS THE DIMENSION.
     * @param dimension THE DIMENSION OF TH EK-D TREE.
     */
    public KDTree280(int dimension){
        this.dimension = dimension;
        this.rootNode = null;
    }


    /**	Is the lib280.tree empty?.
     Analysis: Time = O(1)  */
    public boolean isEmpty(){return rootNode == null;}

    /**	Set root node to new node.
     Analysis: Time = O(1)
     @param newNode node to become the new root node */
    protected void setRootNode(KDNode280<I> newNode){rootNode = newNode;}

    /**	Contents of the root item.
     Analysis: Time = O(1)
     @precond !isEmpty()
     */
    public I rootItem() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot access the root of an empty lib280.tree.");

        return rootNode.item();
    }

    /**	Left subtree of the root.
     Analysis: Time = O(1)
     @precond !isEmpty()
     */
    public KDTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280<I> result = this.clone();
        result.clear();
        result.setRootNode((KDNode280<I>) rootNode.leftNode());
        return result;
    }

    /**	Right subtree of the root.
     Analysis: Time = O(1)
     @precond !isEmpty()
     */
    public KDTree280<I> rootRightSubtree() throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280<I> result = this.clone();
        result.clear();
        result.setRootNode((KDNode280<I>) rootNode.rightNode());
        return result;
    }

    /**	A shallow clone of this lib280.tree.
     Analysis: Time = O(1)
     */
    @SuppressWarnings("unchecked")
    public KDTree280<I> clone()
    {
        try
        {
            return (KDTree280<I>) super.clone();
        } catch(CloneNotSupportedException e)
        {
            /*	Should not occur because Container280 extends Cloneable */
            e.printStackTrace();
            return null;
        }
    }

    /**	Remove all items from the lib280.tree.
     Analysis: Time = O(1) */
    public void clear() {
        setRootNode(null);
    }

    /**
     *RETURNS THE DIMENSION OF THE TREE
     * @return DIMENSION OF THE TREE.
     */
    protected int getDimension(){return this.dimension;}


    /**
     * Below is a version of the j-th smallest element algorithm that operates on a subarray of an array
     * specified by offsets le f t and right (inclusive). It places at offset j (where le f t ≤ j ≤ right) the element
     * that belongs at offset j if the subarray were sorted.
     * @param list The array
     * @param left Left offset
     * @param right Right offset
     * @param j j th offset
     * @param dimension dimension of the tree.
     */
    public void jSmallest(NDPoint280[] list,int left,int right,int j, int dimension){

        if (right > left){
            int pivotIndex = partition ( list , left , right, dimension );

            if (j < pivotIndex){
                jSmallest(list,left,pivotIndex-1,j,dimension);
            }
            else if (j > pivotIndex){
                jSmallest(list,pivotIndex + 1,right,j,dimension);
            }
        }
    }

    /**
     *jSmallest uses the partition algorithm partition the elements of the
     * subarray using a pivot.
     * @param list the array
     * @param left left offset
     * @param right right offset
     * @param dimension dimension of the tree
     * @return the offset of the median
     */
    public int partition(NDPoint280[] list, int left, int right, int dimension){

        double pivot = list[right].idx(dimension);
        int swapOffset = left;

        for (int i = left; i < right ; i++ ){
            if (list[i].idx(dimension) <= pivot ){
                NDPoint280 temp = list[i];
                list[i] = list[swapOffset];
                list[swapOffset] = temp;
                swapOffset++;
            }
        }
        NDPoint280 temp = list[right];
        list[right] = list[swapOffset];
        list[swapOffset] = temp;
        return swapOffset;
    }

    /**
     * for building a k-d tree from a set of k-dimensional points is given below.
     * @param pointArray array of k - dimensional points
     * @param left offset of start of subarray from which to build a kd - tree
     * @param right offset of end of subarray from which to build a kd - tree
     * @param depth the current depth in the partially built tree - note that the root
     * of a tree has depth 0 and the $k$ dimensions of the points
     * are numbered 0 through k -1.
     * @return kd node
     */
    public KDNode280<I> kdTree(NDPoint280[] pointArray, int left, int right, int depth){

        if (right < left){
            return null;
        }

        else{
            int d = depth % getDimension();
            int medianOffset = (left + right)/2;

            jSmallest(pointArray,left,right,medianOffset,d);

            // Create a new node and construct subtrees.

            KDNode280<I> node = new KDNode280<>((I)pointArray[medianOffset]);
            node.setItem((I) pointArray[medianOffset]);
            node.setLeftNode(kdTree(pointArray,left,medianOffset-1,depth+1));
            node.setRightNode(kdTree(pointArray,medianOffset+1,right,depth+1));

            return node;
        }
    }

    public String toStringByLevel(int i){
        {
            StringBuffer blanks = new StringBuffer((i - 1) * 5);
            for (int j = 0; j < i - 1; j++)
                blanks.append("     ");

            String result = new String();
            if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
                result += rootRightSubtree().toStringByLevel(i+1);

            result += "\n" + blanks + i + ": " ;
            if (isEmpty())
                result += "-";
            else
            {
                result += rootItem();
                if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                    result += rootLeftSubtree().toStringByLevel(i+1);
            }
            return result;
        }
    }

    /**
     * Do a range search of the given points.
     * @param T The tree
     * @param low the lower point
     * @param high the higher point
     * @param depth the level of the root of the subtree T in the overall Tree.
     * @return A HasSet which consists of NDPoint
     */
    public HashSet<NDPoint280> rangeSearch(KDTree280<NDPoint280> T, NDPoint280 low, NDPoint280 high,int depth){
//        T - subtree in which to search for elements between a and b ( inclusive ).
//        xmin , xmax - lower and upper bounds of search range for first dimension
//        ymin , ymax - lower and upper bounds of search range for second dimension
//        depth - level of the root of subtree T in the overall tree .
//        Returns the set of in - range elements in T.

        if (T.isEmpty()){return null;}

        int coord = depth % getDimension();

        NDPoint280 splitValue = T.rootItem();

        if (low.compareByDim(coord,splitValue) > 0){
            return rangeSearch(T.rootRightSubtree(),low,high,depth+1);
        }
        else if (high.compareByDim(coord,splitValue) < 0){
            return rangeSearch(T.rootLeftSubtree(),low,high,depth+1);
        }
        else {
            HashSet<NDPoint280> L = rangeSearch(T.rootLeftSubtree(), low, high, depth + 1);
            HashSet<NDPoint280> R = rangeSearch(T.rootRightSubtree(), low, high, depth + 1);
            HashSet<NDPoint280> set = new HashSet<>();
            if (L != null) set.addAll(L);
            if (R != null) set.addAll(R);

            for (int i = 0; i < getDimension(); i++){
                if (!(low.compareByDim(i,T.rootItem()) <= 0 && high.compareByDim(i,T.rootItem()) >= 0)){
                    return set;
                }
            }
            set.add(T.rootItem());
            return set;
        }
    }
    public String toString(){return this.toStringByLevel(1);}

    public static void main(String[] args) {

        KDTree280<NDPoint280> tree1 = new KDTree280<>(2);

        NDPoint280 a = new NDPoint280(new double[]{5,2});
        NDPoint280 b = new NDPoint280(new double[]{9,10});
        NDPoint280 c = new NDPoint280(new double[]{11,1});
        NDPoint280 d = new NDPoint280(new double[]{4,3});
        NDPoint280 e = new NDPoint280(new double[]{2,12});
        NDPoint280 f = new NDPoint280(new double[]{3,7});
        NDPoint280 g = new NDPoint280(new double[]{1,5});

        System.out.println("Input 2D points:");
        NDPoint280[] arr = {a,b,c,d,e,f,g};

        for (NDPoint280 p : arr ){
            System.out.println(p);
        }

        System.out.println("The 2 D tree built from these points is :\n");
        tree1.setRootNode(tree1.kdTree(arr,0,arr.length - 1,0));
        System.out.println(tree1.toString());

        System.out.println();
        System.out.println("Input 3D points:");
        KDTree280<NDPoint280> tree3D = new KDTree280<>(3);


        NDPoint280 a2 = new NDPoint280(new double[]{1,12,1});
        NDPoint280 b2 = new NDPoint280(new double[]{18,1,2});
        NDPoint280 c2 = new NDPoint280(new double[]{2,12,16});
        NDPoint280 d2 = new NDPoint280(new double[]{7,3,3});
        NDPoint280 e2 = new NDPoint280(new double[]{3,7,5});
        NDPoint280 f2 = new NDPoint280(new double[]{16,4,4});
        NDPoint280 g2 = new NDPoint280(new double[]{4,6,1});
        NDPoint280 h2 = new NDPoint280(new double[]{5,5,17});

        NDPoint280[] arr2 = {a2,b2,c2,d2,e2,f2,g2,h2};
        for (NDPoint280 p : arr2){
            System.out.println(p);
        }

        tree3D.setRootNode(tree3D.kdTree(arr2,0,arr2.length - 1,0));
        System.out.println(tree3D.toString());

        NDPoint280 r1 = new NDPoint280(new double[]{0,1,0});
        NDPoint280 r2 = new NDPoint280(new double[]{4,6,3});

        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (4.0 , 6.0 , 3.0).\n" +
                "Found :");
        HashSet<NDPoint280> set = tree3D.rangeSearch(tree3D,r1,r2,0);

        for (NDPoint280 s:set){
            System.out.println(s);
        }
        System.out.println();

        NDPoint280 r3 = new NDPoint280(new double[]{0,1,0});
        NDPoint280 r4 = new NDPoint280(new double[]{8,7,4});
        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (8.0 , 7.0 , 4.0).\n" +
                "Found :");
        HashSet<NDPoint280> set2 = tree3D.rangeSearch(tree3D,r3,r4,0);
        for (NDPoint280 s:set2){
            System.out.println(s);
        }
        System.out.println();

        NDPoint280 r5 = new NDPoint280(new double[]{0,1,0});
        NDPoint280 r6 = new NDPoint280(new double[]{17,9,10});
        System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (17.0 , 9.0 , 10.0).\n" +
                "Found :");

        HashSet<NDPoint280> set3 = tree3D.rangeSearch(tree3D,r5,r6,0);
        for (NDPoint280 s: set3){
            System.out.println(s);
        }
    }

}
