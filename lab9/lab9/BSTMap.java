package lab9;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author tinaaaaa42
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        
        if (p.key.equals(key)) {
            return p.value;
        } else if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        if (p.key.equals(key)) {
            p.value = value;
        } else if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }

        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values not allowed.");
        }
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        Iterator<K> iterator = new BSTIterator<>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V temp = get(key);
        if (temp == null) {
            return null;
        }
        Node node = locate(key, root);
        deleteTop(node);
        size -= 1;
        return temp;
    }

    /** returns the Node with the given key under p
     *  precondition: key exists under p */
    private Node locate(K key, Node node) {
        if (node.key.equals(key)) {
            return node;
        } else if (node.key.compareTo(key) > 0) {
            return locate(key, node.left);
        } else {
            return locate(key, node.right);
        }
    }

    /** delete the top under p (going down in left if existing) */
    public void deleteTop(Node p) {
        if (p.left != null) {
            Node downNode = p.left;
            Node parent = p;
            if (downNode.right == null) {
                p.left = downNode.left;
                p.key = downNode.key;
                p.value = downNode.value;
                return;
            }
            while (downNode.right != null) {
                parent = downNode;
                downNode = downNode.right;
            }
            parent.right = downNode.left;
            p.key = downNode.key;
            p.value = downNode.value;
        } else if (p.right != null) {
            Node downNode = p.right;
            Node parent = p;
            if (downNode.left == null) {
                p.right = downNode.right;
                p.key = downNode.key;
                p.value = downNode.value;
                return;
            }
            while (downNode.left != null) {
                parent = downNode;
                downNode = downNode.left;
            }
            parent.left = downNode.right;
            p.key = downNode.key;
            p.value = downNode.value;
        } else {
            p = null;
        }
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V temp = get(key);
        if (temp == null) {
            return null;
        }
        if (temp.equals(value)) {
            Node node = locate(key, root);
            deleteTop(node);
            size -= 1;
            return temp;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator<K>();
    }

    private class BSTIterator<K> implements Iterator<K> {
        private ArrayList<Node> nodeList;
        private Node node;
        private int cnt;
        public BSTIterator() {
            node = root;
            nodeList = new ArrayList<>();
            nodeList.add(root);
            cnt = 0;
        }
        @Override
        public boolean hasNext() {
            return cnt < size;
        }

        @Override
        public K next() {
            for (Node n: nodeList) {
                K temp = (K) n.key;
                if (!isOccupied(n.left)) {
                    nodeList.add(n.left);
                }
                if (!isOccupied(n.right)) {
                    nodeList.add(n.right);
                }
                nodeList.remove(n);
                cnt += 1;
                return temp;
            }
            return null;
        }

        private boolean isOccupied(Node n) {
            return n == null || nodeList.contains(n);
        }
    }

    @Test
    public void testRemove() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("a", 1);
        assertTrue(((Integer) b.remove("a")).equals(1));
        assertEquals(0, b.size());

        b.put("d", 2);
        b.put("b", 3);
        b.put("c", 4);
        assertTrue(((Integer) b.remove("b")).equals(3));
        assertEquals(2, b.size());

        assertTrue(b.remove("d", 1) == null);
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("d", 2);
        b.put("b", 3);
        b.put("c", 4);
        b.remove("d");
    }
}
