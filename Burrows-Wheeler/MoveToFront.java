import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int SIZE = 256;

    public static void encode() {
        char[] seq = getAsciiArray();
        
        char c;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            for (int i = 0; i < seq.length; i++) {
                if (seq[i] == c) {
                    if (i != 0) {
                        System.arraycopy(seq, 0, seq, 1, i);
                        seq[0] = c;
                    }
                    BinaryStdOut.write((char) i);
                    break;
                }
            }
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] seq = getAsciiArray();
        
        int r;
        char c;
        while (!BinaryStdIn.isEmpty()) {
            r = BinaryStdIn.readChar();
            c = seq[r];
            if (r != 0) {
                System.arraycopy(seq, 0, seq, 1, r);
                seq[0] = c;
            }            
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }
    
    private static char[] getAsciiArray() {
        char[] seq = new char[SIZE];
        for (int i = 0; i < SIZE; i++) {
            seq[i] = (char) i;
        }
        return seq;
    }
    
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
