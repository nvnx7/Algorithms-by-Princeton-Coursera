import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class CircularSuffixArray {

    private final Integer[] index;
    private final char[] charArray;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();

        index = new Integer[s.length()];
        charArray = s.toCharArray();

        for (int i = 0; i < s.length(); i++)
            index[i] = i;

        MergeX.sort(index, new SuffixComparator());
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();

        return index[i];
    }

    private class SuffixComparator implements Comparator<Integer> {

        public int compare(Integer int1, Integer int2) {
            int len = charArray.length;
            int i = 0;
            int i1 = int1;
            int i2 = int2;
            while (i < len) {
                char c1 = charArray[i1];
                char c2 = charArray[i2];

                if (c1 < c2) return -1;
                else if (c2 < c1) return 1;

                i1 = (i1 == len - 1) ? 0 : (i1 + 1);
                i2 = (i2 == len - 1) ? 0 : (i2 + 1);

                i++;
            }
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s1 = "ABRACADABRA!";
        String s2 = "BRACADABRA!A";

        CircularSuffixArray csa = new CircularSuffixArray(s1);
        for (int i = 0; i < s1.length(); i++) {
            StdOut.println(csa.index(i));
        }
    }
}
