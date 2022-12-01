import edu.princeton.cs.algs4.Queue;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLength = 0;
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            maxLength = Math.max(asciis[i].length(), maxLength);
            sorted[i] = asciis[i];
        }
        for (int i = maxLength - 1; i >= 0; i--) {
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        Queue<String>[] elementsQueue = new Queue[256];
        for (String s : asciis) {
            int bucket;
            if (index >= s.length()) {
                bucket = '_';
            } else {
                bucket = s.charAt(index);
            }
            if (elementsQueue[bucket] == null) {
                elementsQueue[bucket] = new Queue<>();
            }
            elementsQueue[bucket].enqueue(s);
        }
        int i = 0;
        for (Queue<String> q : elementsQueue) {
            while (q != null && !q.isEmpty()) {
                asciis[i] = q.dequeue();
                i ++;
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
