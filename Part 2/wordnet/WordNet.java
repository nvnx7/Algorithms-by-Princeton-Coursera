import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private final ST<Integer, String> synsetTable;
    private final ST<String, Bag<Integer>> wordTable;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        synsetTable = new ST<>();
        wordTable = new ST<>();

        In synsetIn = new In(synsets);
        while (synsetIn.hasNextLine()) {
            String line = synsetIn.readLine();

            int id = Integer.parseInt(line.split(",")[0]);
            String synset = line.split(",")[1];

            synsetTable.put(id, synset);

            for (String word : synset.split(" ")) {
                if (wordTable.contains(word)) {
                    wordTable.get(word).add(id);
                }
                else {
                    Bag<Integer> idsBag = new Bag<>();
                    idsBag.add(id);
                    wordTable.put(word, idsBag);
                }
            }
        }

        Digraph wordNetDigraph = new Digraph(synsetTable.size());

        In hypernymIn = new In(hypernyms);
        while (hypernymIn.hasNextLine()) {
            String line = hypernymIn.readLine();

            String[] hypernymArr = line.split(",");
            int synsetId = Integer.parseInt(hypernymArr[0]);

            for (int i = 1; i < hypernymArr.length; i++) {
                wordNetDigraph.addEdge(synsetId, Integer.parseInt(hypernymArr[i]));
            }
        }

        DirectedCycle directedCycle = new DirectedCycle(wordNetDigraph);
        if (directedCycle.hasCycle()) throw new IllegalArgumentException("Not a rooted DAG");

        int numRoot = 0;
        for (int i = 0; i < wordNetDigraph.V(); i++) {
            if (!wordNetDigraph.adj(i).iterator().hasNext()) {
                numRoot++;
            }

            if (numRoot > 1) {
                throw new IllegalArgumentException("Multiple roots");
            }
        }

        sap = new SAP(wordNetDigraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordTable.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();

        return wordTable.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        int dist = sap.length(wordTable.get(nounA), wordTable.get(nounB));
        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        int ancestorId = sap.ancestor(wordTable.get(nounA), wordTable.get(nounB));

        return synsetTable.get(ancestorId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String s = "21,45";
        String[] a = s.split(",", 2);
        String b = s.split(",", 2)[1];
        for (String str : a) {
            StdOut.println(str);
        }
    }
}
