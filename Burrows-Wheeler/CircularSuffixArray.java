
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private static final int R = 256;   // extended ASCII alphabet size
    private static final int CUTOFF = 10;   // cutoff to insertion sort
    private int mLength;
    private int[] mIndex;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new java.lang.NullPointerException();
        }

        mLength = s.length();
        mIndex = new int[mLength];
        int[] aux = new int[mLength];
        for (int i = 0; i < mIndex.length; i++) {
            mIndex[i] = i;
        }
        
        sort(s, 0, mLength-1, 0, aux);
    }

    private void sort(String a, int lo, int hi, int d, int[] aux) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // compute frequency counts
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a, mIndex[i], d);
            count[c+2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a, mIndex[i], d);
            aux[count[c+1]++] = mIndex[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++) 
            mIndex[i] = aux[i - lo];


        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }

    
    // length of s
    public int length() {
        return mLength;
    }
    
    private int charAt(String s, int suffix, int offset) {
        if (offset == s.length()) {
            return -1;
        }
        return s.charAt((suffix + offset) % s.length());
    }

    
    private void exch(int i, int j) {
        int tmp = mIndex[i];
        mIndex[i] = mIndex[j];
        mIndex[j] = tmp;
    }

    // Insertion sort starting at index offset. Code adapted from
    // http://algs4.cs.princeton.edu/51radix/Quick3string.java.html
    private void insertion(String s, int lo, int hi, int offset) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, j, j - 1, offset); j--)
                exch(j, j - 1);
    }

    // Is suffix i less than suffix j, starting at offset
    private boolean less(String s, int i, int j, int offset) {
        int oi = mIndex[i], oj = mIndex[j];
        for (; offset < mIndex.length; offset++) {
            int ival = charAt(s, oi, offset);
            int jval = charAt(s, oj, offset);
            if (ival < jval)
                return true;
            else if (ival > jval)
                return false;
        }
        return false;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 && i > mLength - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return mIndex[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray cA = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < cA.length(); i++) {            
            StdOut.println(cA.index(i));
        }
    }
}
