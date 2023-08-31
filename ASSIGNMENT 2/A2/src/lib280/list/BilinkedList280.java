/**
 * NAME         :       JEET AGRAWAL
 * NSID         :       jea316
 * STUDENT ID  :       11269096
 */
package lib280.list;

import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		return new BilinkedNode280<I>(item);  // Added this line to the code which return the BiLinked List Node.
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		// TODO
		BilinkedNode280<I> newItem = createNewNode(x);

		newItem.setNextNode(this.head);
		newItem.setPreviousNode(null);

		this.head = newItem;
//		((BilinkedNode280<I>)newItem.nextNode()).setPreviousNode((BilinkedNode280<I>)this.head);

		if (!this.isEmpty() && this.position == this.head) this.prevPosition = newItem;

		if (this.isEmpty()){this.head = newItem; this.tail = newItem; newItem.setPreviousNode(null);}

		else {
			((BilinkedNode280<I>)this.head).setPreviousNode(newItem);
			newItem.setNextNode(head);
		}
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
		// TODO
		BilinkedNode280<I> lastItem = createNewNode(x);
		lastItem.setNextNode(null);

		if( !isEmpty() && this.after() ) this.prevPosition = lastItem;

		if (this.isEmpty())
			{
				this.head = lastItem; this.tail = lastItem; lastItem.setPreviousNode(null);
			}
		else{
				this.tail.setNextNode(lastItem);
				lastItem.setPreviousNode((BilinkedNode280<I>)this.tail);
				this.tail = lastItem;
			}
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		// TODO
		if(!this.itemExists()) throw new NoCurrentItem280Exception("There is no item at the cursor to delete.");

		//If we want to delete the first item

		if (this.position == this.head){
			this.deleteFirst();
		}
		else if (this.position == this.tail){
			this.deleteLast();
		}
//		else{
//
//		}

	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		// TODO
		if (this.isEmpty()) throw new ContainerEmpty280Exception("Cannot delete first because List is empty.");

		this.head = this.head.nextNode();
		if (this.head == this.tail){
			this.tail = null;
			this.head = null;
		}
		else{
			((BilinkedNode280<I>)this.head).setPreviousNode(null);
		}

		this.position = this.head;

	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		// TODO
		if (this.isEmpty()) throw new ContainerEmpty280Exception("Cannot delete last because List is empty.");

		this.tail = ((BilinkedNode280)this.tail).previousNode;
		this.tail.setNextNode(null);

		goAfter();

	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		// TODO

		if (this.isEmpty()) throw new ContainerEmpty280Exception("Cannot iterate in empty list.");
		this.prevPosition = ((BilinkedNode280<I>)this.tail).previousNode;
		this.position = tail;
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// TODO

		if (this.prevPosition == null) throw new BeforeTheStart280Exception("");

		this.position = this.prevPosition;
		this.prevPosition = ((BilinkedNode280<I>)prevPosition).previousNode;

	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		// TODO

		//Probably doesn't achieve 100% coverage, but comes pretty close.

		BilinkedList280<Integer> L = new BilinkedList280<Integer>();

		// test isEmpty, isFull, insert, insertLast, insert,
		// toString() (which implicitly tests iteration to some extent)

		System.out.println(L);

		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");


		L.insert(5);

		// At this point the cursor should be in the "before" position.
		if( !L.before() )
			System.out.println("Error: cursor should be in the before() position after inserting into an empty list and it is not.");

		L.insert(4);
//		L.insertLast(3);
//		System.out.println("after insert");
//		L.insertLast(10);
		L.insertFirst(3);
		L.insertFirst(4);
		//L.insertFirst(5);

		L.insertFirst(2);

		System.out.print("List should be 'not full'...");
		if( !L.isFull() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.println("List should be: 2, 4, 5, 3, 10, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		// Test delete methods
		L.delete(5);

		System.out.println("List should be: 2, 4, 3, 10, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		L.deleteFirst();
		System.out.println("List should be: 4, 3, 10, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);


		L.deleteLast();
		System.out.println("List should be: 4, 3, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	}
}