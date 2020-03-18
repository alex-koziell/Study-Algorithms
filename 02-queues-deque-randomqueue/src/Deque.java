import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Item[] items;
    private int head;   // index of head of deque
    private int tail;   // index of tail of deque

    // construct an empty deque
    public Deque() {
        int capacity = 2;
        head = capacity/2 - 1;
        tail = capacity/2;
        items = (Item[]) new Object[capacity];   // The "ugly cast"
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (head+1 == tail);
    }

    // return the number of items on the deque
    public int size() {
        return tail-head-1;
    }

    // add the item to the front
    public void addFirst(Item item) {
        items[head--] = item;
        if (head == -1) resize(2 * items.length); // repeated doubling
    }

    // add the item to the back
    public void addLast(Item item) {
        items[tail++] = item;
        if (tail == items.length) resize(2 * items.length);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new NoSuchElementException("The deque is currently empty!");
        Item item = items[++head];
        items[head] = null;
        if (size() > 0 && size() < items.length/4) resize(items.length/2);
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size() == 0) throw new NoSuchElementException("The deque is currently empty!");
        Item item = items[--tail];
        items[tail] = null;
        if (size() > 0 && size() < items.length/4) resize(items.length/2);
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new ArrayIterator(); }

    private class ArrayIterator implements Iterator<Item> {
        private int i = head;
        private int end = tail-1;

        public boolean hasNext() { return i < end; }
        public void remove() { throw new UnsupportedOperationException("Remove operation not yet supported for iterators"); }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to iterate!");
            return  items[++i];
        }
    }

    // Double capacity at head end of items[]
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int oldSize = size();
        for (int i=0; i < oldSize; ++i) copy[i+capacity/4] = items[head+i+1];
        head = capacity/4 - 1;
        tail = capacity/4 + oldSize;
        items = copy;
    }

    public int capacity() {
        return items.length;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        System.out.println("Intially empty? " + deque.isEmpty());
        System.out.println("Size: " + deque.size());

        for (int i = 0; i < 10; ++i) {
            System.out.println("Adding " + i + " to end from current size: " + deque.size() + ". Capacity: " + deque.capacity());
            deque.addLast(i);
        }

        Iterator<Integer> iterator = deque.iterator();
        System.out.println("Iterating elements from head to tail: ");
        while (iterator.hasNext()) System.out.println(iterator.next());

        System.out.println("Emptying deque...");
        while (!deque.isEmpty()) System.out.println("Removing " + deque.removeFirst() + ". Size: " + deque.size() + ". Capacity: " + deque.capacity());


        System.out.println("Empty? " + deque.isEmpty());
        System.out.println("Size: " + deque.size());
    }

}
