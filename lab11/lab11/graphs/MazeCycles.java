package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private boolean isFound = false;
    private int[] parents;
    public MazeCycles(Maze m) {
        super(m);
        s = maze.xyTo1D(1, 1);
        parents = new int[maze.V()];
        parents[s] = 0;
    }

    @Override
    public void solve() {
        findCycle(s);
    }

    // Helper methods go here
    private void findCycle(int v) {
        marked[v] = true;
        if (isFound) {
            return;
        }

        for (int s: maze.adj(v)) {
            if (!marked[s]) {
                parents[s] = v;
                findCycle(s);
            } else if (parents[v] != s) {
                marked[s] = true;
                isFound = true;
                parents[s] = v;
                int p = v;
                while (p != s) {
                    edgeTo[p] = parents[p];
                    p = parents[p];
                }
                edgeTo[p] = v;
                announce();
                return;
            }
        }
    }
}

