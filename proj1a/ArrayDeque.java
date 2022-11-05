public class ArrayDeque<T> {

    private T[] data;
    private int nextFirst;
    private int nextLast;
    private int size;
    private final int reFactor = 2;
    private final double maxUsage = 0.25;

    public ArrayDeque() {
        data = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    /** Returns the usage of the array. */
    private double usage() {
        if (size >= 16) {
            return (double) size / data.length;
        }
        return maxUsage;
    }

    /** Resizes when the array is full.
     *  Precondition: The array is full, namely size == data.length */
    private void addResize() {
        int first = (nextFirst + 1) % data.length;

        int newSize = data.length * reFactor;
        T[] newData = (T[]) new Object[newSize];

        int i = first;
        for (int j = 0; j < size; j++) {
            newData[j] = data[i];
            i = (i + 1) % data.length;
        }
        nextFirst = newSize - 1;
        nextLast = size;
        data = newData;
    }

    /** Resizes when the array decreases.
     *  Precondition: the usage of the array is less than 0.25 */
    private void removeResize() {
        while (usage() > maxUsage) {
            int first = (nextFirst + 1) % data.length;

            int newSize = data.length / 2;
            T[] newData = (T[]) new Object[newSize];

            int i = first;
            for (int j = 0; j < size; j++) {
                newData[j] = data[i];
                i = (i + 1) % data.length;
            }
            nextFirst = newSize - 1;
            nextLast = size;

            data = newData;
        }
    }

    /** Adds an item of T to the front of the deque. */
    public void addFirst(T item) {
        if (isFull()) {
            addResize();
        }
        data[nextFirst] = item;
        nextFirst = (nextFirst - 1 + data.length) % data.length;
        size++;
    }

    /** Adds an item of T to the back of the deque. */
    public void addLast(T item) {
        if (isFull()) {
            addResize();
        }
        data[nextLast] = item;
        nextLast = (nextLast + 1) % data.length;
        size++;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return (size == 0);
    }

    /** Returns true if deque is full, false otherwise. */
    private boolean isFull() {
        return (size == data.length);
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to nextLast, separated by a space. */
    public void printDeque() {
        if (size == 0) {
            return;
        }
        int first = (nextFirst + 1) % data.length;
        int last = (nextLast - 1 + data.length) % data.length;
        if (first <= last) {
            for (int i = first; i <= last; i++) {
                System.out.print(data[i] + " ");
            }
        } else {
            for (int i = first; i < data.length; i++) {
                System.out.print(data[i] + " ");
            }
            for (int i = 0; i < nextLast; i++) {
                System.out.print(data[i] + " ");
            }
        }
    }

    /** Removes and returns the item at the front of the deque.
     *  If no such item exists, return null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int first = (nextFirst + 1) % data.length;
        T temp = data[first];
        nextFirst = first;
        size--;
        return temp;
    }

    /** Removes and returns the item at the back of the deque.
     *  If no such item exists, return null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        int last = (nextLast - 1 + data.length) % data.length;
        T temp = data[last];
        nextLast = last;
        size--;

        removeResize();

        return temp;
    }
    /** Gets the item at the given index,
     *  where 0 is the front, 1 is the next item, and so forth.
     *  If no such item exists, returns null. Must not alter the deque! */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int i = (nextFirst + 1 + index) % data.length;
        return data[i];
    }
}
