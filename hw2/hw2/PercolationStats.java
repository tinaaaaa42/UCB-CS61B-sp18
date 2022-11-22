package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final int totalTime;
    private int[] experiment;
    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        totalTime = T;
        experiment = new int[T];

        for (int i = 0; i < N; i += 1) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            experiment[i] = p.numberOfOpenSites() / (N * N);
        }
    }

    /** sample mean of percolation threshold */
    public double mean() {
        return StdStats.mean(experiment);
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return StdStats.stddev(experiment);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / java.lang.Math.sqrt(totalTime);
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / java.lang.Math.sqrt(totalTime);
    }
}
