import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        int first = 0;
        char[] t = new char[s.length()];

        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < suffixArray.length(); i++) {
            int idx = suffixArray.index(i);

            int lastIdx = (idx - 1) < 0 ? (s.length() - 1) : (idx - 1);
            t[i] = s.charAt(lastIdx);
            if (idx == 0) first = i;
        }

        BinaryStdOut.write(first);
        for (char c : t) BinaryStdOut.write(c, 8);

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] t = s.toCharArray();
        ST<Character, Queue<Integer>> cST = new ST<>();
        char[] tSorted = t.clone();
        Arrays.sort(tSorted);

        for (int i = 0; i < t.length; i++) {
            if (!cST.contains(t[i]))
                cST.put(t[i], new Queue<>());
            cST.get(t[i]).enqueue(i);
        }

        int[] next = new int[t.length];
        for (int i = 0; i < next.length; i++) {
            char c = tSorted[i];
            next[i] = cST.get(c).dequeue();
        }

        int j = first;
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(tSorted[j], 8);
            j = next[j];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }
}
