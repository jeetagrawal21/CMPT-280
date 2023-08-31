import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.BinaryNode280;
import lib280.tree.LinkedSimpleTree280;

/**
 * CMPT 280 ASSIGNMENT 4 Q2
 *
 * NAME             :           JEET AGRAWAL
 * NSID              :           jea316
 * STUDENT ID       :           11269096
 */
public class AVLTree280<I extends Comparable<? super I>>{

    protected I item;
    protected AVLTree280<I> rightNode;
    protected AVLTree280<I> leftNode;

    public AVLTree280(I item){
        this.item = item;
    }

    public I getItem(){return this.item;}
    public AVLTree280<I> getLeftNode(){return this.leftNode;}
    public AVLTree280<I> getRightNode(){return this.rightNode;}
}

class AVL280<I extends Comparable<? super I>> {

    protected AVLTree280<I> rootNode;
    /**
     * A constructor for the AVL trees which calls the constructor of LinkedSimpleTree280.
     */

    /**
     * Gives you the height of the tree respective of the currentnode.
     *
     * @param tree BinaryNode280
     * @return Height
     */
    private int getHeight(AVLTree280<I> node) {

        if (node == null) {  //BASE CASE
            return 0;
        } else {
            return 1 + Math.max(getHeight(node.getLeftNode()), getHeight(node.getRightNode())); //RECURSIVE CASE WITH MAX OF LEFT AND RIGHT SIDE
        }
    }

    /**
     * Gives the difference of heights between left and right node
     *
     * @param tree
     * @return Difference of left and right subtrees.
     */
    private int getImbalance(AVLTree280<I> tree) {

        if (tree == null) {
            return 0;
        } else {
            return (getHeight(tree.getLeftNode()) - getHeight(tree.getRightNode()));//RETURNS THE DIFFERENCE BETWEEN THE HEIGHTS
        }
    }

    /**
     * Inserts the items in the tree and performs rotation based on each case
     *
     * @param x The item to be inserted.
     */
    public AVLTree280<I> insertItem(I x, AVLTree280<I> tree) {
        // FOR AN EMPTY TREE
        if (tree == null) {
            return new AVLTree280<>(x);
        }//this.rootNode = createNewNode(x);return this.rootNode();}

        //IF THE ITEM IS GREATER OR EQUAL TO THE ROOT
        if (x.compareTo(tree.getItem()) >= 0) {
            tree.rightNode = insertItem(x, tree.getRightNode());
        }

        // IF THE ITEM IS SMALLER THAN THE ROOT
        else {
            tree.leftNode = insertItem(x, tree.getLeftNode());
        }

        //NOW THE ROTATION PART

//        THE RIGHT RIGHT (RR) CASE
        if (getImbalance(tree) < -1 && (x.compareTo(tree.getRightNode().getItem()) > 0)) {
            return leftRotate(tree);
        }
//
//        // THE LEFT LEFT (LL) CASE
        else if (getImbalance(tree) > 1 && (x.compareTo(tree.getLeftNode().getItem()) < 0)) {
            return rightRotate(tree);
        }
//
//        // THE RIGHT LEFT (RL) CASE
        else if (getImbalance(tree) < -1 && (x.compareTo(tree.getRightNode().getItem()) < 0)) {
            tree.rightNode = rightRotate(tree.getRightNode());
            return leftRotate(tree);
        }
//
//        // THE LEFT RIGHT (LR) CASE
        else if (getImbalance(tree) > 1 && (x.compareTo(tree.getLeftNode().getItem()) > 0)) {
            tree.leftNode = leftRotate(tree.getLeftNode());
            return rightRotate(tree);
        }
        return tree;
    }

