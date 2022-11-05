import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void testAdd() {
        // Test addFirst and addLast
        ArrayDeque<Integer> aDList = new ArrayDeque<Integer>();
        aDList.addFirst(1);
        aDList.addLast(4);
        aDList.addFirst(3);
        aDList.addLast(2);
        aDList.printDeque();
    }

    @Test
    public void testRemove() {
        // Test removeFirst and removeLast
        ArrayDeque<Integer> aDList = new ArrayDeque<Integer>();
        aDList.addFirst(1);
        aDList.addFirst(2);
        aDList.addFirst(4);
        aDList.printDeque();
        aDList.removeFirst();
        aDList.printDeque();
        aDList.removeLast();
        aDList.printDeque();
    }

}
