import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    private WordNet mWordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        mWordnet = wordnet;

    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int distance = Integer.MIN_VALUE;
        String result = nouns[0];
        for (int i = 0; i < nouns.length; i++) {
            int tmpDistance = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    tmpDistance += mWordnet.distance(nouns[i], nouns[j]);
                }
            }
            if (distance < tmpDistance) {
                result = nouns[i];
                distance = tmpDistance;
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}