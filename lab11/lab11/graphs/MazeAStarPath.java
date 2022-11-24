package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private MinPQ<Integer> fringe;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new MinPQ<>(new AComparator());
        fringe.insert(s);
    }

//    private class Stuff {
//        int totalDis;
//    }
    private class AComparator<Integer> implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            return distTo[(int) i1] + h((int) i1) - distTo[(int) i2] - h((int) i2);
        }
    }
    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int x = maze.toX(v);
        int y = maze.toY(v);
        return Math.abs(maze.N() - x) + Math.abs(maze.N() - y);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        if (s == t) {
            targetFound = true;
            return;
        }

        while (!fringe.isEmpty()) {
            for (int i : maze.adj(s)) {
                if (!marked[i]) {
                    fringe.insert(i);
                    edgeTo[i] = s;
                    distTo[i] = distTo[s] + 1;
                }
            }
            int min = fringe.delMin();
            astar(min);
            announce();
            if (targetFound) {
                return;
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

