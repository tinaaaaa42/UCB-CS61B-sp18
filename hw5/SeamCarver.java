import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double[][] energy;
    private double[][] energyM;
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture().height();
        energy = new double[width][height];

        for (int col = 0; col < width; col += 1) {
            for (int row = 0; row < height; row += 1) {
                energy[col][row] = calEnergy(col, row);
            }
        }

        calEnergyM();
    }

    private double min(double d1, double d2) {
        if (d1 > d2) {
            return d2;
        }
        return d1;
    }

    /** calculate the energy of the pixel at column i and row j */
    public double calEnergy(int i, int j) {
        return calEnergyX(i, j) + calEnergyY(i, j);
    }

    public double calEnergyX(int i, int j) {
        int left = (i - 1 + width) % width;
        int right = (i + 1) % width;
        Color colorLeft = picture.get(left, j);
        Color colorRight = picture.get(right, j);
        double redX = Math.pow((colorLeft.getRed() - colorRight.getRed()), 2);
        double greenX = Math.pow((colorLeft.getGreen() - colorRight.getGreen()), 2);
        double blueX = Math.pow((colorLeft.getBlue() - colorRight.getBlue()), 2);
        return redX + greenX + blueX;
    }

    public double calEnergyY(int i, int j) {
        int up = (j - 1 + height) % height;
        int down = (j + 1) % height;
        Color colorUp = picture.get(i, up);
        Color colorDown = picture.get(i, down);
        double redY = Math.pow((colorUp.getRed() - colorDown.getRed()), 2);
        double greenY = Math.pow((colorUp.getGreen() - colorDown.getGreen()), 2);
        double blueY = Math.pow((colorUp.getBlue() - colorDown.getBlue()), 2);
        return redY + greenY + blueY;
    }

    private void calEnergyM() {
        energyM = new double[width][height];
        for (int col = 0; col < width; col += 1) {
            energyM[col][0] = energyM[col][0];
        }
        for (int row = 1; row < height; row += 1) {
            if (width == 1) {
                energyM[0][row] = energyM[0][row - 1] + energy[0][row];
            } else if (width == 2) {
                energyM[0][row] = min(energyM[0][row - 1], energyM[1][row - 1]) + energy[0][row];
                energyM[1][row] = min(energyM[0][row - 1], energyM[1][row - 1]) + energy[1][row];
            } else if (width >= 3) {
                energyM[0][row] = min(energyM[0][row - 1], energyM[1][row - 1]) + energy[0][row];
                energyM[width - 1][row] = min(energyM[width - 2][row - 1],
                        energyM[width - 1][row - 1]) + energy[width - 1][row];
                for (int col = 1; col < width - 1; col += 1) {
                    energyM[col][row] = min(energyM[col - 1][row - 1],
                            min(energyM[col][row - 1], energyM[col + 1][row - 1]))
                            + energy[col][row];
                }
            }
        }
    }

    /** current picture */
    public Picture picture() {
        return picture;
    }

    /** width of current picture */
    public int width() {
        return width;
    }

    /** height of the current picture */
    public int height() {
        return height;
    }

    /** energy of the pixel at column x and row y */
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return energy[x][y];
    }

    /** sequence if indices for horizontal seam */
    public int[] findHorizontalSeam() {
        energy = transpose();
        int[] seam = findVerticalSeam();
        energy = transpose();
        return seam;
    }

    private double[][] transpose() {
        double[][] transposed = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                transposed[row][col] = energy[col][row];
            }
        }
        width = transposed.length;
        height = transposed[0].length;
        return transposed;
    }

    /** sequence if indices for vertical seam */
    public int[] findVerticalSeam() {
        calEnergyM();
        int[][] roads = new int[width][height];
        int minTotalEnergy = 0;
        int currentTotalEnergy = 0;
        int index = 0;
        for (int i = 0; i < width; i += 1) {
            roads[i][0] = i;
        }
        if (width == 1) {
            for (int i = 0; i < height; i += 1) {
                roads[0][i] = 0;
            }
        } else if (width == 2) {
            for (int r = 0; r < width; r += 1) {
                currentTotalEnergy += energyM[r][0];
                for (int floor = 1; floor < height; floor += 1) {
                    if (min(energyM[0][floor], energyM[1][floor]) == energyM[0][floor]) {
                        roads[r][floor] = 0;
                        currentTotalEnergy += energyM[0][floor];
                    } else {
                        roads[r][floor] = 1;
                        currentTotalEnergy += energyM[1][floor];
                    }
                }
                if (currentTotalEnergy < minTotalEnergy) {
                    index = r;
                    minTotalEnergy = currentTotalEnergy;
                }
            }
        } else if (width >= 3) {
            for (int r = 0; r < width; r += 1) {
                currentTotalEnergy += energyM[r][0];
                for (int floor = 1; floor < height; floor += 1) {
                    int last = roads[r][floor - 1];
                    if (last == 0) {
                        if (min(energyM[0][floor], energyM[1][floor]) == energyM[0][floor]) {
                            roads[r][floor] = 0;
                            currentTotalEnergy += energyM[0][floor];
                        } else {
                            roads[r][floor] = 1;
                            currentTotalEnergy += energyM[1][floor];
                        }
                    } else if (last == width - 1) {
                        if (min(energyM[width - 1][floor], energyM[width - 2][floor])
                                == energyM[width - 1][floor]) {
                            roads[r][floor] = width - 1;
                            currentTotalEnergy += energyM[width - 1][floor];
                        } else {
                            roads[r][floor] = width - 2;
                            currentTotalEnergy += energyM[width - 2][floor];
                        }
                    } else {
                        double nextMin = min(energyM[last - 1][floor],
                                min(energyM[last][floor], energyM[last + 1][floor]));
                        if (nextMin == energyM[last - 1][floor]) {
                            roads[r][floor] = last - 1;
                            currentTotalEnergy += energyM[last - 1][floor];
                        } else if (nextMin == energyM[last][floor]) {
                            roads[r][floor] = last;
                            currentTotalEnergy += energyM[last][floor];
                        } else {
                            roads[r][floor] = last + 1;
                            currentTotalEnergy += energyM[last + 1][floor];
                        }
                    }
                }
                if (currentTotalEnergy < minTotalEnergy) {
                    index = r;
                    minTotalEnergy = currentTotalEnergy;
                }
            }
        }

        return roads[index];
    }

    /** remove horizontal seam from picture */
    public void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(picture, seam);
        // update the height
        height = picture.height();
    }

    /** remove vertical seam from picture */
    public void removeVerticalSeam(int[] seam) {
        SeamRemover.removeVerticalSeam(picture, seam);
        // update the width
        width = picture.width();
    }
}
