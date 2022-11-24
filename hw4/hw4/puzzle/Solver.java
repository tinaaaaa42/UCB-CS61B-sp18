package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {
    private int moves;
    private MinPQ<SearchNode> fringe;
    private Stack<WorldState> solution;
    private int totalEnqueue;

    private class SearchNode {
        private WorldState ws;
        private int moveToThis;
        private SearchNode previous;
        private int distanceToGoal;

        private SearchNode(WorldState ws, int moveToThis, SearchNode previous) {
            this.ws = ws;
            this.moveToThis = moveToThis;
            this.previous = previous;
            distanceToGoal = ws.estimatedDistanceToGoal();;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            return s1.distanceToGoal + s1.moveToThis - s2.distanceToGoal - s2.moveToThis;
        }
    }
    public Solver(WorldState initial) {
        fringe = new MinPQ<>(new SearchNodeComparator());
        solution = new Stack<>();
        fringe.insert(new SearchNode(initial, 0, null));
        totalEnqueue = 1;

        SearchNode end = null;
        while (!fringe.isEmpty()) {
            SearchNode currentNode = fringe.delMin();
            if (currentNode.ws.isGoal()) {
                moves = currentNode.moveToThis;;
                end = currentNode;
                break;
            }
            for (WorldState w: currentNode.ws.neighbors()) {
                if (currentNode.previous == null || !w.equals(currentNode.previous.ws)) {
                    fringe.insert(new SearchNode(w, currentNode.moveToThis + 1, currentNode));
                    totalEnqueue += 1;
                }
            }
        }
        for (SearchNode s = end; s != null; s = s.previous) {
            solution.push(s.ws);
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

    private int getTotalEnqueue() {
        return totalEnqueue;
    }
}
