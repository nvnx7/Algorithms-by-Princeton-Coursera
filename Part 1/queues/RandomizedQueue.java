import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int n;
    // private int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        // last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Null received");

        if (n == q.length) resize(2 * n);
        q[n] = item;

        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int rIndex = StdRandom.uniform(n);
        Item rItem = q[rIndex];

        q[rIndex] = q[n - 1];
        q[n - 1] = null;
        n--;

        if (n > 0 && n == q.length / 4) resize(q.length / 2);

        return rItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return q[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private int current = 0;
        private Item[] shuffledQ;

        public ListIterator() {
            shuffledQ = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                shuffledQ[i] = q[i];
            }

            StdRandom.shuffle(shuffledQ);
        }

        public boolean hasNext() {
            return current < shuffledQ.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = shuffledQ[current];
            current++;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[i];
        }

        q = temp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        StdOut.println("isEmpty(): " + q.isEmpty());
        StdOut.println("Adding elements...");

        q.enqueue("AA");
        q.enqueue("BB");
        q.enqueue("CC");
        q.enqueue("DD");

        StdOut.println("isEmpty(): " + q.isEmpty());

        StdOut.print("RandomizedQueue: ");
        for (String s : q) {
            StdOut.print(s + " ");
        }

        StdOut.println("");

        StdOut.println("sample(): " + q.sample());
        StdOut.println("sample(): " + q.sample());

        StdOut.println("isEmpty(): " + q.isEmpty());
        StdOut.print("RandomizedQueue: ");
        for (String s : q) {
            StdOut.print(s + " ");
        }

        StdOut.println("");

        StdOut.println("dequeue(): " + q.dequeue());
        StdOut.println("dequeue(): " + q.dequeue());
        StdOut.println("isEmpty(): " + q.isEmpty());
        StdOut.print("RandomizedQueue: ");
        for (String s : q) {
            StdOut.print(s + " ");
        }

        StdOut.println("dequeue(): " + q.dequeue());
        StdOut.println("dequeue(): " + q.dequeue());
        StdOut.println("isEmpty(): " + q.isEmpty());
    }

}
