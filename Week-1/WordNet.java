import java.util.HashMap;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

    private final HashMap<Integer, String> id2Noun;
    private final HashMap<String, Bag<Integer>> noun2Id;
    private final Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        id2Noun = new HashMap<Integer, String>();
        noun2Id = new HashMap<String, Bag<Integer>>();

        In readSynsets = new In(synsets);
        while (readSynsets.hasNextLine()) {
            String[] lineWiseSynsets = readSynsets.readLine().split(",");
            int id = Integer.parseInt(lineWiseSynsets[0]);
            String synset = lineWiseSynsets[1];
            String[] nouns = synset.split(" ");
            id2Noun.put(id, synset);

            for (String w : nouns) {
                if (noun2Id.containsKey(w)) {
                    Bag<Integer> bag = noun2Id.get(w);
                    bag.add(id);
                    noun2Id.put(w, bag);
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(Integer.valueOf(id));
                    noun2Id.put(w, bag);
                }
            }
        }

        digraph = new Digraph(id2Noun.size());

        In readHypernyms = new In(hypernyms);
        while (readHypernyms.hasNextLine()) {
            String[] lineWiseHypernyms = readHypernyms.readLine().split(",");
            int id = Integer.parseInt(lineWiseHypernyms[0]);
            Bag<Integer> bag = new Bag<Integer>();

            for (int i = 1; i < lineWiseHypernyms.length; i++) {
                int hypernymId = Integer.parseInt(lineWiseHypernyms[i]);
                bag.add(Integer.valueOf(hypernymId));
                digraph.addEdge(id, hypernymId);
            }
        }
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }

        int start = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (!digraph.adj(i).iterator().hasNext()) {
                start++;
            }
        }

        if (start != 1) {
            throw new IllegalArgumentException();
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun2Id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return noun2Id.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        Bag<Integer> A = noun2Id.get(nounA);
        Bag<Integer> B = noun2Id.get(nounB);

        SAP sap = new SAP(digraph);
        int result = sap.length(A, B);

        return result;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        Bag<Integer> idsA = noun2Id.get(nounA);
        Bag<Integer> idsB = noun2Id.get(nounB);
        SAP sap = new SAP(digraph);
        int ancestor = sap.ancestor(idsA, idsB);

        return id2Noun.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}