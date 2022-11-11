package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static class Position {
        private int x;
        private int y;

        public Position(int Px, int Py) {
            x = Px;
            y = Py;
        }
    }

    /** example: s = 3
     *    ###
     *   #####
     *  #######
     *  #######  <- i = 2, returns 7
     *   #####
     *    ###
     *  @param s the size of the hex
     *  @param i the current row (start in 0 from bottom to top)
     *  @return  the width of the ith row
     */
    private static int hexRowWidth(int s, int i) {
        if (i < s) {
            return s + 2 * i;
        }
        return hexRowWidth(s, 2 * s - 1 - i);
    }

    /** example: s = 3
     *    ###
     *   #####
     *  #######
     *  #######  <- i = 2, returns -2
     *   #####
     *    ###
     *  @param s the size of the hex
     *  @param i the current row (start in 0 from bottom to top)
     *  @return  the xOffset in the ith row(compared to row 0)
     */
    private static int hexRowOffset(int s, int i) {
        if (i < s) {
            return -i;
        }
        return hexRowOffset(s, 2 * s - 1 - i);
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param rowStartPoint the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position rowStartPoint, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xPoint = rowStartPoint.x + xi;
            int yPoint = rowStartPoint.y;
            world[xPoint][yPoint] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;

            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);

        }
    }

    /** get the position of the ith hex */
    public static Position getPosition(int s, int i) {
        Position p = new Position(0, 0);
        if (i <= 2) {
            p.x = s - 1;
            p.y = 2 * (i + 1) * s;
        } else if (i <= 5) {
            p.x = getPosition(s, i - 3).x + 2 * s - 1;
            p.y = getPosition(s, i - 3).y - s;
        } else if (i == 6) {
            p.x = getPosition(s, 2).x + 2 * s - 1;
            p.y = getPosition(s, 2).y + s;
        } else if (i <= 10) {
            p.x = getPosition(s, i - 4).x + 2 * s - 1;
            p.y = getPosition(s, i - 4).y - s;
        } else if (i == 11) {
            p.x = getPosition(s, 6).x + 2 * s - 1;
            p.y = getPosition(s, 6).y + s;
        } else if (i <= 15) {
            p.x = getPosition(s, i - 5).x + 2 * s - 1;
            p.y = getPosition(s, i - 5).y + s;
        } else {
            p.x = getPosition(s, i - 4).x + 2 * s - 1;
            p.y = getPosition(s, i - 4).y + s;
        }

        return p;
    }

    /** returns the tile in the ith hex */
    public static TETile getTile(int i) {
        return switch (i) {
            case 0, 1, 6 -> Tileset.GRASS;
            case 2, 4, 5, 7, 8, 9, 10, 12 -> Tileset.MOUNTAIN;
            case 3, 15, 18 -> Tileset.FLOWER;
            case 11, 13, 17 -> Tileset.TREE;
            case 14, 16 -> Tileset.LOCKED_DOOR;
            default -> Tileset.NOTHING;
        };
    }
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int s = 3;
        for (int i = 0; i < 19; i += 1) {
            addHexagon(world, getPosition(s, i), s, getTile(i));
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }

    @Test
    public void testHexRowWidth() {
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(7, hexRowWidth(3, 3));
        assertEquals(3, hexRowWidth(3, 0));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-2,hexRowOffset(3, 3));
        assertEquals(0, hexRowOffset(3, 5));
    }
}
