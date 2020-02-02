import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Node solutionNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Board initialTwin = initial.twin();

        MinPQ<Node> minPQ = new MinPQ<>();
        MinPQ<Node> minPQTwin = new MinPQ<>();

        minPQ.insert(new Node(initial, null));
        minPQTwin.insert(new Node(initialTwin, null));

        while (true) {
            Node deqNode = minPQ.delMin();
            Node deqNodeTwin = minPQTwin.delMin();

            if (deqNode.board.isGoal()) {
                solutionNode = deqNode;
                break;
            }
            else if (deqNodeTwin.board.isGoal()) {
                solutionNode = null;
                break;
            }

            deqNode.insertNeighbors(minPQ);
            deqNodeTwin.insertNeighbors(minPQTwin);
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) return solutionNode.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> solutionSeq = new Stack<>();
            Node next = solutionNode;
            while (next != null) {
                solutionSeq.push(next.board);
                next = next.prevNode;
            }

            return solutionSeq;
        }
        else {
            return null;
        }
    }

    private class Node implements Comparable<Node> {

        private final Board board;
        private final int moves;
        private final Node prevNode;
        private final int priority;

        public Node(Board board, Node prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            this.moves = this.prevNode == null ? 0 : prevNode.moves + 1;
            this.priority = board.manhattan() + moves;
        }

        public void insertNeighbors(MinPQ<Node> minPQ) {
            for (Board b : board.neighbors()) {
                if (prevNode != null && b.equals(prevNode.board)) continue;

                minPQ.insert(new Node(b, this));
            }
        }

        public int compareTo(Node other) {
            return this.priority - other.priority;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = {
                { 0, 1, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
