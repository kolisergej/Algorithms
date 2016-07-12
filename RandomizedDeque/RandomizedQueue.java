import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] mItems;
    private int mSize;

    public RandomizedQueue() {
        mSize = 0;
        mItems = (Item[]) new Object[1];
    }
    public boolean isEmpty() {
        return mSize == 0;
    }
    
    public int size() {
        return mSize;
    }
    
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Null element");
        }
        if (mSize == mItems.length)
        {
            resize(2 * mItems.length);
        }
        mItems[mSize++] = item;
    }
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        int index = StdRandom.uniform(0, mSize);
        Item item = mItems[index];
        mItems[index] = mItems[mSize - 1];
        mItems[--mSize] = null;
        if (mSize > 0 && mSize == mItems.length / 4)
        {
            resize(mItems.length / 2);
        }
        return item;
    }
    
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        int index = StdRandom.uniform(0, mSize);
        return mItems[index];
    }
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < mSize; i++) {
            copy[i] = mItems[i];
        }
        mItems = copy;
    }
    
    private class ArrayIterator implements Iterator<Item> {
        private int currentIndex = 0;
        private Item[] iteratorItems;
        
        public ArrayIterator() {
            if (!isEmpty()) {
                iteratorItems = (Item[]) new Object[mSize];
                for (int i = 0; i < mSize; i++) {
                    iteratorItems[i] = mItems[i];
                }
                StdRandom.shuffle(iteratorItems, 0, mSize - 1);
            }
        }
        
        public boolean hasNext() {
            return currentIndex != mSize;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Unsupported operation");
        }
        
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Now such element");
            }
            Item item = iteratorItems[currentIndex++];
            return item;            
        }
    }
    
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.enqueue(833);
        queue.enqueue(418);
        queue.enqueue(833);
        queue.enqueue(418);
        queue.enqueue(833);
        queue.enqueue(418);
        queue.dequeue();
        queue.dequeue();
        for (Integer string : queue) {
            StdOut.println(string);
        }
    }
}