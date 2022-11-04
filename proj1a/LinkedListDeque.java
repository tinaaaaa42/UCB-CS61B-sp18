public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(Node front, T i, Node back) {
            prev = front;
            item = i;
            next = back;
        }
    }
    private int size;
    private Node sentinel;

    /** initialize a linked list deque*/
    public LinkedListDeque () {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Adds an item of T to the prev of the deque. */
    public void addFirst(T item) {
        size ++;
        sentinel.next = new Node(sentinel, item, sentinel.next); // add the first and link sentinel and first
        sentinel.next.next.prev = sentinel.next; // link the first and the second
    }

    /** Adds an item of T to the back of the deque. */
    public void addLast(T item) {
        size ++;
        sentinel.prev = new Node(sentinel, item, sentinel.prev); // add the last one and link the last and sentinel
        sentinel.prev.prev.next = sentinel.prev; // link the last and second last
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.item + " ");
            p = p.next;
        }
    }

    /** Removes and returns the item at the prev of the deque. If no such item exists, return null. */
    public T removeFirst() {
        T temp = sentinel.next.item;
        if (size > 0) {
            sentinel.next.next.prev = sentinel; // disconnect the first and second
            sentinel.next = sentinel.next.next;
            size--;
        }
        return temp;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, return null. */
    public T removeLast() {
        T temp = sentinel.prev.item;
        if (size > 0) {
            sentinel.prev.prev.next = sentinel; // disconnect the last and second last
            sentinel.prev = sentinel.prev.prev;
            size --;
        }
        return temp;
    }

    /** Gets the item at the given index, where 0 is the prev, 1 is the next item, and so forth.
     *    If no such item exists, returns null. Must not alter the deque! */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    /** Same as get, but uses recursion. */
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getR(index , sentinel.next);
    }
    private T getR(int index, Node n) {
        if (index == 0) {
            return n.item;
        }
        return getR(index - 1, n.next);
    }
}
