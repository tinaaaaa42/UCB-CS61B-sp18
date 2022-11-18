package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator implements Serializable {
    public TETile[][] world;
    public int[][] isOccupied;
    private final int width;
    private final int height;
    Random r;
    private final int ROOMS = 20;
    private Room[] room = new Room[ROOMS];
    public Position playerPosition;

    public static class Position implements Serializable{
        public int x;
        public int y;

        public Position(int px, int py) {
            x = px;
            y = py;
        }

        public boolean equal(Position p) {
            return x == p.x && y == p.y;
        }

    }

    private class Room implements Serializable {
        private WorldGenerator.Position position;
        private int roomWidth;
        private int roomHeight;
        private WorldGenerator.Position[] edges;
        private int edgeTotalNumber;
        private int edgesNumber = 0;

        private Position linkPosition;

        private Room(WorldGenerator.Position p, int sizeX, int sizeY) {
            position = p;
            roomWidth = sizeX;
            roomHeight = sizeY;

            if (roomHeight == 1) {
                edgeTotalNumber = roomWidth;
            } else if (roomWidth == 1) {
                edgeTotalNumber = roomHeight;
            } else {
                edgeTotalNumber = 2 * (roomWidth + roomHeight) - 4;
            }
            edges = new Position[2 * edgeTotalNumber];
            for (int i = 0; i < 2 * edgeTotalNumber; i += 1) {
                edges[i] = new Position(0, 0);
            }
            getEdges();
            linkPosition = getLinkPosition(r);
        }

        public void getEdges() {
            for (int x = 0; x < roomWidth; x += 1) {
                if (position.y > 0 && isOccupied[position.x + x][position.y - 1] != 0) {
                    edges[edgesNumber].x = position.x + x;
                    edges[edgesNumber].y = position.y;
                    edgesNumber += 1;
                }
                if (position.y + roomHeight < height && isOccupied[position.x + x][position.y + roomHeight] != 0) {
                    edges[edgesNumber].x = position.x + x;
                    edges[edgesNumber].y = position.y + roomHeight - 1;
                    edgesNumber += 1;
                }
            }
            for (int y = 1; y < roomHeight - 1; y += 1) {
                if (position.x > 0 && isOccupied[position.x - 1][position.y + y] != 0) {
                    edges[edgesNumber].x = position.x;
                    edges[edgesNumber].y = position.y + y;
                    edgesNumber += 1;
                }
                if (position.x + roomWidth < width && isOccupied[position.x + roomWidth][position.y + y] != 0) {
                    edges[edgesNumber].x = position.x + roomWidth - 1;
                    edges[edgesNumber].y = position.y + y;
                    edgesNumber += 1;
                }
            }
        }

        private Position getLinkPosition(Random r) {
            if (edgesNumber == 0) {
                return position;
            }
            Position link = edges[r.nextInt(edgesNumber)];

            return link;
        }
    }
    /** generates a new world in the given size with seed */
    public WorldGenerator(int w, int h, long seed) {
        r = new Random(seed);
        width = w;
        height = h;
        world = new TETile[width][height];
        isOccupied = new int[width][height];

        // initialize the world
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
                isOccupied[x][y] = 0;
            }
        }

        // generate the room and extend
        for (int i = 0; i < ROOMS; i += 1) {
            Position p =getAPosition(r);
            extendRoom(i, p, r, Tileset.FLOOR);
            // world[room[i].linkPosition.x][room[i].linkPosition.y] = Tileset.MOUNTAIN;
            // world[room[i].position.x][room[i].position.y] = Tileset.WALL;
        }

        // cope with the inside room / linkPosition  do nothing seems alright as well

        // link the link position (only one link point version)
        for (int i = 0; i < ROOMS - 1; i += 1) {
            if (!room[i].linkPosition.equal(new Position(0, 0))) {
                link(room[i].linkPosition, room[i + 1].linkPosition, Tileset.FLOOR);
            }
        }

        // generate the wall
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                wallGenerator(new Position(x, y));
            }
        }

        // generate a player
        playerGenerator(r);
    }

    /** Randomly get a position */
    public Position getAPosition(Random r) {
        int x = r.nextInt(width - 2) + 1;
        int y = r.nextInt(height - 2) + 1;

        return new Position(x, y);
    }

    /** generates a rectangle room in the given position
     *  the size of room ranges from 1*1 to 6*6 */
    public void extendRoom(int i, Position p, Random r, TETile t) {
        int sizeX = r.nextInt(6) + 1;
        int sizeY = r.nextInt(6) + 1;

        // index out of bound
        if (sizeX + p.x >= width) {
            sizeX = width - p.x;
        }
        if (sizeY + p.y >= height) {
            sizeY = height - p.y;
        }

        room[i] = new Room(p, sizeX, sizeY);
        // place already occupied  ignore them?

        for (int x = p.x; x < p.x + sizeX; x += 1) {
            for (int y = p.y; y < p.y + sizeY; y += 1) {
                world[x][y] = t;
                isOccupied[x][y] = 1;
            }
        }
    }

    /** generates a path between two given position
     *  just be straight */
    public void link(Position start, Position end, TETile t) {
        if (start.x - end.x > 0) {
            for (int x = 1; x <= start.x - end.x; x += 1) {
                world[end.x + x][start.y] = t;
                isOccupied[end.x + x][start.y] = 1;
            }
        } else {
            for (int x = 1; x <= end.x - start.x; x += 1) {
                world[start.x + x][start.y] = t;
                isOccupied[start.x + x][start.y] = 1;
            }
        }

        if (start.y - end.y > 0) {
            for (int y = 1; y <= start.y - end.y; y += 1) {
                world[end.x][end.y + y] = t;
                isOccupied[end.x][end.y + y] = 1;
            }
        } else {
            for (int y = 1; y <= end.y - start.y; y += 1) {
                world[end.x][start.y + y] = t;
                isOccupied[end.x][start.y + y] = 1;
            }
        }
    }

    public void wallGenerator(Position p) {
        if (isOccupied[p.x][p.y] > 0) {
            // left side
            if (p.x > 0 && isOccupied[p.x - 1][p.y] == 0) {
                world[p.x - 1][p.y] = Tileset.WALL;
                isOccupied[p.x - 1][p.y] = -1;
            }
            // right side
            if (p.x < width - 1 && isOccupied[p.x + 1][p.y] == 0) {
                world[p.x + 1][p.y] = Tileset.WALL;
                isOccupied[p.x + 1][p.y] = -1;
            }
            // upside
            if (p.y < height - 1 && isOccupied[p.x][p.y + 1] == 0) {
                world[p.x][p.y + 1] = Tileset.WALL;
                isOccupied[p.x][p.y + 1] = -1;
            }
            // downside
            if (p.y > 0 && isOccupied[p.x][p.y - 1] == 0) {
                world[p.x][p.y - 1] = Tileset.WALL;
                isOccupied[p.x][p.y - 1] = -1;
            }
        }

    }

    public void playerGenerator(Random r) {
        ArrayList<Position> place = new ArrayList<>();

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (isOccupied[x][y] == 1) {
                    place.add(new Position(x, y));
                }
            }
        }
        playerPosition = place.get(r.nextInt(place.size()));
        world[playerPosition.x][playerPosition.y] = Tileset.PLAYER;
        isOccupied[playerPosition.x][playerPosition.y] = 2;
    }

    public TETile[][] getWorld() {
        return world;
    }
}
