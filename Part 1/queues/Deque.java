import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null received");
        }

        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldFirst;

        if (isEmpty()) last = first;
        else oldFirst.prev = first;

        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null received");
        }

        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.prev = oldLast;

        if (isEmpty()) first = last;
        else oldLast.next = last;

        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Node<Item> oldFirst = first;
        first = first.next;

        n--;
        if (isEmpty()) last = null;
        else first.prev = null;

        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Node<Item> oldLast = last;
        last = last.prev;

        n--;
        if (isEmpty()) first = null;
        else last.next = null;

        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator(first);
    }

    private class ListIterator implements Iterator<Item> {

        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        StdOut.println("isEmpty(): " + deque.isEmpty());

        StdOut.println("Adding elements...");
        deque.addFirst("B");
        deque.addLast("C");
        deque.addLast("D");
        deque.addFirst("A");

        StdOut.println("isEmpty(): " + deque.isEmpty());

        StdOut.print("Deque: ");
        for (String s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println("");

        StdOut.println("removeFirst(): " + deque.removeFirst());
        StdOut.println("removeLast(): " + deque.removeLast());

        StdOut.print("Deque: ");
        for (String s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println("");

        StdOut.println("removeLast(): " + deque.removeLast());
        StdOut.println("removeLast(): " + deque.removeLast());

        StdOut.print("Deque: ");
        for (String s : deque) {
            StdOut.print(s + " ");
        }
        StdOut.println("");

        StdOut.println("isEmpty(): " + deque.isEmpty());
    }

}
