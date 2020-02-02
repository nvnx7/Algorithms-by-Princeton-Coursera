import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private Digraph wordNetDigraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        wordNetDigraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= wordNetDigraph.V()) throw new IllegalArgumentException();
        if (w < 0 || w >= wordNetDigraph.V()) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(wordNetDigraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(wordNetDigraph, w);

        int ancestor = commonAncestor(bfsV, bfsW);
        if (ancestor == -1) return -1;

        int dist = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);

        return dist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= wordNetDigraph.V()) throw new IllegalArgumentException();
        if (w < 0 || w >= wordNetDigraph.V()) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(wordNetDigraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(wordNetDigraph, w);

        int ancestor = commonAncestor(bfsV, bfsW);

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(wordNetDigraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(wordNetDigraph, w);

        int ancestor = commonAncestor(bfsV, bfsW);
        if (ancestor == -1) return -1;

        int dist = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);

        return dist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(wordNetDigraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(wordNetDigraph, w);

        int ancestor = commonAncestor(bfsV, bfsW);

        return ancestor;
    }

    private int commonAncestor(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int shortestDist = Integer.MAX_VALUE;
        int common = -1;

        for (int i = 0; i < wordNetDigraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < shortestDist) {
                    shortestDist = dist;
                    common = i;
                }
            }
        }

        return common;
    }

    private void validateIterable(Iterable<Integer> iterable) {
        if (iterable == null) throw new IllegalArgumentException();

        for (Integer integer : iterable) {
            if (integer == null) throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
