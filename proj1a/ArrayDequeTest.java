import org.junit.Test;
import org.junit.Assert;

public class ArrayDequeTest {
    @Test
    public void TestAdd() {
        // Test addFirst and addLast
        ArrayDeque<Integer> ADList = new ArrayDeque<Integer>();
        ADList.addFirst(1);
        ADList.addLast(4);
        ADList.addFirst(3);
        ADList.addLast(2);
        ADList.printDeque();
    }

    @Test
    public void TestRemove() {
        // Test removeFirst and removeLast
        ArrayDeque<Integer> ADList = new ArrayDeque<Integer>();
        ADList.addFirst(1);
        ADList.addFirst(2);
        ADList.addFirst(4);
        ADList.printDeque();
        ADList.removeFirst();
        ADList.printDeque();
        ADList.removeLast();
        ADList.printDeque();
    }

//    public static void main(String[] args) {
//        TestAdd();
//    }
}
