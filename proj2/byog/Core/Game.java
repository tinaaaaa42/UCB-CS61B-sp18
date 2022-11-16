package byog.Core;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /** get the seed from the input string */
    public static int getSeed(String input) {
        int seed = 0;
        for (int i = 1; i < input.length(); i += 1) {
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                seed = seed * 10 + input.charAt(i) - '0';
            } else {
                break;
            }
        }
        return seed;
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            int seed = getSeed(input);
            WorldGenerator w = new WorldGenerator(WIDTH, HEIGHT, seed);
            TETile[][] finalWorldFrame = w.getWorld();
            return finalWorldFrame;
        }

        return null;
    }

    @Test
    public void testGetSeed() {
        assertEquals(123, getSeed("n123"));
        assertEquals(123, getSeed("n123ss"));
        assertEquals(1,getSeed("n1ss321"));
    }
}
