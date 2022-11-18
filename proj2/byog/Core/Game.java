package byog.Core;

import edu.princeton.cs.introcs.StdDraw;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.awt.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static int seedLength = 0;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        keyBoardGameGenerator keyGame = new keyBoardGameGenerator(WIDTH, HEIGHT);
        keyGame.drawInitialize();
        keyGame.menuStarter();
        char choice = keyGame.menuInput();
        keyGame.choiceMaker(choice);
    }

    /** get the seed from the input string */
    public static long getSeed(String input) {
        long seed = 0;
        for (int i = 1; i < input.length(); i += 1) {
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                seed = seed * 10 + input.charAt(i) - '0';
                seedLength += 1;
            } else {
                break;
            }
        }
        return seed;
    }

    public void inputMove(keyBoardGameGenerator key, String s) {
        for (int i = 0; i < s.length(); i += 1) {
            switch (s.charAt(i)) {
                case 'w':
                case 'W':
                case 's':
                case 'S':
                case 'a':
                case 'A':
                case 'd':
                case 'D':
                    key.move(s.charAt(i));
                    break;
                case ':':
                    if (i < s.length() && s.charAt(i + 1) == 'Q') {
                        key.saveWorld(key.wg);
                        return;
                    }
                    break;
                default:
            }
        }
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
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        keyBoardGameGenerator keyGame = new keyBoardGameGenerator(WIDTH, HEIGHT);
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            long seed = getSeed(input);
            keyGame.wg = new WorldGenerator(WIDTH, HEIGHT, seed);
            inputMove(keyGame, input.substring(seedLength + 1));
        } else if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            keyGame.wg = keyGame.loadWorld();
            inputMove(keyGame, input.substring(1));
        } else if (input.charAt(0) == 'q' || input.charAt(0) == 'Q') {
            return keyGame.wg.world;
        }

        return keyGame.wg.world;
    }

    @Test
    public void testGetSeed() {
        assertEquals(123, getSeed("n123"));
        assertEquals(123, getSeed("n123ss"));
        assertEquals(1,getSeed("n1ss321"));
    }
}
