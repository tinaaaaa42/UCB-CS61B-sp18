import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void randomTest() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        StringBuffer msg = new StringBuffer();

        int size = 0;

        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.25) {
                sad.addLast(i);
                ads.addLast(i);
                size++;
                msg.append("addLast(" + i + ")\n");
                assertEquals(msg.toString(), sad.get(size - 1), ads.get(size - 1));
            } else if (numberBetweenZeroAndOne < 0.5) {
                sad.addFirst(i);
                ads.addFirst(i);
                size++;
                msg.append("addFirst(" + i + ")\n");
                assertEquals(msg.toString(), sad.get(0), ads.get(0));
            } else if (numberBetweenZeroAndOne < 0.75) {
                if (sad.isEmpty()){
                    assertTrue(ads.isEmpty());
                    continue;
                }
                Integer x = sad.removeFirst();
                Integer y = ads.removeFirst();
                size--;
                msg.append("removeFirst()\n");
                assertEquals(msg.toString(), x, y);
            } else {
                if (sad.isEmpty()){
                    assertTrue(ads.isEmpty());
                    continue;
                }
                Integer x = sad.removeLast();
                Integer y = ads.removeLast();
                size--;
                msg.append("removeLast()\n");
                assertEquals(msg.toString(), x, y);
            }
        }

        sad.printDeque();
        ads.printDeque();
    }
}

