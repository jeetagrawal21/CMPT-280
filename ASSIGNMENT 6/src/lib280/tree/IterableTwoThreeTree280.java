/**
 * NAME			:			JEET AGRAWAL
 * NSID			:			jea316
 * STUDENT ID	:			11269096
 */

package lib280.tree;

import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.*;

public class IterableTwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeTree280<K, I> implements KeyedDict280<K,I> {

	// References to the leaf nodes with the smallest and largest keys.
	LinkedLeafTwoThreeNode280<K,I> smallest, largest;
	
	// These next two variables represent the cursor which
	// the methods inherited from KeyedLinearIterator280 will
	// manipulate.  The cursor may only be positioned at leaf
	// nodes, never at internal nodes.
	
	// Reference to the leaf node at which the cursor is positioned.
	LinkedLeafTwoThreeNode280<K,I> cursor;
	
	// Reference to the predecessor of the node referred to by 'cursor' 
	// (or null if no such node exists).
	LinkedLeafTwoThreeNode280<K,I> prev;
	
	
	protected LinkedLeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LinkedLeafTwoThreeNode280<K,I>(newItem);
	}


	@Override
	public void insert(I newItem) {

		if( this.has(newItem.key()) ) 
			throw new DuplicateItems280Exception("Key already exists in the tree.");

		// If the tree is empty, just make a leaf node. 
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
			// Set the smallest and largest nodes to be the one leaf node in the tree.
			this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
			this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
		}
		// If the tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LinkedLeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LinkedLeafTwoThreeNode280<K,I> oldRoot = (LinkedLeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);	
				newLeaf.setNext(oldRoot);
				oldRoot.setPrev(newLeaf);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = newLeaf;
				this.largest = oldRoot;
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
				oldRoot.setNext(newLeaf);
				newLeaf.setPrev(oldRoot);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = oldRoot;
				this.largest = newLeaf;
			}
			this.rootNode = newRoot;
		}
		else {
			Pair280<TwoThreeNode280<K,I>, K> extra = this.insert((InternalTwoThreeNode280<K,I>)this.rootNode, newItem);

			// If extra returns non-null, then the root was split and we need
			// to make a new root.
			if( extra != null ) {
				InternalTwoThreeNode280<K,I> oldRoot = (InternalTwoThreeNode280<K,I>)rootNode;

				// extra always contains larger keys than its sibling.
				this.rootNode = createNewInternalNode(oldRoot, extra.secondItem(), extra.firstItem(), null, null);				
			}
		}
	}


	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	protected Pair280<TwoThreeNode280<K,I>, K> insert(TwoThreeNode280<K,I> root,
                                                      I newItem) {

		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> extraNode;
			LinkedLeafTwoThreeNode280<K,I> oldLeaf = (LinkedLeafTwoThreeNode280<K, I>) root;

			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			
			LinkedLeafTwoThreeNode280<K,I> newLeaf= (LinkedLeafTwoThreeNode280<K, I>) extraNode.firstItem();
		
			// No matter what happens above, the node 'newLeaf' is a new leaf node that is 
			// immediately to the right of the node 'oldLeaf'.
			
			// TODO Link newLeaf to its proper successor/predecessor nodes and
			//  adjust links of successor/predecessor nodes accordingly.

			newLeaf.setNext(oldLeaf.next());
			if (oldLeaf.next() != null){
			oldLeaf.next().setPrev(newLeaf);
			}
			oldLeaf.setNext(newLeaf);
			newLeaf.setPrev(oldLeaf);

			if (newLeaf.next() == null){
				this.largest = newLeaf;
			}
			// Also adjust this.largest if necessary.

			// (this.smallest will never need adjustment because if a new
			//  smallest element is inserted, it gets put in the existing 
			//  leaf node, and the old smallest element is copied to a  
			//  new node -- this is "true" case for the previous if/else.)
			
		
			return extraNode;
		}
		else { // Otherwise, recurse! 
			Pair280<TwoThreeNode280<K,I>, K> extra;
			TwoThreeNode280<K,I> insertSubtree;

			if( newItem.key().compareTo(root.getKey1()) < 0 ) {
				// decide to recurse left
				insertSubtree = root.getLeftSubtree();
			}
			else if(!root.isRightChild() || newItem.key().compareTo(root.getKey2()) < 0 ) {
				// decide to recurse middle
				insertSubtree = root.getMiddleSubtree();
			}
			else {
				// decide to recurse right
				insertSubtree = root.getRightSubtree();
			}

			// Actually recurse where we decided to go.
			extra = insert(insertSubtree, newItem);

			// If recursion resulted in a new node needs to be linked in as a child
			// of root ...
			if( extra != null ) {
				// Otherwise, extra.firstItem() is an internal node... 
				if( !root.isRightChild() ) {
					// if root has only two children.  
					if( insertSubtree == root.getLeftSubtree() ) {
						// if we inserted in the left subtree...
						root.setRightSubtree(root.getMiddleSubtree());
						root.setMiddleSubtree(extra.firstItem());
						root.setKey2(root.getKey1());
						root.setKey1(extra.secondItem());
						return null;
					}
					else {
						// if we inserted in the right subtree...
						root.setRightSubtree(extra.firstItem());
						root.setKey2(extra.secondItem());
						return null;
					}
				}
				else {
					// otherwise root has three children
					TwoThreeNode280<K, I> extraNode;
					if( insertSubtree == root.getLeftSubtree()) {
						// if we inserted in the left subtree
						extraNode = createNewInternalNode(root.getMiddleSubtree(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setMiddleSubtree(extra.firstItem());
						root.setRightSubtree(null);
						K k1 = root.getKey1();
						root.setKey1(extra.secondItem());
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k1);
					}
					else if( insertSubtree == root.getMiddleSubtree()) {
						// if we inserted in the middle subtree
						extraNode = createNewInternalNode(extra.firstItem(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, extra.secondItem());
					}
					else {
						// we inserted in the right subtree
						extraNode = createNewInternalNode(root.getRightSubtree(), extra.secondItem(), extra.firstItem(), null, null);
						K k2 = root.getKey2();
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k2);
					}
				}
			}
			// Otherwise no new node was returned, so there is nothing extra to link in.
			else return null;
		}		
	}


	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;

		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete ) {
				this.rootNode = null;
				this.smallest = null;
				this.largest = null;
			}
		}
		else {
			delete(this.rootNode, keyToDelete);	
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
				if( !this.rootNode.isInternal() ) {
					this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
					this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
				}
			}
		}
	}


	/**
	 * Given a key, delete the corresponding key-item pair from the tree.
	 * @param root root of the current tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
	protected void delete(TwoThreeNode280<K, I> root, K keyToDelete ) {
		if( root.getLeftSubtree().isInternal() ) {
			// root is internal, so recurse.
			TwoThreeNode280<K,I> deletionSubtree;
			if( keyToDelete.compareTo(root.getKey1()) < 0){
				// recurse left
				deletionSubtree = root.getLeftSubtree();
			}
			else if( root.getRightSubtree() == null || keyToDelete.compareTo(root.getKey2()) < 0 ){
				// recurse middle
				deletionSubtree = root.getMiddleSubtree();
			}
			else {
				// recurse right
				deletionSubtree = root.getRightSubtree();
			}

			delete(deletionSubtree, keyToDelete);

			// Do the first possible of:
			// steal left, steal right, merge left, merge right
			if( deletionSubtree.getMiddleSubtree() == null)  
				if(!stealLeft(root, deletionSubtree))
					if(!stealRight(root, deletionSubtree))
						if(!giveLeft(root, deletionSubtree))
							if(!giveRight(root, deletionSubtree))
								throw new InvalidState280Exception("This should never happen!");

		}
		else {
			// children of root are leaf nodes
			if( root.getLeftSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on left

				// TODO Unlink leaf from it's linear successor/predecessor
				//  Hint: Be prepared to typecast where appropriate.

				// MY CODE STARTS
				LinkedLeafTwoThreeNode280 leftLeaf = (LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree();

				if (this.cursor == leftLeaf) this.cursor = leftLeaf.next();

				if (leftLeaf.prev() != null){
					if (leftLeaf.next() == null){
						leftLeaf.prev().setNext(null);
						leftLeaf.setPrev(null);
					}
					leftLeaf.prev().setNext(leftLeaf.next());
					leftLeaf.next().setPrev(leftLeaf.prev());
				}
				else{
					this.smallest = leftLeaf.next();
				}
				if (leftLeaf.next() != null){leftLeaf.next().setPrev(null);}
				leftLeaf.setNext(null);
//MY CODE ENDS
				// Proceed with deletion of leaf from the 2-3 tree.
				root.setLeftSubtree(root.getMiddleSubtree());
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());
				if( root.getRightSubtree() != null) root.setKey2(null);
				root.setRightSubtree(null);					
			}
			else if( root.getMiddleSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is in middle

				// TODO Unlink leaf from it's linear successor/predecessor
				//  Hint: Be prepared to typecast where appropriate.
//MY CODE STARTS

				LinkedLeafTwoThreeNode280 middleLeaf = (LinkedLeafTwoThreeNode280) root.getMiddleSubtree();

				if (this.cursor == middleLeaf){
					this.cursor = middleLeaf.next();
				}
				if (middleLeaf.prev() != null) middleLeaf.prev().setNext(middleLeaf.next());
				if (middleLeaf.next() != null) middleLeaf.next().setPrev(middleLeaf.prev());

				if (middleLeaf.next() == null) this.largest = middleLeaf.prev();

				middleLeaf.setPrev(null);
				middleLeaf.setNext(null);

// MY CODE ENDS.
				// Proceed with deletion from the 2-3 tree.
				root.setMiddleSubtree(root.getRightSubtree());				
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());

				if( root.getRightSubtree() != null) {
					root.setKey2(null);
					root.setRightSubtree(null);
				}
			}
			else if( root.getRightSubtree() != null && root.getRightSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on the right

				// TODO Unlink leaf from it's linear successor/predecessor
				//  Hint: Be prepared to typecast where appropriate.

// MY CODE STARTS

				LinkedLeafTwoThreeNode280 rightLeaf = (LinkedLeafTwoThreeNode280) root.getRightSubtree();

				if (this.cursor == rightLeaf){
					this.cursor = rightLeaf.next();
				}

				if (rightLeaf.next() != null){
					if (rightLeaf.prev() == null){
						rightLeaf.next().setPrev(null);
						rightLeaf.setNext(null);
					}
					rightLeaf.prev().setNext(rightLeaf.next());
					rightLeaf.next().setPrev(rightLeaf.prev());
				}
				else{
					this.largest = rightLeaf.prev();
				}
				if (rightLeaf.prev() != null){rightLeaf.prev().setNext(null);}
				rightLeaf.setPrev(null);
// MY CODE ENDS.

				// Proceed with deletion of the node from the 2-3 tree.
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in tree.
			}
		}		
	}	
	
	
	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		// TODO Return the key of the item in the node on which the cursor is positioned.
		if (!itemExists()) throw new NoCurrentItem280Exception("There is no item at the current position.");

		return this.cursor.getKey1();
	}


	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		// Return a pair consisting of the key of the item
		// at which the cursor is positioned, and the entire
		// item in the node at which the cursor is positioned.
		if( !itemExists() ) 
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		return new Pair280<K, I>(this.itemKey(), this.item());
	}


	@Override
	public I item() throws NoCurrentItem280Exception {
		// TODO Return the item in the node at which the cursor is positioned.

		if (!itemExists()) throw new NoCurrentItem280Exception("There is no item at the current position.");

		return this.cursor.getData();

	}


	@Override
	public boolean itemExists() {
		return this.cursor != null;
	}


	@Override
	public boolean before() {
		return this.cursor == null && this.prev == null;
	}


	@Override
	public boolean after() {
		return this.cursor == null && this.prev != null || this.isEmpty();
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.after() ) throw new AfterTheEnd280Exception("Cannot advance the cursor past the end.");
		if( this.before() ) this.goFirst();
		else {
			this.prev = this.cursor;
			this.cursor = this.cursor.next();
		}
	}


	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Attempted to move linear iterator to first element of an empty tree.");
		this.prev = null;
		this.cursor = this.smallest;
	}


	@Override
	public void goBefore() {
		this.prev = null;
		this.cursor = null;
	}


	@Override
	public void goAfter() {
		this.prev = this.largest;
		this.cursor = null;
	}


	@Override
	public CursorPosition280 currentPosition() {
		return new TwoThreeTreePosition280<K,I>(this.cursor, this.prev);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) {
		if(c instanceof TwoThreeTreePosition280 ) {
			this.cursor = ((TwoThreeTreePosition280<K,I>) c).cursor;
			this.prev = ((TwoThreeTreePosition280<K,I>) c).prev;		
		}
		else {
			throw new InvalidArgument280Exception("The provided position was not a TwoThreeTreePosition280 object.");
		}
	}


	public void search(K k) {
		// TODO Position the cursor at the item with key k (if such an item exists).
		//  Don't use the cursor for this -- this will cause the search to be O(n).
		//  Instead, Use the inherited protected find() method to locate the node containing k if it exists,
		//  then adjust the cursor variables to refer to it.  find() is O(log n).
		//  If no item with key k can be found leave the cursor in the after position.

		if (this.find(this.rootNode,k) != null) {
			LinkedLeafTwoThreeNode280 nodeFound = (LinkedLeafTwoThreeNode280) this.find(this.rootNode,k);
			this.cursor = nodeFound;
			this.prev = nodeFound.prev();
		}
		else goAfter();
	}


	@Override
	public void searchCeilingOf(K k) {
		// Position the cursor at the smallest item that
		// has key at least as large as 'k', if such an
		// item exists.  If no such item exists, leave 
		// the cursor in the after position.
		
		// This one is easier to do with a linear search.
		// Could make it potentially faster but the solution is
		// not obvious -- just use linear search via the cursor.
		
		// If it's empty, do nothing; itemExists() will be false.
		if( this.isEmpty() ) 
			return;
		
		// Find first item item >= k.  If there is no such item,
		// cursor will end up in after position, and that's fine
		// since itemExists() will be false.
		this.goFirst();
		while(this.itemExists() && this.itemKey().compareTo(k) < 0) {
			this.goForth();
		}
		
	}

	@Override
	public void setItem(I x) throws NoCurrentItem280Exception,
            InvalidArgument280Exception {
		// TODO Store the item x in the node at which the cursor is positioned *if* the item in that node's key
		//   matches the key of x. Otherwise throw an exception indicating that the item in the node can't be replaced
		//   because it's key does not match the key of x.
		//

		if (itemKey().compareTo(x.key()) == 0){
			this.cursor.setData(x);
		}
		else throw new NoCurrentItem280Exception("The item in the node can't be replaced because it's key does not match the key of x.");
	}


	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		// TODO Remove the item at which the cursor is positioned from the tree.
		// Leave the cursor on the successor of the deleted item.

		this.delete(itemKey());
	}


	
	
	
    @Override
    public String toStringByLevel() {
        String s = super.toStringByLevel();
        
        s += "\nThe Linear Ordering is: ";
        CursorPosition280 savedPos = this.currentPosition();
        this.goFirst();
        while(this.itemExists()) {
            s += this.itemKey() + ", ";
            this.goForth();
        }
        this.goPosition(savedPos);
        
        if( smallest != null)
            s += "\nSmallest: " + this.smallest.getKey1();
        if( largest != null ) {
            s += "\nLargest: " + this.largest.getKey1();
        }
        return s;
    }

	public static void main(String args[]) {

		// A class for an item that is compatible with our 2-3 Tree class.  It has to implement Keyed280
		// as required by the class header of the 2-3 tree.  Keyed280 just requires that the item have a method
		// called key() that returns its key.  You *must* test your tree using Loot objects.

		class Loot implements Keyed280<String> {
			protected int goldValue;
			protected String key;

			@Override
			public String key() {
				return key;
			}
			
			@SuppressWarnings("unused")
			public int itemValue() {
				return this.goldValue;
			}

			Loot(String key, int i) {
				this.goldValue = i;
				this.key = key;
			}

		}
		
		// Create a tree to test with. 
		IterableTwoThreeTree280<String, Loot> T =
				new IterableTwoThreeTree280<String, Loot>();

		// An example of instantiating an item. (you can remove this if you wish)
		Loot sampleItem1 = new Loot("A", 1000);
		Loot sampleItem2 = new Loot("B", 2000);
		Loot sampleItem3 = new Loot("C", 3000);
		Loot sampleItem4 = new Loot("D", 4000);
		Loot sampleItem5 = new Loot("E", 5000);
		Loot sampleItem6 = new Loot("F", 6000);
		Loot sampleItem7 = new Loot("G", 7000);
		Loot sampleItem8 = new Loot("H", 8000);
		Loot sampleItem9 = new Loot("I", 9000);

		// Insert your first item! (you can remove this if you wish)
		T.insert(sampleItem1);
		T.insert(sampleItem2);
		T.insert(sampleItem3);
		T.insert(sampleItem4);
		T.insert(sampleItem5);
		T.insert(sampleItem6);
		T.insert(sampleItem7);
		T.insert(sampleItem8);
		T.insert(sampleItem9);


		// TODO Write your regression test here


		// TESTING goBefore() method.
		T.goBefore();

		if (!T.before()) {
			System.out.println("The cursor must be in before position but it points to "+T.itemKey() + ".");
		}

		// TESTING goAfter() method.
		T.goAfter();

		if (!T.after()){
			System.out.println("The cursor must be in after position but it points to "+T.itemKey() + ".");
		}

		// TESTING goForth() method.
		T.goBefore();
		T.goForth();

		if (T.item() != sampleItem1) {
			System.out.println("The cursor should point to "+sampleItem1.key() + " but it points to "+T.itemKey() + ".");
		}

		T.goForth();
		if (T.item() != sampleItem2) {
			System.out.println("The cursor should point to "+sampleItem2.key() + " but it points to "+T.itemKey() + ".");
		}

		T.goBefore();

		if (!T.before()){
			System.out.println("The cursor must be in the before position but points at "+ T.itemKey()+".");
		}

		T.goAfter();
		if (!T.after()){
			System.out.println("The cursor must be in the after position but points at "+ T.itemKey()+".");
		}

		// TESTING FOR search() method.

		T.goFirst();

		T.search("D");
		if (!T.itemKey().equals("D")){
			System.out.println("The cursor should point to "+sampleItem4.key() + " but it points to "+T.itemKey() + ".");
		}

		T.search("I");
		if (!T.itemKey().equals("I")){
			System.out.println("The cursor should point to "+sampleItem4.key() + " but it points to "+T.itemKey() + ".");

		}
		T.search("Z");
		if (!T.after()){
			System.out.println("The cursor should be at after position because Z doesn't exist.");
		}

		T.goBefore();
		T.goForth();
		if (T.item() != sampleItem1)
		{
			System.out.println("Error in goBefore(), goForth(), or item() method, expected result: " + sampleItem1 + ", instead have: "+ T.item());
		}
		// Checking if goForth() is working correctly, if goForth() works, then goBefore works based on the test cases.
		T.goForth();
		if (T.item() != sampleItem2)
		{
			System.out.println("Error in goForth(), or item() method, expected result" + sampleItem2 + ", instead have: " + T.item());
		}
		// goForth() method works perfectly fine, thus goBefore() and item() works fine
		T.goFirst();
		if (T.item() != sampleItem1)
		{
			System.out.println("Error in goFirst(), expected result: " + sampleItem1 +", instead have: "+ T.item());
		}
		// goFirst() method works correctly


		// Testing itemExists()
		T.goBefore();
		if (T.itemExists())
		{
			System.out.println("Method itemExists() should return false as there is no data at before position");
		}
		T.goForth();
		if (!T.itemExists())
		{
			System.out.println("Method itemExists() should return true as there is data at the first position, which is" +sampleItem1);
		}
		// itemExists() works perfectly
		T.goAfter();
		if (T.itemExists())
		{
			System.out.println("Error in goAfter() method, there is no data at the after position");
		}
		// Testing after() and before() method
		// Current position is after
		if (!T.after())
		{
			System.out.println("Method itemExists() should return false as there is no data at the after position");
		}
		T.goBefore();
		if (!T.before())
		{
			System.out.println("Method itemExists() should return false as there is no data at the before position");
		}
		// after() and before() method works correctly
		// Finish testing LinearIterator280 methods and Cursor280 methods
		// Testing itemKey() method
		T.goFirst();
		String string1 = "A";
		if (string1.compareTo(T.itemKey()) != 0)
		{
			System.out.println("Method itemKey() return incorrect value, expected result is "+ string1 + ", instead have " + T.itemKey());
		}
		String string2 = "B";
		T.goForth();
		if (string2.compareTo(T.itemKey()) != 0)
		{
			System.out.println("Method itemKey() return incorrect value ,expected result is " + string2 + ", instead have "+ T.itemKey());
		}
		T.goBefore();
		try
		{
			T.goBefore();
			String itemkey = T.itemKey();
		}
		catch (NoCurrentItem280Exception e)
		{}
		// itemKey() method works correctly
		T.goFirst();
		Pair280<String,Loot> pair1 = new Pair280<String, Loot>("A", sampleItem1);
		if (!pair1.firstItem().equals(T.keyItemPair().firstItem()) || pair1.secondItem() != T.keyItemPair().secondItem())
		{
			System.out.println("The method keyItemPair() does not return as it should be, expected output: " + pair1 + " , instead have " + T.keyItemPair());
		}
		T.goForth();
		Pair280<String,Loot> pair2 = new Pair280<String, Loot>("B", sampleItem2);
		if (!pair2.firstItem().equals(T.keyItemPair().firstItem())|| pair2.secondItem() != T.keyItemPair().secondItem())
		{
			System.out.println("The method keyItemPair() does not return as it should be, expected output: " + pair2 + " , instead have " + T.keyItemPair());
		}
		try
		{
			T.goAfter();
			Pair280<String,Loot> itempair  = T.keyItemPair();
		}
		catch (NoCurrentItem280Exception e) {}


		// itemKey() and keyItemPair() works correctly
		// Testing currentPosition() and goPosition() method
		T.goFirst();
		CursorPosition280 cursor =  T.currentPosition();
		T.goForth();
		if(!((TwoThreeTreePosition280)cursor).cursor.data.key().equals("A"))
		{
			System.out.println("The currentPosition() method doesn't point to the correct cursor");
		}
		CursorPosition280 cursor1 = T.currentPosition();
		if(!((TwoThreeTreePosition280)cursor1).cursor.data.key().equals("B"))
		{
			System.out.println("The currentPosition() method doesn't point to the correct cursor");
		}

		// currentPosition() works correctly

		String key1 = T.itemKey();
		T.goPosition(cursor);
		String key2 = T.itemKey();
		if (key1.compareTo(key2) == 0 || key2.compareTo("A") != 0)
		{
			System.out.println("The goPosition() method doesn't point to the correct cursor");
		}
		T.goPosition(cursor1);
		String key3= T.itemKey();
		if (key2.compareTo(key3) == 0  || key3.compareTo("B") !=0)
		{
			System.out.println("The goPosition() method doesnt' point to the correct cursor");
		}
		// goPosition() works correctly
		// Testing search() method
		T.search("B");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("B"))
		{
			System.out.println("The key of the current cursor when calling currentPosition() method should be B, instead have" + ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		T.search("A");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("A"))
		{
			System.out.println("The key of the current cursor when calling currentPosition() method should be A, instead have" + ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		T.search("H");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("H"))
		{
			System.out.println("The key of the current cursor when calling currentPosition() method should be H, instead have" + ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		T.search("I");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("I"))
		{
			System.out.println("The key of the current cursor when calling currentPosition() method should be I, instead have" + ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		// value is not in the tree
		T.search("Z");
		if (!T.after())
		{
			System.out.println("The current position should be at after position if search() method is looking for a value that doesn't exist in the tree");
		}
		// Search() method works correctly
		// Testing searchCeilingOf() method
		T.searchCeilingOf("E");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("E"))
		{
			System.out.println("The key where the cursor points to suppose to be E, instead have "+ ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		T.searchCeilingOf("A");
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals("A"))
		{
			System.out.println("The key where the cursor points to suppose to be A, instead have "+ ((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key());
		}
		// searchCeilingOf() method works correctly
		// Testing setItem() method


		T.goFirst();
		T.setItem(sampleItem1);
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals(sampleItem1.key))
		{
			System.out.println("The key at the current position should be the same as the key of sampleItem1, which is "+sampleItem1);
		}

		T.goForth();
		try {
			T.setItem(sampleItem1);
			if (!((TwoThreeTreePosition280) T.currentPosition()).cursor.data.key().equals(sampleItem1.key)) {
				System.out.println("The key at the current position should be the same as the key of sampleItem1, which is "+sampleItem1);
			}
		} catch (NoCurrentItem280Exception e){ }


		// Try to set item with sampleItem2 (which has the same key)
		T.setItem(sampleItem2);
		if (!((TwoThreeTreePosition280)T.currentPosition()).cursor.data.key().equals(sampleItem2.key))
		{
			System.out.println("The key at the current position should be the same as the key of sampleItem2, which is "+sampleItem2);
		}
		// setItem() method works correctly


		// Testing deleteItem() method
		T.goFirst();
		T.deleteItem();
		T.search("A");
		if (!T.after())
		{
			System.out.println("There is an error in deleteItem() method, search('A') should move the cursor to the after position.");
		}
		T.goFirst();
		T.deleteItem();
		T.search("B");
		if (!T.after())
		{
			System.out.println("There is an error in deleteItem() method, search('B') should move the cursor to the after position.");
		}
		T.goFirst();
		T.goForth();
		T.deleteItem();
		T.search("D");
		if (!T.after())
		{
			System.out.println("There is an errror in deleteItem() method, search('D') should move the cursor to the after position.");
		}

		// if you delete 6, the sequence is messed up (check toStringByLevel())
		T.goFirst();
		T.goForth();
		T.goForth();
		T.deleteItem();
		T.search("F");
		if (!T.after())
		{
			System.out.println("There is an error in deleteItem() method, search('F') should move the cursor to the after position.");
		}
		try
		{
			T.goBefore();
			T.deleteItem();
		} catch (NoCurrentItem280Exception e){}

		try{
			T.goAfter();
			T.deleteItem();
		} catch (NoCurrentItem280Exception e){}

		System.out.println("Regression Testing Completed.");
	}

}