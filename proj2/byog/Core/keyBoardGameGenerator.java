package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class keyBoardGameGenerator implements Serializable {
    Random r;
    private final int width;
    private final int height;
    private long seed;
    private TERenderer ter = new TERenderer();
    public WorldGenerator wg;
    private WorldGenerator.Position mousePosition = new WorldGenerator.Position(0, 0);

    public keyBoardGameGenerator(int w, int h) {
        width = w;
        height = h;
    }

    public void drawInitialize() {
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();
    }
    public void menuStarter() {
        // show the start menu
        drawFrame(width / 2, height / 2 - 5, "New Game(N)");
        drawFrame(width / 2, height / 2, "Load a game(L)");
        drawFrame(width / 2, height / 2 + 5, "Quit(Q)");
    }
    public void drawFrame(int x, int y, String s) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }

    /** get a menu input*/
    public char menuInput() {
        StringBuilder input = new StringBuilder();

        while (input.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input.append(key);
        }
        StdDraw.pause(500);
        return input.toString().charAt(0);
    }

    public void choiceMaker(char choice) {
        if (choice == 'N' || choice == 'n') {
            // update
            StdDraw.clear();
            StdDraw.clear(StdDraw.BLACK);
            drawFrame(width / 2, height / 2, "Enter a seed. End in 'S'.");

            // get a seed ended with a 's'
            seed = seedInput();
            r = new Random(seed);

            // show the world
            wg = new WorldGenerator(width, height - 1, seed);

            ter.initialize(width, height);
            StdDraw.clear();
            StdDraw.clear(StdDraw.BLACK);
            showWorld(wg.world);
            playTheGame();
        } else if (choice == 'L' || choice == 'l') {
            StdDraw.clear();
            StdDraw.clear(StdDraw.BLACK);
            wg = loadWorld();
            showWorld(wg.world);
            playTheGame();
        } else if (choice == 'Q' || choice == 'q') {
            System.exit(0);
        }
    }
    /** get a seed input */
    public long seedInput() {
        long seed = 0;
        StringBuilder input = new StringBuilder();
        while (input.length() < 21) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key >= '0' && key <= '9') {
                seed = seed * 10 + key - '0';
                StdDraw.clear();
                StdDraw.clear(StdDraw.BLACK);
                drawFrame(width / 2, height / 2, String.valueOf(seed));
            } else if (key == 's' || key == 'S') {
                break;
            }
        }
        return seed;
    }


    public void showWorld(TETile[][] world) {
        ter.renderFrame(world);
    }

    public boolean isQuit() {
        StringBuilder input = new StringBuilder();

        while (input.length() < 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input.append(key);
        }
        if (input.toString().charAt(0) == 'Q') {
            return true;
        } else {
            return false;
        }
    }

    public WorldGenerator.Position mouseCatcher() {
        return new WorldGenerator.Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
    }

    public void keyListener() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            switch (c) {
                case ':':
                    if (isQuit()) {
                        saveWorld(wg);
                        System.exit(0);
                        break;
                    }
                    break;
                case 'w':
                    move('w');
                    showWorld(wg.world);
                    break;
                case 's':
                    move('s');
                    showWorld(wg.world);
                    break;
                case 'a':
                    move('a');
                    showWorld(wg.world);
                    break;
                case 'd':
                    move('d');
                    showWorld(wg.world);
                    break;
                default:
            }
        }
    }

    public String currentTile(WorldGenerator.Position p) {
        if (p.x >= width || p.y >= height - 1 || wg.world[p.x][p.y].equals(Tileset.NOTHING)) {
            return " ";
        } else {
            return wg.world[p.x][p.y].description();
        }
    }
    public void playTheGame() {
        while (true) {
            if (!mouseCatcher().equal(mousePosition)) {
                mousePosition.x = (int) StdDraw.mouseX();
                mousePosition.y = (int) StdDraw.mouseY();
                StdDraw.clear();
                StdDraw.clear(StdDraw.BLACK);
                drawFrame(2, height - 1, currentTile(mousePosition));
                showWorld(wg.world);
            }

            keyListener();
        }
    }

    /** move */
    public void move(char c) {
        switch (c) {
            case 'w':
                if (wg.playerPosition.y < height - 1 &&
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y + 1] == 1) {
                    wg.world[wg.playerPosition.x][wg.playerPosition.y] = Tileset.FLOOR;
                    wg.world[wg.playerPosition.x][wg.playerPosition.y + 1] = Tileset.PLAYER;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y] = 1;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y + 1] = 2;
                    wg.playerPosition.y += 1;
                }
                break;
            case 's':
                if (wg.playerPosition.y > 0 &&
                        wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y - 1] == 1) {
                    wg.world[wg.playerPosition.x][wg.playerPosition.y] = Tileset.FLOOR;
                    wg.world[wg.playerPosition.x][wg.playerPosition.y - 1] = Tileset.PLAYER;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y] = 1;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y - 1] = 2;
                    wg.playerPosition.y -= 1;
                }
                break;
            case 'a':
                if (wg.playerPosition.x > 0 &&
                        wg.isOccupied[wg.playerPosition.x - 1][wg.playerPosition.y] == 1) {
                    wg.world[wg.playerPosition.x][wg.playerPosition.y] = Tileset.FLOOR;
                    wg.world[wg.playerPosition.x - 1][wg.playerPosition.y] = Tileset.PLAYER;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y] = 1;
                    wg.isOccupied[wg.playerPosition.x - 1][wg.playerPosition.y] = 2;
                    wg.playerPosition.x -= 1;
                }
                break;
            case 'd':
                if (wg.playerPosition.x < width - 1 &&
                        wg.isOccupied[wg.playerPosition.x + 1][wg.playerPosition.y] == 1) {
                    wg.world[wg.playerPosition.x][wg.playerPosition.y] = Tileset.FLOOR;
                    wg.world[wg.playerPosition.x + 1][wg.playerPosition.y] = Tileset.PLAYER;
                    wg.isOccupied[wg.playerPosition.x][wg.playerPosition.y] = 1;
                    wg.isOccupied[wg.playerPosition.x + 1][wg.playerPosition.y] = 2;
                    wg.playerPosition.x += 1;
                }
                break;
        }
    }
    public void saveWorld(WorldGenerator wg) {
        String pathName = "./world.ser";
        File f = new File(pathName);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(wg);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public WorldGenerator loadWorld() {
        String pathName = "./world.ser";
        File f = new File(pathName);
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                WorldGenerator loadWorld = (WorldGenerator) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        /* In the case no World has been saved yet, we return a new one. */
        return new WorldGenerator(width, height, seed);
    }
}
