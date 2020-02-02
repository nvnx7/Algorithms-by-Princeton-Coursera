import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] asciiChars = asciiCharArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int idx = indexOf(c, asciiChars);
            moveToFront(idx, asciiChars);
            BinaryStdOut.write(idx, 8);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] asciiChars = asciiCharArray();

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            int c = asciiChars[i];
            moveToFront(i, asciiChars);
            BinaryStdOut.write(c, 8);
        }

        BinaryStdOut.close();
    }

    private static char[] asciiCharArray() {
        char[] ascii = new char[256];

        for (int i = 0; i < ascii.length; i++)
            ascii[i] = (char) i;

        return ascii;
    }

    private static void moveToFront(int idx, char[] arr) {
        char c = arr[idx];
        for (int i = idx; i > 0; i--)
            arr[i] = arr[i - 1];

        arr[0] = c;
    }

    private static int indexOf(char c, char[] arr) {
        int idx = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == c) {
                idx = i;
            }
        }

        return idx;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
