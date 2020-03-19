import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.Random;

public class RandomizedQueue<Item> implements Iterable<Item> {

    int N = 0;
    Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        int capacity = 2;
        items = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return (N == 0); }

    // return the number of items on the randomized queue
    public int size() { return N; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add a null value!");
        if (N == items.length) resize(2*items.length);
        items[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Queue is empty!");
        int index = StdRandom.uniform(N);
        Item item = items[index];  // get item from random index
        items[index] = items[--N]; // overwrite array instance with the item at the end of the queue
        items[N] = null;           // remove the end item
        if (N > 0 && N == items.length/4) resize(items.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Queue is empty!");
        return items[StdRandom.uniform(N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomIterator(); }

    private class RandomIterator implements Iterator<Item> {

        boolean[] used;
        int count;

        RandomIterator() {
             used = new boolean[N];
             for (int i = 0; i < N; ++i) used[i] = false;
             count = 0;
        }

        public boolean hasNext() { return (count < N); }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No next element!");
            int index = StdRandom.uniform(N);
            if (used[index]) {
                return next();
            } else {
                used[index] = true;
                ++count;
                return items[index];
            }
        }
        public void remove() { throw new UnsupportedOperationException("Remove operation not yet supported for iterators"); }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; ++i) copy[i] = items[i];
        items = copy;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<>();

        System.out.println("Created new RandomizedQueue, currently empty: " + randomQueue.isEmpty());

        for (int i = 0; i < 100; ++i) {
            randomQueue.enqueue(i);
        }

        System.out.println("Added the numbers 0 to 99. Current size: " + randomQueue.size());
        System.out.println("Random sample: " + randomQueue.sample());

        Iterator<Integer> iterator = randomQueue.iterator();
        System.out.println("Randomly iterating through 10 elements:");
        for(int i = 0; i < 10; ++i) System.out.println(iterator.next());
        System.out.println("Randomly iterating through 10 elements again, with the same iterator:");
        for(int i = 0; i < 10; ++i) System.out.println(iterator.next());
        System.out.println("Current size: " + randomQueue.size());


        System.out.println("Randomly removing 10 elements: ");
        for (int i = 0; i < 10; ++i) System.out.println(randomQueue.dequeue());
        System.out.println("Current size: " + randomQueue.size());
    }

}