import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static TrieSet ts;
    private static char[][] board;
    private static int row;
    private static int width;
    private static PriorityQueue<String> wordsPQ = new PriorityQueue<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() < o2.length()) {
                return 1;
            } else if (o1.length() > o2.length()) {
                return -1;
            } else {
                return o1.compareTo(o2);
            }
        }
    });

    private static class Position {
        int row;
        int col;

        Position(int r, int c) {
            row = r;
            col = c;
        }
    }

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k <= 0) {
            throw new IllegalArgumentException("k should be positive!");
        }
        getDatabase();
        getBoard(boardFilePath);
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < width; j += 1) {
                start(new Position(i, j));
            }
        }

        int min;
        if (k < wordsPQ.size()) {
            min = k;
        } else {
            min = wordsPQ.size();
        }

        ArrayList<String> words = new ArrayList<>();
        int cnt = 0;
        String nextWord;
        while (cnt < min) {
            nextWord = wordsPQ.poll();
            if (!words.contains(nextWord)) {
                words.add(nextWord);
                cnt += 1;
            }
        }
        return words;
    }

    /** get all the words into the TrieSet */
    private static void getDatabase() {
        In database = new In(dictPath);
        ts = new TrieSet();
        while (!database.isEmpty()) {
            String word = database.readString();
            ts.put(word);
        }
    }

    /** get the board from the given path
     *  @param boardFilePath The file path to Boggle board file.
     */
    private static void getBoard(String boardFilePath) {
        In inBoard = new In(boardFilePath);
        row = 1;
        List<String> temp = new ArrayList<>();
        temp.add(inBoard.readString());

        width = temp.get(0).length();
        while (!inBoard.isEmpty()) {
            row += 1;
            String string = inBoard.readString();
            if (string.length() != width) {
                throw new IllegalArgumentException(" require rectangle board");
            }
            temp.add(string);
        }

        board = new char[row][width];
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < width; j += 1) {
                board[i][j] = temp.get(i).charAt(j);
            }
        }
    }

    private static void start(Position p) {
        String string = String.valueOf(board[p.row][p.col]);
        boolean[][] track = new boolean[row][width];
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < width; j += 1) {
                track[i][j] = false;
            }
        }
        track[p.row][p.col] = true;
        move(track, p, string);
    }

    private static void move(boolean[][] track, Position p, String s) {
        for (Position position: adjacent(p)) {
            boolean[][] newTrack = new boolean[row][width];
            for (int i = 0; i < row; i += 1) {
                for (int j = 0; j < width; j += 1) {
                    newTrack[i][j] = track[i][j];
                }
            }

            if (!newTrack[position.row][position.col]) {
                newTrack[position.row][position.col] = true;
                String newString = s + board[position.row][position.col];
                if (ts.prefixFind(newString)) {
                    if (ts.find(newString)) {
                        wordsPQ.add(newString);
                    }
                    move(newTrack, position, newString);
                }
            }
        }
    }

    private static List<Position> adjacent(Position p) {
        ArrayList<Position> adj = new ArrayList<>();
        for (int i = p.row - 1; i <= p.row + 1; i += 1) {
            for (int j = p.col - 1; j <= p.col + 1; j += 1) {
                if (legalIndex(i, j) && !(i == p.row && j == p.col)) {
                    adj.add(new Position(i, j));
                }
            }
        }
        return adj;
    }

    private static boolean legalIndex(int r, int col) {
        return r >= 0 && r < row && col >= 0 && col < width;
    }

    public static void main(String[] args){
        System.out.println(solve(Integer.valueOf(args[0]) ,args[1]));
    }
}
