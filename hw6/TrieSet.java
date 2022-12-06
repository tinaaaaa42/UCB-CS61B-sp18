import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class TrieSet {
    private class Node {
        boolean exists;
        Map<Character, Node> links;

        public Node() {
            links = new HashMap<>();
            exists = false;
        }
    }

    private Node root = new Node();
    /** the public version of put to add a word to the set */
    public void put(String key) {
        put(root, key, 0);
    }

    /**
     *  a helper method to put a word to the set
     *  @param x the top node
     *  @param key the word to add
     *  @param d the current character
     *  @return the position that the word added to
     */
    private Node put(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.exists = true;
            return x;
        }

        char c = key.charAt(d);
        x.links.put(c, put(x.links.get(c), key, d + 1));
        return x;
    }

    /** the public version of find to find a word in the set */
    public boolean find(String key) {
        return find(root, key, 0);
    }

    /**
     *  a helper method to find whether the word exists in the set
     *  @param x the top node
     *  @param key the word to find
     *  @param d the current character
     *  @return true if existing in the set
     */
    private boolean find(Node x, String key, int d) {
        if (d == key.length() && x.exists) {
            return true;
        } else if (d == key.length()) {
            return false;
        } else {
            char c = key.charAt(d);
            if (!x.links.containsKey(c)) {
                return false;
            } else {
                d += 1;
                return find(x.links.get(c), key, d);
            }
        }
    }

    /** the public version of prefixFind to find the prefix in the set */
    public boolean prefixFind(String key) {
        return prefixFind(root, key, 0);
    }

    /**
     *  a helper method to find whether the prefix exists in the set
     *  @param x the top node
     *  @param key the prefix to find
     *  @param d the current character
     *  @return true if existing in the set
     */
    private boolean prefixFind(Node x, String key, int d) {
        if (d == key.length()) {
            return true;
        }
        char c = key.charAt(d);
        if (!x.links.containsKey(c)) {
            return false;
        } else {
            d += 1;
            return prefixFind(x.links.get(c), key, d);
        }
    }
    @Test
    public void putAndFindTest() {
        TrieSet ts = new TrieSet();
        ts.put("abandon");
        ts.put("ability");
        assertTrue(ts.find("abandon"));
        assertFalse(ts.find("pig"));
        assertTrue(ts.prefixFind("ab"));
        assertTrue(ts.prefixFind("aba"));
        assertFalse(ts.prefixFind("abo"));
    }

    public static void main(String[] args) {
        In database = new In("words.txt");
        TrieSet ts = new TrieSet();
        while (!database.isEmpty()) {
            String word = database.readString();
            ts.put(word);
        }

        System.out.println(ts.prefixFind("as"));
    }
}
