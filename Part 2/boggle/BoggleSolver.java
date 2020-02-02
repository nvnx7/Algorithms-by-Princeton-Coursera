import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

    private final TST<Integer> dictionarySet;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictionarySet = new TST<>();

        int i = 0;
        for (String s : dictionary) {
            dictionarySet.put(s, i);
            i++;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows();
        int n = board.cols();

        SET<String> enumStrings = new SET<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boolean[][] marked = new boolean[m][n];
                dfs(i, j, board, enumStrings, "", marked);
            }
        }

        return enumStrings;
    }

    private void dfs(int i, int j, BoggleBoard board, SET<String> enumStrings, String prefix,
                     boolean[][] marked) {

        if (!marked[i][j]) {

            String letter = board.getLetter(i, j) == 'Q' ? "QU" :
                            String.valueOf(board.getLetter(i, j));

            prefix = prefix + letter;
            if (prefix.length() >= 3 && dictionarySet.contains(prefix) && !enumStrings
                    .contains(prefix)) {
                enumStrings.add(prefix);
            }

            if (!dictionarySet.keysWithPrefix(prefix).iterator()
                              .hasNext()) {
                marked[i][j] = false;
                return;
            }

            marked[i][j] = true;

            if (i != 0) {
                dfs(i - 1, j, board, enumStrings, prefix, marked);

                if (j != 0) {
                    dfs(i - 1, j - 1, board, enumStrings, prefix, marked);
                }

                if (j != board.cols() - 1) {
                    dfs(i - 1, j + 1, board, enumStrings, prefix, marked);
                }

            }

            if (i != board.rows() - 1) {
                dfs(i + 1, j, board, enumStrings, prefix, marked);

                if (j != 0) {
                    dfs(i + 1, j - 1, board, enumStrings, prefix, marked);
                }

                if (j != board.cols() - 1) {
                    dfs(i + 1, j + 1, board, enumStrings, prefix, marked);
                }
            }

            if (j != 0) {
                dfs(i, j - 1, board, enumStrings, prefix, marked);
            }

            if (j != board.cols() - 1) {
                dfs(i, j + 1, board, enumStrings, prefix, marked);
            }

            marked[i][j] = false;
        }

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionarySet.contains(word)) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        }
        else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        long current = System.nanoTime();

        int n = 1000;
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            BoggleBoard board = new BoggleBoard();
            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
                score += solver.scoreOf(word);
            }
            scores[i] = score;
        }

        double elapsed = (double) (System.nanoTime() - current) / 1000000000;

        for (int score : scores) StdOut.println("Score: " + score);
        StdOut.println("TIME ELAPSED (in s): " + elapsed);
    }
}
