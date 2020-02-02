import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();

        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1) throw new IllegalArgumentException();
        if (y < 0 || y > height() - 1) throw new IllegalArgumentException();

        if ((x == 0 || x == width() - 1)
                || (y == 0 || y == height() - 1)) {
            return 1000;
        }

        Color prevX = picture.get(x - 1, y);
        Color nextX = picture.get(x + 1, y);
        Color prevY = picture.get(x, y - 1);
        Color nextY = picture.get(x, y + 1);

        int xGrad = gradientSquare(prevX, nextX);
        int yGrad = gradientSquare(prevY, nextY);

        return Math.sqrt(xGrad + yGrad);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
        int[] minEnergySeam = findVerticalSeam();
        transposePicture();

        return minEnergySeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energyArr = new double[width()][height()];

        for (int x = 0; x < width(); x++)
            for (int y = 0; y < height(); y++)
                energyArr[x][y] = energy(x, y);

        int[] edgeTo = new int[width() * height() + 1];
        double[] distTo = new double[width() * height() + 1];

        for (int i = 0; i < distTo.length; i++)
            if (i < width()) {
                distTo[i] = energyArr[i][0];
                edgeTo[i] = width() * height();
            }
            else distTo[i] = Double.POSITIVE_INFINITY;
        distTo[width() * height()] = 0;

        for (int i = 0; i <= width() * (height() - 1); i++) {

            for (int j : getAdj(i)) {
                if (distTo[j] > distTo[i] + energyArr[j % width()][j / width()]) {
                    distTo[j] = distTo[i] + energyArr[j % width()][j / width()];
                    edgeTo[j] = i;
                }
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int startIdx = width() * (height() - 1);
        int from = startIdx;
        for (int i = startIdx; i < distTo.length - 1; i++) {
            if (distTo[i] < minEnergy) {
                minEnergy = distTo[i];
                from = i;
            }
        }

        int[] minEnergySeam = new int[height()];
        for (int i = minEnergySeam.length - 1; i > -1; i--) {
            minEnergySeam[i] = from % width();
            from = edgeTo[from];
        }

        return minEnergySeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height() <= 1) throw new IllegalArgumentException();

        transposePicture();
        removeVerticalSeam(seam);
        transposePicture();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width() <= 1) throw new IllegalArgumentException();

        Picture copy = picture();
        picture = new Picture(copy.width() - 1, copy.height());
        for (int i = 0; i < copy.height(); i++) {
            int x = 0;
            for (int j = 0; j < copy.width(); j++) {
                if (j == seam[i]) continue;
                int color = copy.getRGB(j, i);
                picture.setRGB(x, i, color);
                x++;
            }
        }
    }

    private int gradientSquare(Color c1, Color c2) {
        int r = c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();

        return (r * r) + (g * g) + (b * b);
    }

    private void transposePicture() {
        Picture copy = picture();
        picture = new Picture(height(), width());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                int color = copy.getRGB(j, i);
                picture.setRGB(i, j, color);
            }
        }
    }

    private Iterable<Integer> getAdj(int i) {
        Bag<Integer> adj = new Bag<>();

        int x = i % width();
        int y = i / width();

        if (y == height() - 1) return adj;

        if (x > 0) adj.add(getPointByPos(x - 1, y + 1));
        if (x < width() - 1) adj.add(getPointByPos(x + 1, y + 1));
        adj.add(getPointByPos(x, y + 1));

        return adj;
    }

    private int getPointByPos(int x, int y) {
        int col = x + 1;
        int row = y + 1;

        return ((row - 1) * width() + col - 1);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        // picture.show();
        SeamCarver sc = new SeamCarver(picture);
        int[] seam = new int[] { 3, 4, 3, 2, 1 };

        // StdOut.println(picture);
        // StdOut.println(" ");
        // sc.removeVerticalSeam(seam);
        // StdOut.println(sc.picture);

        // int[] seam = sc.findVerticalSeam();
        // for (int i : sc.getAdj(62)) StdOut.println(i);
        // StdOut.println(sc.getPointByPos(0, 1));

        // StdOut.println(sc.energy(1, 1));
    }
}
