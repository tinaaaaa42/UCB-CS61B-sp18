package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int sizeN;

    // To tell if the system is percolated
    private final WeightedQuickUnionUF percolationUnion;
    // To tell if the site is full
    private final WeightedQuickUnionUF unionWithoutBottom;
    private final int top;
    private final int bottom;
    private boolean[] sites;
    private int openNumber = 0;

    /**
     * create N-by-N grid, with all sites initially blocked
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        sizeN = N;
        percolationUnion = new WeightedQuickUnionUF(N * N + 2);
        unionWithoutBottom = new WeightedQuickUnionUF(N * N + 1);
        top = N * N;
        bottom = N * N + 1;
        sites = new boolean[N * N];

        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                sites[xyToIndex(i, j)] = false;
            }
        }
    }

    /**
     * check the index
     */
    private void indexChecker(int row, int col) {
        if (row < 0 || row >= sizeN || col < 0 || col >= sizeN) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * returns the index of (row, col)
     */
    private int xyToIndex(int row, int col) {
        return row * sizeN + col;
    }

    /** cope with the around sites when opening a site */
    public void aroundSites(int row, int col) {
        int currentIndex = xyToIndex(row, col);
        // top side
        if (row > 0 && isOpen(row - 1, col)) {
            percolationUnion.union(currentIndex, xyToIndex(row - 1, col));
            unionWithoutBottom.union(currentIndex, xyToIndex(row - 1, col));
        }
        // downside
        if (row < sizeN - 1 && isOpen(row + 1, col)) {
            percolationUnion.union(currentIndex, xyToIndex(row + 1, col));
            unionWithoutBottom.union(currentIndex, xyToIndex(row + 1, col));
        }
        // left side
        if (col > 0 && isOpen(row, col - 1)) {
            percolationUnion.union(currentIndex, xyToIndex(row, col - 1));
            unionWithoutBottom.union(currentIndex, xyToIndex(row, col - 1));
        }
        // right side
        if (col < sizeN - 1 && isOpen(row, col + 1)) {
            percolationUnion.union(currentIndex, xyToIndex(row, col + 1));
            unionWithoutBottom.union(currentIndex, xyToIndex(row, col + 1));
        }
    }

    /**
     * open the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        indexChecker(row, col);

        if (!isOpen(row, col)) {
            int index = xyToIndex(row, col);
            sites[index] = true;
            openNumber += 1;

            if (row == 0) {
                percolationUnion.union(top, index);
                unionWithoutBottom.union(top, index);
            }
            if (row == sizeN - 1) {
                percolationUnion.union(bottom, index);
            }

            aroundSites(row, col);
        }
    }

    /**
     * is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        indexChecker(row, col);
        return sites[xyToIndex(row, col)];
    }

    /**
     * is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        indexChecker(row, col);
        return unionWithoutBottom.connected(top, xyToIndex(row, col));
    }

    /**
     * number of open sites
     */
    public int numberOfOpenSites() {
        return openNumber;
    }

    /**
     * does the system percolate?
     */
    public boolean percolates() {
        return percolationUnion.connected(top, bottom);
    }
}