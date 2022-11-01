import org.junit.Test;
import static org.junit.Assert.*;

/** An Integer tester created by Flik Enterprises. */
public class Flik {
    public static boolean isSameNumber(Integer a, Integer b) {
        return a == b;
    }

    @Test
    public void testIsSameNumber() {
        assertTrue(isSameNumber(127, 127));
        assertTrue(isSameNumber(20, 20));
        assertTrue(!isSameNumber(20, 25));
    }
}
