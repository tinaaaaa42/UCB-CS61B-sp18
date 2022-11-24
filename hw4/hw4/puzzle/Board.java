package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int [][] tiles;
    private int sizeN;
    private final int BLANK = 0;

    public Board(int [][] tiles) {
        sizeN = tiles.length;
        this.tiles = new int[sizeN][sizeN];
        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    private void indexChecker(int i, int j) {
        if (i < 0 || i > sizeN - 1 || j < 0 || j > sizeN - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }
    public int tileAt(int i, int j) {
        indexChecker(i, j);
        return tiles[i][j];
    }

    public int size() {
        return sizeN;
    }

    /** @source: http://joshh.ug/neighbors.html*/
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int totalHamming = 0;
        int goal = 1;
        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                if (tileAt(i, j) != goal) {
                    totalHamming += 1;
                }
                goal += 1;
            }
            if (goal == sizeN * sizeN) {
                break;
            }
        }
        return totalHamming;
    }

    public int manhattan() {
        int totalManhattan = 0;
        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                int actual = tileAt(i, j);
                if (actual == BLANK) {
                    continue;
                }
                int supposedRow = (actual - 1) / sizeN;
                int supposedCol = (actual - 1) % sizeN;
                totalManhattan += Math.abs(supposedRow - i) + Math.abs(supposedCol - j);
            }
        }
        return totalManhattan;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Board board = (Board) obj;
        if (board.sizeN != this.sizeN) {
            return false;
        }
        for (int i = 0; i < sizeN; i += 1) {
            for (int j = 0; j < sizeN; j += 1) {
                if (board.tileAt(i, j) != this.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
