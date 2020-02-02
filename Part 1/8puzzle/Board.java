/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int n;
    private int[][] tiles;
    private int hammingDist;
    private int manhattanDist;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = tiles;
        hammingDist = 0;
        manhattanDist = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int elem = tiles[i][j];

                if (elem == 0) continue;

                int goalElemCol = (elem % n) - 1;
                if (goalElemCol == -1) goalElemCol = n - 1;

                int goalElemRow = (elem / n);
                if (elem % n == 0) goalElemRow--;

                manhattanDist += Math.abs(i - goalElemRow) + Math.abs(j - goalElemCol);

                if (tiles[i][j] != (n * i) + (j + 1)) hammingDist++;
            }
        }
    }

    // string representation of this board
    public String toString() {
        String boardStr = String.valueOf(n);
        boardStr += "\n";

        for (int[] row : tiles) {
            for (int elem : row) {
                boardStr += " " + String.valueOf(elem);
            }
            boardStr += "\n";
        }

        return boardStr;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;

        Board that = (Board) y;
        if (this.n != that.n) return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int row = 0; // row number of blank space
        int col = 0; // column number of blank space

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        int prevRow = row - 1;
        int prevCol = col - 1;
        int nextRow = row + 1;
        int nextCol = col + 1;

        if (prevRow >= 0) {
            int[][] neighbor1 = copyTiles(tiles);

            neighbor1[row][col] = neighbor1[prevRow][col];
            neighbor1[prevRow][col] = 0;

            neighbors.push(new Board(neighbor1));
        }


        if (nextCol <= n - 1) {
            int[][] neighbor2 = copyTiles(tiles);

            neighbor2[row][col] = neighbor2[row][nextCol];
            neighbor2[row][nextCol] = 0;

            neighbors.push(new Board(neighbor2));
        }

        if (nextRow <= n - 1) {
            int[][] neighbor3 = copyTiles(tiles);

            neighbor3[row][col] = neighbor3[nextRow][col];
            neighbor3[nextRow][col] = 0;

            neighbors.push(new Board(neighbor3));
        }

        if (prevCol >= 0) {
            int[][] neighbor4 = copyTiles(tiles);

            neighbor4[row][col] = neighbor4[row][prevCol];
            neighbor4[row][prevCol] = 0;

            neighbors.push(new Board(neighbor4));
        }

        return neighbors;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copyTiles(tiles);

        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            twinTiles[0][0] = tiles[0][1];
            twinTiles[0][1] = tiles[0][0];
        }
        else {
            twinTiles[1][0] = tiles[1][1];
            twinTiles[1][1] = tiles[1][0];
        }

        return new Board(twinTiles);
    }

    private int[][] copyTiles(int[][] tiles) {

        int[][] copy = new int[tiles.length][tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }

        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] boardItems = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };

        Board board = new Board(boardItems);

        StdOut.println(board);

        StdOut.println("Neighbors:");
        for (Board b : board.neighbors()) {
            StdOut.println(b);
        }

        StdOut.println("Twin:");
        StdOut.println(board.twin());

        StdOut.println("Hemming distance: " + board.hamming());
        StdOut.println("Manhattan distance: " + board.manhattan());
        StdOut.println("Is Goal: " + board.isGoal());
    }
}
