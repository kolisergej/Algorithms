
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private HashMap<Integer, List<String> > mIdSynset;
    private HashMap<String, List<Integer> > mSynonimId;
    private HashMap<Integer, String> mSynset;
    private SAP mSap;
    private Digraph mHypernyms;
    
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException();
        }
        
        mIdSynset = new HashMap<>();
        mSynonimId = new HashMap<>();
        mSynset = new HashMap<>();
        
        In synsetsInput = new In(synsets);
        In hypernymsInput = new In(hypernyms);

        String line = synsetsInput.readLine();
        while (line != null) {
            String[] splitByCommas = line.split(",");
            int id = Integer.parseInt(splitByCommas[0]);
            mSynset.put(id, splitByCommas[1]);
            
            List<String> list = new ArrayList<String>();
            String[] splitSynsetBySpace = splitByCommas[1].split(" ");
            for (int i = 0; i < splitSynsetBySpace.length; i++) {
                list.add(splitSynsetBySpace[i]);
                
                List<Integer> tmpList = mSynonimId.get(splitSynsetBySpace[i]);
                if (tmpList == null) {
                    tmpList = new ArrayList<>();
                } 
                tmpList.add(id);
                mSynonimId.put(splitSynsetBySpace[i], tmpList);
            }
            mIdSynset.put(id, list);
            line = synsetsInput.readLine();
        }
        
        mHypernyms = new Digraph(mIdSynset.size());
        line = hypernymsInput.readLine();
        while (line != null) {
            String[] splitByCommas = line.split(",");
            int from = Integer.parseInt(splitByCommas[0]);
            for (int i = 1; i < splitByCommas.length; i++) {
                mHypernyms.addEdge(from, Integer.parseInt(splitByCommas[i]));
            }
            line = hypernymsInput.readLine();
        }
        
        mSap = new SAP(mHypernyms);
        DirectedCycle cycleGraph = new DirectedCycle(mHypernyms);
        int blankVertices = 0;
        for (int i = 0; i < mHypernyms.V(); i++) {
            Iterable<Integer> have = mHypernyms.adj(i);
            List<Integer> myList = new ArrayList<>();
            for (int e : have) {
                myList.add(e);
                break;
            }
            if (myList.size() == 0) {
                blankVertices++;
                if (blankVertices > 1) {
                    break;
                }
            }
        }

        if (cycleGraph.hasCycle() || blankVertices > 1) {
            throw new IllegalArgumentException();
        }
    }
    
 // returns all WordNet nouns
    public Iterable<String> nouns() {
        return mSynonimId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        return mSynonimId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        if (!mSynonimId.containsKey(nounA) || !mSynonimId.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        
        return mSap.length(mSynonimId.get(nounA), mSynonimId.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        
        if (!mSynonimId.containsKey(nounA) || !mSynonimId.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        
        int ancestral = mSap.ancestor(mSynonimId.get(nounA), mSynonimId.get(nounB));
        return mSynset.get(ancestral);
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}