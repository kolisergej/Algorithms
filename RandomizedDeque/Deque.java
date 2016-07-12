import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item mValue;
        private Node next = null;
        private Node prev = null;
    }
    private int mSize;
    private Node head;
    private Node tail;

    public Deque() {
        mSize = 0;
        head = null;
        tail = null;
    }
    public boolean isEmpty() {
        return mSize == 0;
    }
    public int size() {
        return mSize;
    }
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("Null element");
        }
        Node oldHead = head;
        head = new Node();
        head.mValue = item;
        head.prev = oldHead;
        if (isEmpty())
        {
            tail = head;
        }
        else {            
            oldHead.next = head;
        }
        ++mSize;
    }
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("Null element");
        }
        Node oldTail = tail;
        tail = new Node();
        tail.mValue = item;
        tail.next = oldTail;
        if (isEmpty()) {
            head = tail;
        }
        else {
            oldTail.prev = tail;
        }
        ++mSize;
    }
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        Item element = head.mValue;
        if (head == tail)
        {
            head = null;
            tail = null;
        }
        else {
            head = head.prev;
            head.next = null;
        }
        --mSize;
        return element;
    }
    
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue");
        }
        Item element = tail.mValue;
        if (head == tail)
        {
            head = null;
            tail = null;
        }
        else {
            tail = tail.next;
            tail.prev = null;
        }
        --mSize;
        return element;
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = head;
        
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new UnsupportedOperationException("Unsupported operation");
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Now such element");
            }
            Item item = current.mValue;
            current = current.prev;
            return item;            
        }
    }
    
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addLast("1");
        deque.removeLast();
        
        for (String string : deque) {
            StdOut.println(string);
        }
    }
}