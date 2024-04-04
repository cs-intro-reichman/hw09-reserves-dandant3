import java.util.ListIterator;
import java.util.NoSuchElementException;

/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    private class ListIteratorImpl implements ListIterator<CharData> {
        private Node current;
        private Node previous;
        private int nextIndex;

        public ListIteratorImpl(Node startNode, int startIndex) {
            this.current = startNode;
            this.nextIndex = startIndex;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public CharData next() {
            if (!hasNext()) throw new NoSuchElementException();
            CharData data = current.cp;
            previous = current;  // Update previous before moving current forward
            current = current.next;
            nextIndex++;
            return data;
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException("Not implemented.");
        }

        @Override
        public CharData previous() {
            throw new UnsupportedOperationException("Not implemented.");
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }

        @Override
        public void set(CharData e) {
            throw new UnsupportedOperationException("Not implemented.");
        }

        @Override
        public void add(CharData e) {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newFirst = new Node(newCharData);
        newFirst.next = this.first; 
        this.first = newFirst; 
        this.size++; // Ensure this line is correctly executing
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        String allText = "";
        Node pointer = this.first;
        while (pointer != null){
            allText += pointer.toString();
            pointer = pointer.next;
        }
        return allText; 
    }
    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node pointer = this.first;
        int index = 0;
        while (pointer != null) {
            if (pointer.cp.chr == chr) {
                return index;
            }
            pointer = pointer.next;
            index++;
        }
        return -1; // Indicating not found
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        Node pointer = this.first;
        while (pointer != null){
            if(pointer.cp.chr == chr){
                pointer.cp.count++;
                return;
            }
            pointer = pointer.next;
        }
        this.addFirst(chr);
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if(this.first == null){
            return false;
        }
        if (this.first.cp.chr == chr) {
            this.first = this.first.next;
            this.size--;
            return true;            
        }
        Node pointer = this.first.next;
        Node pointerPrev = this.first;
        while (pointer != null){
            if(pointer.cp.chr == chr){
                pointerPrev.next = pointer.next;
                this.size--;
                return true;
            }
            pointer = pointer.next;
            pointerPrev = pointerPrev.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        Node pointer = this.first;
        for (int i = 0; i < index; i++) {
            pointer = pointer.next;
        }
        return pointer.cp; 
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator<CharData> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        Node current = first;
        for (int i = 0; i < index && current != null; i++) {
            current = current.next;
        }
        return new ListIteratorImpl(current, index);
    }
}

