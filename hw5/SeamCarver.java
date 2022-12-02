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

    }

    /** calculate the energy of the pixel at column i and row j */
    private double calEnergy(int i, int j) {
        return calEnergyX(i, j) + calEnergyY(i, j);
    }

    private double calEnergyX(int i, int j) {
        int left = (i - 1 + width) % width;
        int right = (i + 1) % width;
        Color colorLeft = picture.get(left, j);
        Color colorRight = picture.get(right, j);
        double redX = Math.pow((colorLeft.getRed() - colorRight.getRed()), 2);
        double greenX = Math.pow((colorLeft.getGreen() - colorRight.getGreen()), 2);
        double blueX = Math.pow((colorLeft.getBlue() - colorRight.getBlue()), 2);
        return redX + greenX + blueX;
    }

    private double calEnergyY(int i, int j) {
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
        /* Initialize the values for the first column/row depending on the orientation. */
        for (int i = 0; i < height; i++) {
            energyM[0][i] = energy[0][i];
        }
        /* For the other columns/rows, use the expression above. */
        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double[][] neighbors = touchingPixelsEnergy(i, j);
                int minIndex = minEnergy(neighbors[0]);
                energyM[i][j] = energy[i][j] + neighbors[0][minIndex];
            }
        }
    }

    /** current picture */
    public Picture picture() {
        return new Picture(picture);
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
        calEnergyM();
        return findSeam();
    }

    private double[][] transpose() {
        double[][] transposed = new double[height][width];
        for (int row = 0; row < height; row += 1) {
            for (int col = 0; col < width; col += 1) {
                transposed[row][col] = energy[col][row];
            }
        }
        width = transposed.length;
        height = transposed[0].length;
        return transposed;
    }

    /** sequence if indices for vertical seam */
    public int[] findVerticalSeam() {
        energy = transpose();
        int[] seam = findHorizontalSeam();
        energy = transpose();
        return seam;
    }

    private double[][] touchingPixelsEnergy(int c, int r) {
        int topRow = r - 1, midRow = r, btmRow = r + 1;
        double mid = energyM[c - 1][midRow];
        if (height == 1) {
            return new double[][]{{mid}, {midRow}};
        }
        if (height == 2) {
            if (r == 0) {
                return new double[][]{{mid, energyM[c - 1][btmRow]}, {midRow, btmRow}};
            } else {
                return new double[][]{{energyM[c - 1][topRow], mid}, {topRow, midRow}};
            }
        }
        if (r != 0 && r != height - 1) {
            return new double[][]{
                    {energyM[c - 1][topRow], mid, energyM[c - 1][btmRow]},
                    {topRow, midRow, btmRow}};
        } else if (r == 0) {
            return new double[][]{{mid, energyM[c - 1][btmRow]}, {midRow, btmRow}};
        } else {
            return new double[][]{{energyM[c - 1][topRow], mid}, {topRow, midRow}};
        }
    }

    /** Return the index of the minimum energy among values. */
    private int minEnergy(double[] values) {
        double minEnergy = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < minEnergy) {
                minEnergy = values[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private int[] findSeam() {
        int[] seam = new int[width];
        int last = minEnergy(energyM[width - 1]);
        for (int i = width - 1; i > 0; i--) {
            seam[i] = last;
            int temp = minEnergy(touchingPixelsEnergy(i, last)[0]);
            last = (int) touchingPixelsEnergy(i, last)[1][temp];
        }
        seam[0] = last;
        return seam;
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
