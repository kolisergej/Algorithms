import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.AbstractMap;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph mG;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new NullPointerException();
        }
        mG = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        
        BreadthFirstDirectedPaths bfdpFromV = new BreadthFirstDirectedPaths(mG, v);
        BreadthFirstDirectedPaths bfdpFromW = new BreadthFirstDirectedPaths(mG, w);
        
        return findResultPair(bfdpFromV, bfdpFromW).getKey();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVertex(v);
        checkVertex(w);

        
        BreadthFirstDirectedPaths bfdpFromV = new BreadthFirstDirectedPaths(mG, v);
        BreadthFirstDirectedPaths bfdpFromW = new BreadthFirstDirectedPaths(mG, w);

        return findResultPair(bfdpFromV, bfdpFromW).getValue();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertexes(v);
        checkVertexes(w);
        
        BreadthFirstDirectedPaths bfdpFromV = new BreadthFirstDirectedPaths(mG, v);
        BreadthFirstDirectedPaths bfdpFromW = new BreadthFirstDirectedPaths(mG, w);

        return findResultPair(bfdpFromV, bfdpFromW).getKey();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertexes(v);
        checkVertexes(w);
 
        BreadthFirstDirectedPaths bfdpFromV = new BreadthFirstDirectedPaths(mG, v);
        BreadthFirstDirectedPaths bfdpFromW = new BreadthFirstDirectedPaths(mG, w);

        return findResultPair(bfdpFromV, bfdpFromW).getValue();
    }
    
    private Entry<Integer, Integer> findResultPair(BreadthFirstDirectedPaths bfdpFromV, BreadthFirstDirectedPaths bfdpFromW) {
        int result = -1;
        int distance = -1;
        for (int i = 0; i < mG.V(); i++) {
            if (bfdpFromV.hasPathTo(i) && bfdpFromW.hasPathTo(i)) {
                int dist = bfdpFromV.distTo(i) + bfdpFromW.distTo(i);
                if (distance == -1 || dist < distance) {
                    distance = dist;
                    result = i;
                }
            }   
        }
        return new AbstractMap.SimpleEntry<Integer, Integer>(distance, result);
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex > mG.V() - 1) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkVertexes(Iterable<Integer> vertexes) {
        for (int vertex : vertexes) {
            checkVertex(vertex);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In input = new In(args[0]);
        Digraph G = new Digraph(input);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}