    public AVLTree280<I> deleteItem(I x, AVLTree280<I> tree) throws NoCurrentItem280Exception {

//        if (tree == null) throw new NoCurrentItem280Exception("The item doesn't exist in the tree.");

        if (tree == null) {
            return null;
        }

        if (x.compareTo(tree.getItem()) > 0) {
            tree.rightNode = deleteItem(x, tree.getRightNode());
        } else if (x.compareTo(tree.getItem()) < 0) {
            tree.leftNode = deleteItem(x, tree.getLeftNode());

            // THE CASE WHEN THE ITEM IS EQUAL TO THE ROOT ITEM.
        } else {

            // WHEN THERE IS NO CHILD
            if (tree.getRightNode() == null && tree.getLeftNode() == null) {
                tree.item = null;
            }
            // WHEN THERE IS EITHER ON CHILD OR TWO
            else if (tree.getLeftNode() == null || tree.getRightNode() == null) {

                if (tree.getLeftNode() == null) {
                    this.rootNode = tree.rightNode;
                    tree.rightNode = null;
                } else {
                    this.rootNode = tree.leftNode;
                    tree.leftNode = null;
                }
            } else {
                AVLTree280<I> temp = tree.getRightNode();
                AVLTree280<I> temp_1 = tree;


                while (temp.leftNode != null) {
                    temp_1 = temp;
                    temp = temp.getLeftNode();
                }
                tree.item = temp.item;

                temp_1.leftNode = null;
                tree.rightNode = deleteItem(x, tree.getRightNode());
            }
        }
        //NOW THE ROTATION PART

//        THE RIGHT RIGHT (RR) CASE
        if (getImbalance(tree) < -1 && (getImbalance(tree.getRightNode()) <= 0)) {
            return leftRotate(tree);
        }
//
//        // THE LEFT LEFT (LL) CASE
        else if (getImbalance(tree) > 1 && (getImbalance(tree.getLeftNode()) >= 0)) {
            return rightRotate(tree);
        }
//
//        // THE RIGHT LEFT (RL) CASE
        else if (getImbalance(tree) < -1 && (getImbalance(tree.getRightNode()) > 0)) {
            tree.rightNode = rightRotate(tree.getRightNode());
            return leftRotate(tree);
        }
//
//        // THE LEFT RIGHT (LR) CASE
        else if (getImbalance(tree) > 1 && (getImbalance(tree.getLeftNode()) < 0)) {
            tree.leftNode = leftRotate(tree.getLeftNode());
            return rightRotate(tree);
        }
        return tree;
    }

    /**
     * Does the right rotate.
     *
     * @param tree The tree to be rotate
     * @return The tree after rotation.
     */
    public AVLTree280<I> rightRotate(AVLTree280<I> tree) {

        AVLTree280<I> left_Node = tree.getLeftNode();

        AVLTree280<I> right_LeftNode = left_Node.getRightNode();

        left_Node.rightNode = tree;
        tree.leftNode = right_LeftNode;

        tree = left_Node;
        return tree;
    }

    /**
     * Does the right rotate.
     *
     * @param tree The tree to be rotate
     * @return The tree after rotation.
     */
    public AVLTree280<I> leftRotate(AVLTree280<I> tree) {

        AVLTree280<I> right_Node = tree.getRightNode();
        AVLTree280<I> left_RightNode = right_Node.getLeftNode();

        right_Node.leftNode = tree;
        tree.rightNode = left_RightNode;

        tree = right_Node;

        return tree;
    }

    /**
     * Checks whether the tree has the value or not
     *
     * @param x    The value to be checked.
     * @param tree The tree where the value is to be checked
     * @return True if the value is there and false otherwise.
     */

    public boolean has(I x, AVLTree280<I> tree) {

        if (tree == null) {
            return false;
        }

        if (tree.getItem().compareTo(x) == 0) {
            return true;
        } else {
            return has(x, tree.getLeftNode()) || has(x, tree.getRightNode());
        }
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        this.rootNode = null;
    }

