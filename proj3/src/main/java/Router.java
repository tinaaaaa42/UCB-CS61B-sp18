import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long start = g.closest(stlon, stlat);
        long destination = g.closest(destlon, destlat);
        HashMap<Long, Double> disToTarget = new HashMap<>();
        HashMap<Long, Long> edges = new HashMap<>();
        HashSet<Long> marked = new HashSet<>();

        PriorityQueue<Long> fringe = new PriorityQueue<>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                double d1 = disToTarget.get(o1) + g.distance(o1, destination);
                double d2 = disToTarget.get(o2) + g.distance(o2, destination);

                if (d1 > d2) {
                    return 1;
                } else if (d1 == d2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (long id: g.vertices()) {
            disToTarget.put(id, Double.POSITIVE_INFINITY);
            edges.put(id, -1L);
        }
        disToTarget.put(start, 0.0);
        edges.put(start, 0L);
        fringe.add(start);

        while (!fringe.isEmpty()) {
            long node = fringe.poll();
            if (node == destination) {
                break;
            }
            if (!marked.contains(node)) {
                marked.add(node);
                for (long neighbor: g.adjacent(node)) {
                    double dis = disToTarget.get(node) + g.distance(neighbor, node);
                    if (dis < disToTarget.get(neighbor)) {
                        disToTarget.put(neighbor, dis);
                        fringe.add(neighbor);
                        edges.put(neighbor, node);
                    }
                }
            }
        }

        LinkedList<Long> path = new LinkedList<>();
        for (long last = destination; last != 0L; last = edges.get(last)) {
            path.addFirst(last);
        }
        return path;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> directionList = new LinkedList<>();

        int preDir = NavigationDirection.START, curDir;
        
        String preWay = g.nodeList.get(route.get(0)).getName(), curWay = "";

        NavigationDirection first = new NavigationDirection();
        first.distance = g.distance(route.get(0), route.get(route.size() - 1));
        first.way = preWay;
        first.direction = preDir;

        directionList.add(first);
        for (int i = 1; i < route.size(); i++) {
            long start = route.get(i - 1), end = route.get(i);
            curDir = getDirection(g.bearing(start, end));
            curWay = g.nodeList.get(route.get(i)).getName();
            // if there is a change of way
            if (curDir != preDir) {
                NavigationDirection instance = new NavigationDirection();
                instance.way = curWay;
                instance.distance = g.distance(start, end);
                instance.direction = curDir;
                System.out.println(g.bearing(start, end) + curDir + ", " + curWay);
                directionList.add(instance);
            }
            preDir = curDir;
            preWay = curWay;
        }
        return directionList;
    }

    private static int getDirection(double bearing) {
        if (bearing >= -15 && bearing <= 15) {
            return NavigationDirection.STRAIGHT;
        } else if (bearing >= -30) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (bearing <= 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (bearing >= -100) {
            return NavigationDirection.LEFT;
        } else if (bearing <= 100) {
            return NavigationDirection.RIGHT;
        } else if (bearing < -100) {
            return NavigationDirection.SHARP_LEFT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }
    }
    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
