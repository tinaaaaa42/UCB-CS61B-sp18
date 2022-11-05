public interface Deque<T> {
    /** Adds an item of T to the front of the deque. */
    void addFirst(T item);

    /** Adds an item of T to the back of the deque. */
    void addLast(T item);

    /** Returns true if deque is empty, false otherwise. */
    boolean isEmpty();

    /** Returns the number of items in the deque. */
    int size();

    /** Prints the items in the deque from first to nextLast, separated by a space. */
    void printDeque();

    /** Removes and returns the item at the front of the deque.
     *  If no such item exists, return null. */
    T removeFirst();

    /** Removes and returns the item at the back of the deque.
     *  If no such item exists, return null. */
    T removeLast();

    /** Gets the item at the given index,
     *  where 0 is the front, 1 is the next item, and so forth.
     *  If no such item exists, returns null. Must not alter the deque! */
    T get(int index);
}