    /**
     * Prints the Pre-order tree
     *
     * @param tree The tree
     */
    public String toString(AVLTree280<I> tree) {
        String x = "";
        if (tree != null) {
            x += (tree.getItem().toString()) + " ";
            x += toString(tree.getLeftNode());
            x += toString(tree.getRightNode());
        }
        return x;
    }

    public static void main(String[] args) {

        /**
         * TESTING BEGINS FROM HERE
         */


        AVL280<Integer> tree = new AVL280<>();

        //RR CASE

        tree.rootNode = tree.insertItem(50, tree.rootNode);
        tree.rootNode = tree.insertItem(6, tree.rootNode);
        tree.rootNode = tree.insertItem(7, tree.rootNode);
        tree.rootNode = tree.insertItem(8, tree.rootNode);
        tree.rootNode = tree.insertItem(10, tree.rootNode);
        tree.rootNode = tree.insertItem(51, tree.rootNode);

        String result_1 = tree.toString(tree.rootNode);
        String test_1 = "10 7 6 8 50 51 ";
        if (!(tree.toString(tree.rootNode).equals(test_1))) {
            System.out.println("The tree doesn't have the AVL property. The tree must be " + result_1 + " but gave " + test_1 + ".");
        }

        //LL case
        tree.rootNode = tree.insertItem(5, tree.rootNode);
        tree.rootNode = tree.insertItem(4, tree.rootNode);

        String result_2 = tree.toString(tree.rootNode);
        String test_2 = "10 7 5 4 6 8 50 51 ";
        if (!(result_2.equals(test_2))) {
            System.out.println("The tree doesn't have the AVL property. The tree must be " + result_2 + " but gave " + test_2 + ".");
        }

        tree.clear();
        //RL case

        tree.rootNode = tree.insertItem(6, tree.rootNode);
        tree.rootNode = tree.insertItem(4, tree.rootNode);
        tree.rootNode = tree.insertItem(7, tree.rootNode);
        tree.rootNode = tree.insertItem(9, tree.rootNode);
        tree.rootNode = tree.insertItem(8, tree.rootNode);

        String result_3 = tree.toString(tree.rootNode);
        String test_3 = "6 4 8 7 9 ";
        if (!(result_3.equals(test_3))) {
            System.out.println("The tree doesn't have the AVL property. The tree must be " + result_3 + " but gave " + test_3 + ".");
        }
        tree.clear();
        // LR case
        tree.rootNode = tree.insertItem(4, tree.rootNode);
        tree.rootNode = tree.insertItem(3, tree.rootNode);
        tree.rootNode = tree.insertItem(2, tree.rootNode);
        tree.rootNode = tree.insertItem(-1, tree.rootNode);
        tree.rootNode = tree.insertItem(0, tree.rootNode);

        tree.clear();

        tree.rootNode = tree.insertItem(50, tree.rootNode);
        tree.rootNode = tree.insertItem(6, tree.rootNode);
        tree.rootNode = tree.insertItem(7, tree.rootNode);
        tree.rootNode = tree.insertItem(8, tree.rootNode);
        tree.rootNode = tree.insertItem(9, tree.rootNode);
        tree.rootNode = tree.insertItem(10, tree.rootNode);
        tree.rootNode = tree.insertItem(11, tree.rootNode);
        tree.rootNode = tree.insertItem(49, tree.rootNode);

        String result_4 = tree.toString(tree.rootNode);
        String test_4 = "9 7 6 8 11 10 50 49 ";
        if (!(result_4.equals(test_4))) {
            System.out.println("The tree doesn't have the AVL property. The tree must be " + result_4 + " but gave " + test_4 + ".");
        }

        tree.rootNode = tree.deleteItem(9, tree.rootNode);


        for (int i = 0; i < 25; i++) {
            tree.rootNode = tree.insertItem(i, tree.rootNode);
            if (!tree.has(i, tree.rootNode)) {
                System.out.println("Error!!!!!");
            }
        }
        System.out.println("TESTING COMPLETED.");
    }
}
