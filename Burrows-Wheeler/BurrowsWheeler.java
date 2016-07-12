import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void encode() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray sA = new CircularSuffixArray(str);

        for (int i = 0; i < sA.length(); i++) {
            if (sA.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < sA.length(); i++) {
            int position = (sA.index(i) + str.length() - 1) % str.length();
            BinaryStdOut.write(str.charAt(position));
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void decode() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int N = s.length();
        int[] next = new int[N];

        int[] count = new int[R + 1];
        for (int d = N - 1; d >= 0; d--)
            count[s.charAt(d) + 1]++;

        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        for (int i = 0; i < N; i++) {
            next[count[s.charAt(i)]++] = i;
        }

        int index = next[first];
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(s.charAt(index));
            index = next[index];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
