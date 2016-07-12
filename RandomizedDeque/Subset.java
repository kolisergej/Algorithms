import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) { 
        int count = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
            if (queue.size() == count) {
                break;
            }
        }
        for (int i = 0; i < count; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}