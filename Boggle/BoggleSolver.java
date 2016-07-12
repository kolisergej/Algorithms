import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashSet;

public class BoggleSolver
{
    private FastTST<Integer> mDictionary = new FastTST<Integer>();

//    private class FastTST<Value> {
//        private int N;              // size
//        private Node<Value> root;   // root of TST
//
//        private class Node<Value> {
//            private char c;                        // character
//            private Node<Value> left, mid, right;  // left, middle, and right subtries
//            private Value val;                     // value associated with string
//        }
//
//        public FastTST() {
//        }
//
//        public Value get(String key) {
//            Node<Value> x = get(root, key, 0);
//            Value result = null;
//            if (x != null) {
//                result = x.val;
//            }
//            return result;
//        }
//
//        private Node<Value> get(Node<Value> x, String key, int d) {
//            Node<Value> result = null;
//            if (x != null) {
//                char c = key.charAt(d);
//                if      (c < x.c) result = get(x.left,  key, d);
//                else if (c > x.c) result = get(x.right, key, d);
//                else if (d < key.length() - 1) result = get(x.mid,   key, d+1);
//                else                           result = x;
//            }
//            return result;
//        }
//
//        public void put(String key, Value val) {
//            if (get(key) == null) {
//                N++;
//            }
//            root = put(root, key, val, 0);
//        }
//
//        private Node<Value> put(Node<Value> x, String key, Value val, int d) {
//            char c = key.charAt(d);
//            if (x == null) {
//                x = new Node<Value>();
//                x.c = c;
//            }
//            if      (c < x.c)               x.left  = put(x.left,  key, val, d);
//            else if (c > x.c)               x.right = put(x.right, key, val, d);
//            else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
//            else                            x.val   = val;
//            return x;
//        }
//
//        public boolean hasPrefix(String prefix) {
//            Node<Value> x = get(root, prefix, 0);
//            boolean result = false;
//            if (x != null) {
//                if (x.val != null) {
//                    result = true;
//                }
//                else if (x.left == null && x.right == null && x.mid == null) {
//                    result = false;
//                }
//                else {
//                    result = true;
//                }
//            }
//            return result;
//        }
//    }
    
    private class FastTST<Value> {
        private static final int R = 26;
        private static final int OFFSET = 65;    
        
        private Node root = new Node();

        private class Node<Value> {
            private Object val;
            private Node<Value>[] next = new Node[R];
        }

        public Value get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return null;
            return (Value) x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d+1);
        }

        public void put(String key, Value val) {
            root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Value val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, val, d+1);
            return x;
        }

        public boolean hasPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            mDictionary.put(dictionary[i], i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> foundWords = new HashSet<>();
        int rows = board.rows();
        int cols = board.cols();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < cols; column++) {
                String prefix = "";
                boolean[] isVisited = new boolean [rows * cols];
                dfs(isVisited, row, column, prefix, board, foundWords);
            }
        }
        return foundWords;
    }

    private void dfs(boolean[] isVisited, int row, int column, String prefix, BoggleBoard board, HashSet<String> foundWords) {
        if (!isVisited[row * board.cols() + column]) {
            char symbol = board.getLetter(row, column); 
            if (symbol == 'Q') {
                prefix += "QU";
            }
            else {
                prefix += symbol;
            }

            isVisited[row * board.cols() + column] = true;
            if (mDictionary.hasPrefix(prefix)) {
                if (prefix.length() > 2 && mDictionary.get(prefix) != null) {
                    foundWords.add(prefix);
                }

                int rows = board.rows();
                int cols = board.cols();
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i == 0 && j == 0) {
                            continue;
                        }
                        if ((row + i >= 0) && (row + i < rows) && (column + j >= 0) && (column + j < cols)) {
                            if (isVisited[(row + i) * board.cols() + column + j]) continue;
                            dfs(isVisited, row + i, column + j, prefix, board, foundWords);
                        }
                    }
                }
            }
            isVisited[row * board.cols() + column] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || word.length() == 0) {
            throw new NullPointerException();
        }
        int result = 0;
        if (mDictionary.get(word) != null) {
            switch (word.length()) {
            case 1:
            case 2:
                result = 0;
                break;
            case 3:
            case 4:
                result = 1;
                break;
            case 5:
                result = 2;
                break;
            case 6:
                result = 3;
                break;
            case 7:
                result = 5;
                break;
            default:
                result = 11;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        Stopwatch s = new Stopwatch();
        for (String word : solver.getAllValidWords(board))
        {
                        StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println(s.elapsedTime());
        StdOut.println("Score = " + score);
    }

}
