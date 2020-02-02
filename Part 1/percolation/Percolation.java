import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n; // grid size

    // WeightedQuickUnionUF data structure representation of percolation sites
    private final WeightedQuickUnionUF uf;

    // integer value representation of sites that are open
    // 0 for blocked & 1 for open
    private final int[] openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;

        // 2 extra indices for top & bottom virtual sites (at indices n*n & n*n + 1 ie last two)
        uf = new WeightedQuickUnionUF(n * n + 2);

        for (int i = 0; i < n; i++) {
            // connect top virtual site to all sites in first row
            uf.union(n * n, i);

            // connect bottom virtual site to all sites in last row
            uf.union(n * n + 1, n * n - 1 - i);
        }

        openSites = new int[n * n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!siteExists(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid range for row or column. Valid range from 1 to " + n + "Received ("
                            + row + ", " + col + ")");
        }

        if (!isOpen(row, col)) {
            openSites[getIndexOfSite(row, col)] = 1;

            // Two sites are automatically connected if they are both open and adjacent to each other
            if (siteExists(row - 1, col) && isOpen(row - 1, col))
                connectSites(row, col, row - 1, col); // top
            if (siteExists(row, col + 1) && isOpen(row, col + 1))
                connectSites(row, col, row, col + 1); // right
            if (siteExists(row + 1, col) && isOpen(row + 1, col))
                connectSites(row, col, row + 1, col); // bottom
            if (siteExists(row, col - 1) && isOpen(row, col - 1))
                connectSites(row, col, row, col - 1); // left
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!siteExists(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid range for row or column. Valid range from 1 to " + n);
        }

        return openSites[getIndexOfSite(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!siteExists(row, col)) {
            throw new IllegalArgumentException(
                    "Invalid range for row or column. Valid range from 1 to " + n + ". Received ("
                            + row + ", " + col + ")");
        }

        // Site can't be full if it's not even open
        if (!isOpen(row, col)) return false;

        // Site must be full if in some way it's connected to top virtual site
        return uf.connected(getIndexOfSite(row, col), n * n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int sum = 0;
        for (int i = 0; i < openSites.length; i++) {
            sum += openSites[i];
        }
        return sum;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(n * n, n * n + 1);
    }

    // connect two sites using uf ds
    private void connectSites(int row1, int col1, int row2, int col2) {
        if (siteExists(row1, col1) && siteExists(row2, col2)) {
            uf.union(getIndexOfSite(row1, col1), getIndexOfSite(row2, col2));
        }
        else {
            throw new IllegalArgumentException("One of the points does not exist!");
        }
    }

    // check if site exists
    private boolean siteExists(int x, int y) {
        if ((x > n || y > n) || (x <= 0 || y <= 0)) {
            return false;
        }

        return true;
    }

    // get the site index (in it's WeightedQuickUnionUF ds) from it's 2D point (ranges from 0 to n*n - 1)
    private int getIndexOfSite(int x, int y) {
        return (n * x - (n - y) - 1);
    }

    public static void main(String[] args) {

        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);

        while (true) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            StdOut.print("open(): ");
            percolation.open(row, col);
            StdOut.println("Opened");

            StdOut.print("isOpen(): ");
            StdOut.println(percolation.isOpen(row, col));

            StdOut.print("percolates(): ");
            StdOut.println(percolation.percolates());

            StdOut.print("numberOfOpenSites(): ");
            StdOut.println(percolation.numberOfOpenSites());

            StdOut.print("isFull(): ");
            StdOut.println(percolation.isFull(row, col));
        }

    }

}
